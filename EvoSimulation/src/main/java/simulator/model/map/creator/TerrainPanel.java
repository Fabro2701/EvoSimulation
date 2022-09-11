package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import javax.swing.ImageIcon;

import simulator.Constants.MAP_TYPE;
import simulator.model.map.creator.MapCreator.PanelManager;

/**
 * TerrainPanel creates the enumeration distinction of the map nodes
 * 
 * @author fabrizioortega
 *
 */
public class TerrainPanel extends AbstractCreatorPanel {

	public TerrainPanel(PanelManager panelManager) {
		super(panelManager);
		ctrl = new TerrainPanel.Controller(1000, 1000, new Color(0, 0, 0, 255));
		modificationPanel.setViewportView(ctrl.img);
	}

	protected class Controller extends AbstractCreatorPanel.Controller {
		protected int strokeSize;
		// selection is the current MAP_TYPE (Land,Void)
		protected MAP_TYPE selection;
		// featureMap map of MAP_TYPE's
		protected EnumMap<MAP_TYPE, Color> featureMap;

		public Controller(int width, int height, Color initColor) {
			super(width, height, initColor);

			strokeSize = 50;

			featureMap = new EnumMap<MAP_TYPE, Color>(MAP_TYPE.class);
			featureMap.put(MAP_TYPE.LAND, new Color(255, 255, 255, 255));
			featureMap.put(MAP_TYPE.VOID, new Color(0, 0, 0, 255));
			selection = MAP_TYPE.LAND;

			img = new ModificationImage(new ImageIcon(bufferImage));

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			bufferGraphics.setColor(featureMap.get(selection));
			bufferGraphics.fillOval(e.getX() - strokeSize / 2, e.getY() - strokeSize / 2, strokeSize, strokeSize);
			TerrainPanel.this.repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		public void setStrokeSize(int value) {
			strokeSize = value;
		}

		public void setSelection(String selectedItem) {
			if (selectedItem.equals("Land")) {
				selection = MAP_TYPE.LAND;
			} else {
				selection = MAP_TYPE.VOID;
			}
		}

		
	}

	@Override
	public void initComponents() {

		modificationPanel = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
		jComboBox1 = new javax.swing.JComboBox<>();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jsStrokeSize = new javax.swing.JSlider();
		jLabel3 = new javax.swing.JLabel();
		jtfSizeX = new javax.swing.JTextField();
		jtfSizeY = new javax.swing.JTextField();
		jScrollPane2 = new javax.swing.JScrollPane();
		jPanel2 = new javax.swing.JPanel();

		setMaximumSize(new java.awt.Dimension(1373, 682));
		setPreferredSize(new java.awt.Dimension(1373, 682));
		setSize(new java.awt.Dimension(1373, 682));

		modificationPanel.setBorder(new javax.swing.border.MatteBorder(null));
		modificationPanel.setMaximumSize(new java.awt.Dimension(600, 600));
		modificationPanel.setMinimumSize(new java.awt.Dimension(600, 600));
		modificationPanel.setPreferredSize(new java.awt.Dimension(600, 600));

		jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Land", "Void" }));
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox1ActionPerformed(evt);
			}
		});

		jLabel1.setText("Type");

		jLabel2.setText("Stroke Size");

		jsStrokeSize.setMinimum(1);
		jsStrokeSize.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jsStrokeSizeStateChanged(evt);
			}
		});

		jLabel3.setText("Canvas Size");

		jtfSizeX.setText("1000");
		jtfSizeX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jtfSizeXActionPerformed(evt);
			}
		});

		jtfSizeY.setText("1000");
		jtfSizeY.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jtfSizeYActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(28, 28, 28).addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jsStrokeSize, javax.swing.GroupLayout.PREFERRED_SIZE, 109,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)
						.addGap(18, 18, 18)
						.addComponent(jtfSizeX, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jtfSizeY, javax.swing.GroupLayout.PREFERRED_SIZE, 47,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap(19, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
														javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGap(15, 15, 15))
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
												.addComponent(jsStrokeSize, javax.swing.GroupLayout.DEFAULT_SIZE, 34,
														Short.MAX_VALUE)
												.addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jtfSizeY, javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jtfSizeX, javax.swing.GroupLayout.Alignment.LEADING))
										.addContainerGap()))));

		jScrollPane2.setBorder(new javax.swing.border.MatteBorder(null));

		jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(
								jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 766,
								javax.swing.GroupLayout.PREFERRED_SIZE))
						.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jScrollPane2))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
	}// </editor-fold>

	private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
		((Controller) ctrl).setSelection((String) jComboBox1.getSelectedItem());
	}

	private void jtfSizeXActionPerformed(java.awt.event.ActionEvent evt) {
		panelManager.globalResize(Integer.valueOf(jtfSizeX.getText()), Integer.valueOf(jtfSizeY.getText()));
		//((Controller) ctrl).resize(Integer.valueOf(jtfSizeX.getText()), Integer.valueOf(jtfSizeY.getText()));
	}

	private void jtfSizeYActionPerformed(java.awt.event.ActionEvent evt) {
		panelManager.globalResize(Integer.valueOf(jtfSizeX.getText()), Integer.valueOf(jtfSizeY.getText()));
		//((Controller) ctrl).resize(Integer.valueOf(jtfSizeX.getText()), Integer.valueOf(jtfSizeY.getText()));
	}

	private void jsStrokeSizeStateChanged(javax.swing.event.ChangeEvent evt) {
		((Controller) ctrl).setStrokeSize(jsStrokeSize.getValue());
	}

	// Variables declaration - do not modify
	private javax.swing.JComboBox<String> jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	// private javax.swing.JScrollPane modificationPanel;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSlider jsStrokeSize;
	private javax.swing.JTextField jtfSizeX;
	private javax.swing.JTextField jtfSizeY;
	// End of variables declaration
}

