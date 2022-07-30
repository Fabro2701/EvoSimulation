package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
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

import simulator.model.map.creator.MapCreator.PanelManager;

/**
 * AbstractCreatorPanel incorporates common behaviours of the components of
 * MapCreator, like get and set of bufferImage; the Controller,
 * ModificationImage and ViewImage each with its common functionality
 * 
 * @author fabrizioortega
 *
 */
public abstract class AbstractCreatorPanel extends JPanel {
	private static final long serialVersionUID = 2L;
	protected PanelManager panelManager;
	protected Controller ctrl;

	public AbstractCreatorPanel(PanelManager panelManager) {
		initComponents();
		this.panelManager=panelManager;
	}

	public BufferedImage getBufferImage() {
		return ctrl.bufferImage;
	}
	public void resizeCanvas(int w,int h) {
		ctrl.resize(w,h);
	}

	/**
	 * Set a new bufferImage and update what is necessary
	 * 
	 * @param inImg
	 */
	public void setImage(BufferedImage inImg) {
		ctrl.bufferImage = inImg;
		ctrl.bufferGraphics = ctrl.bufferImage.createGraphics();
		ctrl.img.setIcon(new ImageIcon(ctrl.bufferImage));
		if (ctrl.viewObserver != null)
			ctrl.viewObserver.update(ctrl.bufferImage);
		repaint();
	}

	/**
	 * Controller manages the events on ModificationPanel
	 * 
	 * @author fabrizioortega
	 *
	 */
	protected abstract class Controller implements MouseListener, MouseMotionListener {
		protected Graphics2D bufferGraphics;
		protected BufferedImage bufferImage;
		protected int width, height;
		protected ModificationImage img;
		protected Point current;
		public ViewImage viewObserver;

		/**
		 * 
		 * @param width
		 * @param height
		 * @param initColor initial color for the ModificationPanel
		 */
		public Controller(int width, int height, Color initColor) {
			this.width = width;
			this.height = height;

			current = new Point();

			bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			bufferGraphics = bufferImage.createGraphics();
			bufferGraphics.setColor(initColor);
			bufferGraphics.fillRect(0, 0, width, height);

		}
		/**
		 * Resize the current canvas 
		 * 
		 * @param w
		 * @param h
		 */
		public void resize(int w, int h) {
			Image tmp = bufferImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);

			width = w;
			height = h;
			bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			bufferGraphics = bufferImage.createGraphics();
			bufferGraphics.drawImage(tmp, 0, 0, null);

			img.setIcon(new ImageIcon(bufferImage));
			
		}

		protected class ModificationImage extends JLabel implements Scrollable {
			private static final long serialVersionUID = 2L;

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
			private static final long serialVersionUID = 2L;

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

		/**
		 * Each creator component has its own way to modify ModificationPanel
		 */
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

	/**
	 * init GUI components
	 */
	public abstract void initComponents();

	/**
	 * Modification and view panel
	 */
	protected javax.swing.JScrollPane modificationPanel;
	protected javax.swing.JScrollPane viewPanel;
}
