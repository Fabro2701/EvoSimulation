package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import simulator.model.map.creator.MapCreator.PanelManager;

/**
 * Creates the elevation attribute of a map
 * 
 * @author fabrizioortega
 */
public class ElevationPanel extends AbstractCreatorPanel {

	public ElevationPanel(PanelManager panelManager) {
		super(panelManager);
		ctrl = new ElevationPanel.Controller(1000, 1000, new Color(255, 255 / 2, 0, 255));
		modificationPanel.setViewportView(ctrl.img);
		viewPanel.setViewportView(ctrl.viewObserver);
	}

	protected class Controller extends AbstractCreatorPanel.Controller {

		protected int strokeSize, intensity;

		// variables of the 2.5D view
		private float xSkew = 1.0f;
		private float ySkew = 1.0f;
		int originX = 0;
		int originY = 0;
		int xs = 10;

		public Controller(int width, int height, Color initColor) {
			super(width, height, initColor);

			strokeSize = 50;
			intensity = 50;

			img = new ModificationImage(new ImageIcon(bufferImage));

			viewObserver = new ViewImage();
			viewObserver.update(bufferImage);
		}

		public void setIntensity(int value) {
			intensity = value;
		}

		public void setStrokeSize(int value) {
			strokeSize = value;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				bufferGraphics.setColor(new Color(255, 255, 0, intensity));
			} else {
				bufferGraphics.setColor(new Color(255, 0, 0, intensity));
			}

			bufferGraphics.fillOval(e.getX() - strokeSize / 2, e.getY() - strokeSize / 2, strokeSize, strokeSize);
			current.setLocation(e.getPoint());
			ElevationPanel.this.repaint();
			// img.setIcon(new ImageIcon(bufferImage));
			viewObserver.update(bufferImage);

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		protected class ViewImage extends AbstractCreatorPanel.Controller.ViewImage {
			int reducedWidth, reducedHeight;
			BufferedImage buffer;
			int ownWidth,ownHeight;
			public ViewImage() {
				ownWidth=700;
				ownHeight=700;
				reducedWidth = Controller.this.width/5;
				reducedHeight = Controller.this.height/5;
				
				KeyListener keyListener = new KeyListener() {

					@Override
					public void keyPressed(KeyEvent ke) {
						int c = ke.getKeyCode();
						switch (c) {
						case KeyEvent.VK_W:
							originY -= 10;
							break;
						case KeyEvent.VK_S:
							originY += 10;
							break;
						case KeyEvent.VK_A:
							originX -= 10;
							break;
						case KeyEvent.VK_D:
							originX += 10;
							break;
						default:
						}
						update(buffer);

					}

					@Override
					public void keyReleased(KeyEvent ke) {
					}

					@Override
					public void keyTyped(KeyEvent ke) {
					}

				};
				this.addKeyListener(keyListener);
				this.setFocusable(true);
				this.requestFocusInWindow();
				MouseAdapter mouse = new MouseAdapter() {
					boolean pressed = false;
					Point current = null;

					@Override
					public void mouseClicked(MouseEvent e) {
						focus();
					}

					@Override
					public void mouseWheelMoved(MouseWheelEvent e) {
						if (e.getWheelRotation() != 0) {
							xs += e.getWheelRotation();
							update(buffer);
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
						focus();
						if (!pressed) {
							pressed = true;
							current = e.getPoint();
						}
						//
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						pressed = false;

					}

					@Override
					public void mouseDragged(MouseEvent e) {
						if (pressed) {
							Point dir = e.getPoint();
							if (dir.equals(current))
								return;
							int dx = dir.x - current.x;
							int dy = dir.y - current.y;

							float decreaseFactor = 1000.0f;
							xSkew += (float) dx / decreaseFactor;
							ySkew += (float) dy / decreaseFactor;
							// System.out.println(xSkew+" "+ySkew);
							update(buffer);
						}
					}
				};
				this.addMouseListener(mouse);
				this.addMouseMotionListener(mouse);
				this.addMouseWheelListener(mouse);
			}

			public void focus() {
				this.requestFocusInWindow();
			}

			private BufferedImage reduceImage(BufferedImage img, int w, int h) {
				Image imgS = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
				BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) image2.createGraphics();
				g2d.drawImage(imgS, 0, 0, w, h, null);
				return image2;
			}

			public void update(BufferedImage image) {
				reducedWidth = Controller.this.width/5;
				reducedHeight = Controller.this.height/5;
				buffer = image;
				BufferedImage transformedImage = new BufferedImage(this.ownWidth, this.ownHeight, BufferedImage.TYPE_INT_ARGB);
				BufferedImage reducedImage = reduceImage(image, reducedWidth, reducedHeight);
				Graphics2D g2d = transformedImage.createGraphics();
				g2d.setColor(Color.black);
				g2d.fillRect(0, 0, this.ownWidth, this.ownHeight);
				g2d.setColor(Color.green);

				Point2D point1;
				Point2D point2;

				float inskew = 2.0f;

				for (int i = 0; i < reducedImage.getHeight(); i++) {
					for (int j = 0; j < reducedImage.getWidth(); j++) {
						point1 = transform3D(new Point3D(j * xs,
								(int) (new Color(reducedImage.getRGB(j, i), true).getGreen() * inskew), i * xs));
						if (j < reducedImage.getWidth() - 1) {

							point2 = transform3D(new Point3D((j + 1) * xs,
									(int) (new Color(reducedImage.getRGB(j + 1, i), true).getGreen() * inskew),
									i * xs));
							g2d.drawLine(point1.x + originX, point1.y + originY, point2.x + originX,
									point2.y + originY);

						}
						if (i < reducedImage.getHeight() - 1) {

							point2 = transform3D(new Point3D(j * xs,
									(int) (new Color(reducedImage.getRGB(j, i + 1), true).getGreen() * inskew),
									(i + 1) * xs));

							g2d.drawLine(point1.x + originX, point1.y + originY, point2.x + originX,
									point2.y + originY);
						}
					}
				}

				this.setIcon(new ImageIcon(transformedImage));// transform
			}

			public Point2D transform3D(Point3D point3D) {
				return new Point2D((int) ((point3D.x + point3D.z) * xSkew),
						(int) (((-point3D.y) + point3D.z - point3D.x) * ySkew));
				// return new Point2D((int)((point3D.x + point3D.z)*xSkew), (int) (((-point3D.y)
				// + point3D.z) * ySkew));
			}

			public class Point2D {
				public Point2D(int x, int y) {
					this.x = x;
					this.y = y;
				}

				int x = 0;
				int y = 0;
			}

			public class Point3D {
				public Point3D(int x, int y, int z) {
					this.x = x;
					this.y = y;
					this.z = z;
				}

				int x = 0;
				int y = 0;
				int z = 0;
			}

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return null;
			}