/*
 * public class TerrainPanel extends javax.swing.JPanel { private Controller
 * ctrl;
 * 
 * public TerrainPanel() { initComponents(); ctrl = new Controller();
 * modificationPanel.setViewportView(ctrl.img); } public class Controller
 * implements MouseListener, MouseMotionListener{ private Graphics2D
 * bufferGraphics; private BufferedImage bufferImage; private int strokeSize;
 * private int width, height; private ModificationImage img; private Point
 * current; private MAP_TYPE selection; private
 * EnumMap<MAP_TYPE,Color>featureMap;
 * 
 * public Controller() { width = 800; height = 800;
 * 
 * strokeSize = 50;
 * 
 * 
 * bufferImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
 * bufferGraphics = bufferImage.createGraphics(); bufferGraphics.setColor(new
 * Color(0,0,0,255)); bufferGraphics.fillRect(0, 0, width, height);
 * 
 * img = new ModificationImage(new ImageIcon(bufferImage), this); current = new
 * Point();
 * 
 * 
 * featureMap = new EnumMap<MAP_TYPE,Color>(MAP_TYPE.class);
 * featureMap.put(MAP_TYPE.LAND,new Color(255,255,255,255));
 * featureMap.put(MAP_TYPE.VOID,new Color(0,0,0,255)); selection =
 * MAP_TYPE.LAND; } public void resize(int x, int y) {
 * 
 * Image tmp = bufferImage.getScaledInstance(x, y, Image.SCALE_DEFAULT);
 * 
 * width = x; height = y; bufferImage = new BufferedImage(width,
 * height,BufferedImage.TYPE_INT_ARGB); bufferGraphics =
 * bufferImage.createGraphics(); bufferGraphics.drawImage(tmp, 0, 0, null);
 * 
 * img.setIcon(new ImageIcon(bufferImage)); }
 * 
 * @Override public void mousePressed(MouseEvent e) {
 * current.setLocation(e.getPoint()); }
 * 
 * @Override public void mouseDragged(MouseEvent e) { // TODO Auto-generated
 * method stub bufferGraphics.setStroke(new BasicStroke(strokeSize));
 * bufferGraphics.setColor(featureMap.get(selection));
 * //bufferGraphics.drawLine(current.x,current.y,e.getX(),e.getY());
 * bufferGraphics.fillOval(e.getX()-strokeSize/2, e.getY()-strokeSize/2,
 * strokeSize, strokeSize);
 * 
 * img.setIcon(new ImageIcon(bufferImage)); current.setLocation(e.getPoint());
 * 
 * } private class ModificationImage extends JLabel implements Scrollable{
 * public ModificationImage(ImageIcon icon, Controller ctrl) { super(icon);
 * this.addMouseListener(ctrl); this.addMouseMotionListener(ctrl); }
 * 
 * @Override public Dimension getPreferredScrollableViewportSize() { // TODO
 * Auto-generated method stub return null; }
 * 
 * @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int
 * orientation, int direction) { // TODO Auto-generated method stub return 0; }
 * 
 * @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int
 * orientation, int direction) { // TODO Auto-generated method stub return 0; }
 * 
 * @Override public boolean getScrollableTracksViewportWidth() { // TODO
 * Auto-generated method stub return false; }
 * 
 * @Override public boolean getScrollableTracksViewportHeight() { // TODO
 * Auto-generated method stub return false; } }
 * 
 * 
 * @Override public void mouseClicked(MouseEvent e) {}
 * 
 * @Override public void mouseReleased(MouseEvent e) {}
 * 
 * @Override public void mouseEntered(MouseEvent e) {}
 * 
 * @Override public void mouseExited(MouseEvent e) {}
 * 
 * @Override public void mouseMoved(MouseEvent e) {} public void
 * setStrokeSize(int value) { // TODO Auto-generated method stub
 * strokeSize=value; } public void setSelection(String selectedItem) {
 * if(selectedItem.equals("Land")) { selection = MAP_TYPE.LAND; } else {
 * selection = MAP_TYPE.VOID; } }
 * 
 * }
 * 
 * 
 * 
 * @SuppressWarnings("unchecked") // <editor-fold defaultstate="collapsed"
 * desc="Generated Code"> private void initComponents() {
 * 
 * modificationPanel = new javax.swing.JScrollPane(); jPanel1 = new
 * javax.swing.JPanel(); jComboBox1 = new javax.swing.JComboBox<>(); jLabel1 =
 * new javax.swing.JLabel(); jLabel2 = new javax.swing.JLabel(); jsStrokeSize =
 * new javax.swing.JSlider(); jLabel3 = new javax.swing.JLabel(); jtfSizeX = new
 * javax.swing.JTextField(); jtfSizeY = new javax.swing.JTextField();
 * jScrollPane2 = new javax.swing.JScrollPane(); jPanel2 = new
 * javax.swing.JPanel();
 * 
 * setMaximumSize(new java.awt.Dimension(1373, 682)); setPreferredSize(new
 * java.awt.Dimension(1373, 682)); setSize(new java.awt.Dimension(1373, 682));
 * 
 * modificationPanel.setBorder(new javax.swing.border.MatteBorder(null));
 * modificationPanel.setMaximumSize(new java.awt.Dimension(600, 600));
 * modificationPanel.setMinimumSize(new java.awt.Dimension(600, 600));
 * modificationPanel.setPreferredSize(new java.awt.Dimension(600, 600));
 * 
 * jPanel1.setBorder(new javax.swing.border.MatteBorder(null));
 * 
 * jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
 * "Land", "Void" })); jComboBox1.addActionListener(new
 * java.awt.event.ActionListener() { public void
 * actionPerformed(java.awt.event.ActionEvent evt) {
 * jComboBox1ActionPerformed(evt); } });
 * 
 * jLabel1.setText("Type");
 * 
 * jLabel2.setText("Stroke Size");
 * 
 * jsStrokeSize.setMinimum(1); jsStrokeSize.addChangeListener(new
 * javax.swing.event.ChangeListener() { public void
 * stateChanged(javax.swing.event.ChangeEvent evt) {
 * jsStrokeSizeStateChanged(evt); } });
 * 
 * jLabel3.setText("Canvas Size");
 * 
 * jtfSizeX.setText("500"); jtfSizeX.addActionListener(new
 * java.awt.event.ActionListener() { public void
 * actionPerformed(java.awt.event.ActionEvent evt) {
 * jtfSizeXActionPerformed(evt); } });
 * 
 * jtfSizeY.setText("500"); jtfSizeY.addActionListener(new
 * java.awt.event.ActionListener() { public void
 * actionPerformed(java.awt.event.ActionEvent evt) {
 * jtfSizeYActionPerformed(evt); } });
 * 
 * javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
 * jPanel1.setLayout(jPanel1Layout); jPanel1Layout.setHorizontalGroup(
 * jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
 * .addGroup(jPanel1Layout.createSequentialGroup() .addContainerGap()
 * .addComponent(jLabel1)
 * .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
 * .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
 * javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
 * .addGap(28, 28, 28) .addComponent(jLabel2)
 * .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
 * .addComponent(jsStrokeSize, javax.swing.GroupLayout.PREFERRED_SIZE, 109,
 * javax.swing.GroupLayout.PREFERRED_SIZE)
 * .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
 * .addComponent(jLabel3) .addGap(18, 18, 18) .addComponent(jtfSizeX,
 * javax.swing.GroupLayout.PREFERRED_SIZE, 45,
 * javax.swing.GroupLayout.PREFERRED_SIZE)
 * .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
 * .addComponent(jtfSizeY, javax.swing.GroupLayout.PREFERRED_SIZE, 47,
 * javax.swing.GroupLayout.PREFERRED_SIZE)
 * .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) );
 * jPanel1Layout.setVerticalGroup(
 * jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
 * .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
 * jPanel1Layout.createSequentialGroup() .addContainerGap(19, Short.MAX_VALUE)
 * .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment
 * .LEADING) .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
 * jPanel1Layout.createSequentialGroup()
 * .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment
 * .BASELINE) .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
 * javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
 * .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
 * javax.swing.GroupLayout.PREFERRED_SIZE) .addComponent(jLabel2,
 * javax.swing.GroupLayout.PREFERRED_SIZE, 34,
 * javax.swing.GroupLayout.PREFERRED_SIZE)) .addGap(15, 15, 15))
 * .addGroup(jPanel1Layout.createSequentialGroup()
 * .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment
 * .TRAILING, false) .addComponent(jsStrokeSize,
 * javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
 * .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING,
 * javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
 * Short.MAX_VALUE) .addComponent(jtfSizeY,
 * javax.swing.GroupLayout.Alignment.LEADING) .addComponent(jtfSizeX,
 * javax.swing.GroupLayout.Alignment.LEADING)) .addContainerGap()))) );
 * 
 * jScrollPane2.setBorder(new javax.swing.border.MatteBorder(null));
 * 
 * jPanel2.setBorder(new javax.swing.border.MatteBorder(null));
 * 
 * javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
 * jPanel2.setLayout(jPanel2Layout); jPanel2Layout.setHorizontalGroup(
 * jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
 * .addGap(0, 0, Short.MAX_VALUE) ); jPanel2Layout.setVerticalGroup(
 * jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
 * .addGap(0, 0, Short.MAX_VALUE) );
 * 
 * javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
 * this.setLayout(layout); layout.setHorizontalGroup(
 * layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
 * .addGroup(layout.createSequentialGroup()
 * .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.
 * TRAILING, false) .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
 * javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
 * .addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 621,
 * Short.MAX_VALUE))
 * .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
 * .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.
 * LEADING) .addGroup(layout.createSequentialGroup() .addGap(0, 0,
 * Short.MAX_VALUE) .addComponent(jScrollPane2,
 * javax.swing.GroupLayout.PREFERRED_SIZE, 766,
 * javax.swing.GroupLayout.PREFERRED_SIZE)) .addComponent(jPanel2,
 * javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
 * Short.MAX_VALUE))) ); layout.setVerticalGroup(
 * layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
 * .addGroup(layout.createSequentialGroup() .addContainerGap()
 * .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.
 * LEADING, false) .addComponent(modificationPanel,
 * javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
 * Short.MAX_VALUE) .addComponent(jScrollPane2))
 * .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
 * .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.
 * LEADING) .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
 * javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) .addComponent(jPanel1,
 * javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
 * Short.MAX_VALUE))) ); }// </editor-fold>
 * 
 * private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
 * ctrl.setSelection((String)jComboBox1.getSelectedItem()); }
 * 
 * private void jtfSizeXActionPerformed(java.awt.event.ActionEvent evt) {
 * ctrl.resize(Integer.valueOf(jtfSizeX.getText()),Integer.valueOf(jtfSizeY.
 * getText())); } private void
 * jtfSizeYActionPerformed(java.awt.event.ActionEvent evt) {
 * ctrl.resize(Integer.valueOf(jtfSizeX.getText()),Integer.valueOf(jtfSizeY.
 * getText())); }
 * 
 * private void jsStrokeSizeStateChanged(javax.swing.event.ChangeEvent evt) {
 * ctrl.setStrokeSize(jsStrokeSize.getValue()); }
 * 
 * 
 * 
 * 
 * // Variables declaration - do not modify private
 * javax.swing.JComboBox<String> jComboBox1; private javax.swing.JLabel jLabel1;
 * private javax.swing.JLabel jLabel2; private javax.swing.JLabel jLabel3;
 * private javax.swing.JPanel jPanel1; private javax.swing.JPanel jPanel2;
 * private javax.swing.JScrollPane modificationPanel; private
 * javax.swing.JScrollPane jScrollPane2; private javax.swing.JSlider
 * jsStrokeSize; private javax.swing.JTextField jtfSizeX; private
 * javax.swing.JTextField jtfSizeY; // End of variables declaration
 * 
 * }
 */