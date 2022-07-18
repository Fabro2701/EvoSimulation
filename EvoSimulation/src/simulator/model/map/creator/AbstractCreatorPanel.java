package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;

public abstract class AbstractCreatorPanel extends JPanel {
	protected Controller ctrl;

	public AbstractCreatorPanel() {
		initComponents();
	}

	public BufferedImage getBufferImage() {
		return ctrl.bufferImage;
	}

	public void setImage(BufferedImage inImg) {
		ctrl.bufferImage = inImg;
		ctrl.bufferGraphics = ctrl.bufferImage.createGraphics();
		ctrl.img.setIcon(new ImageIcon(ctrl.bufferImage));
		if (ctrl.viewObserver != null)
			ctrl.viewObserver.update(ctrl.bufferImage);
		repaint();
	}

	protected abstract class Controller implements MouseListener, MouseMotionListener {
		protected Graphics2D bufferGraphics;
		protected BufferedImage bufferImage;
		protected int width, height;
		protected ModificationImage img;
		protected Point current;
		public ViewImage viewObserver;

		public Controller(int width, int height, Color initColor) {
			this.width = width;
			this.height = height;

			current = new Point();

			bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			bufferGraphics = bufferImage.createGraphics();
			bufferGraphics.setColor(initColor);
			bufferGraphics.fillRect(0, 0, width, height);

		}

		protected class ModificationImage extends JLabel implements Scrollable {
			public ModificationImage(ImageIcon icon) {
				super(icon);
				this.addMouseListener(Controller.this);
				this.addMouseMotionListener(Controller.this);
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

		protected abstract class ViewImage extends JLabel implements Scrollable {
			public abstract void update(BufferedImage image);

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public boolean getScrollableTracksViewportWidth() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean getScrollableTracksViewportHeight() {
				// TODO Auto-generated method stub
				return false;
			}

		}

		@Override
		public abstract void mouseDragged(MouseEvent e);

		@Override
		public abstract void mousePressed(MouseEvent e);

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	public abstract void initComponents();

	protected javax.swing.JScrollPane modificationPanel;
	protected javax.swing.JScrollPane viewPanel;
}