			@Override
			public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
				return 0;
			}

			@Override
			public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
				return 0;
			}

			@Override
			public boolean getScrollableTracksViewportWidth() {
				return false;
			}

			@Override
			public boolean getScrollableTracksViewportHeight() {
				return false;
			}

		}
	}

	@Override
	public void initComponents() {

		modificationPanel = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jsStrokeSize = new javax.swing.JSlider();
		jLabel3 = new javax.swing.JLabel();
		jsIntensisty = new javax.swing.JSlider();
		viewPanel = new javax.swing.JScrollPane();
		jPanel2 = new javax.swing.JPanel();
		jbZoomIn = new javax.swing.JButton();
		jbZoomOut = new javax.swing.JButton();

		setMaximumSize(new java.awt.Dimension(1373, 682));
		setPreferredSize(new java.awt.Dimension(1373, 682));
		setSize(new java.awt.Dimension(1373, 682));

		modificationPanel.setBorder(new javax.swing.border.MatteBorder(null));
		modificationPanel.setMaximumSize(new java.awt.Dimension(600, 600));
		modificationPanel.setMinimumSize(new java.awt.Dimension(600, 600));
		modificationPanel.setPreferredSize(new java.awt.Dimension(600, 600));

		jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

		jLabel2.setText("Stroke Size");

		jsStrokeSize.setMinimum(1);
		jsStrokeSize.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jsStrokeSizeStateChanged(evt);
			}
		});

		jLabel3.setText("Intensity");

		jsIntensisty.setMinimum(1);
		jsIntensisty.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jsIntensistyStateChanged(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jsStrokeSize, javax.swing.GroupLayout.PREFERRED_SIZE, 109,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(jLabel3)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jsIntensisty, javax.swing.GroupLayout.PREFERRED_SIZE, 108,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap(19, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addComponent(jsStrokeSize, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
								.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
								.addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jsIntensisty, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(15, 15, 15)));

		viewPanel.setBorder(new javax.swing.border.MatteBorder(null));

		jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

		jbZoomIn.setText("Zoom in");
		jbZoomIn.setToolTipText("");
		jbZoomIn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbZoomInActionPerformed(evt);
			}
		});

		jbZoomOut.setText("Zoom out");
		jbZoomOut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbZoomOutActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jbZoomIn)
						.addGap(39, 39, 39).addComponent(jbZoomOut).addContainerGap(518, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel2Layout.createSequentialGroup().addContainerGap()
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jbZoomOut, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jbZoomIn, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(viewPanel).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(0, 29, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addComponent(viewPanel).addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
	}// </editor-fold>

	private void jbZoomInActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void jsStrokeSizeStateChanged(javax.swing.event.ChangeEvent evt) {
		((Controller) ctrl).setStrokeSize(jsStrokeSize.getValue());
		// TODO add your handling code here:
	}

	private void jsIntensistyStateChanged(javax.swing.event.ChangeEvent evt) {
		((Controller) ctrl).setIntensity(jsIntensisty.getValue());
	}

	private void jbZoomOutActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	// Variables declaration - do not modify
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JButton jbZoomIn;
	private javax.swing.JButton jbZoomOut;
	private javax.swing.JSlider jsIntensisty;
	private javax.swing.JSlider jsStrokeSize;
	// private javax.swing.JScrollPane modificationPanel;
	// private javax.swing.JScrollPane viewPanel;
	// End of variables declaration
}
