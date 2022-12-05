package simulator.view.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.w3c.dom.Node;

import simulator.Constants.NODE_TYPE;
import simulator.control.Controller;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.map.Map;


/**
 * Viewer normal 2d visualization 
 * @author fabrizioortega
 *
 */
public class Viewer extends AbstractViewer {

	public Viewer(Controller ctrl, int w, int h) {
		super(ctrl,w,h);
	}
	public Viewer(Controller ctrl) {
		this(ctrl,1000,1000);
		// initGUI();

		addEntityViewers();
		//repaint();
	}

	@Override
	public void onRegister(List<Entity> entities, Map map, int time) { 

		this.mapImg = map.getAttributesImage();
		this.entities = entities;
		// setEntitiesImage(entities);
		updateImage();
		repaint();
		
	}
	
	/**
	 * Update only if it is active to optimize
	 */
	@Override
	public void onUpdate(List<Entity> entities, Map map, int time) {
		if(active) {
			this.mapImg = map.getAttributesImage();
			this.entities = entities;
			updateImage();
			repaint();
		}
	}

	public void updateImage() {
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, this.getHeight(), this.getWidth());
		//bufferGraphics.drawImage(mapImg, 0, 0, null);//attributes
//		Map map = ctrl.getMap();
//		for (int i = 0; i < mapImg.getHeight(null); i ++) {
//			for (int j = 0; j < mapImg.getWidth(null); j ++) {
//				if(map.getNodeAt(j, i).type == NODE_TYPE.LAND) {
//					
//				}
//			}
//		}
		bufferGraphics.setColor(Color.black);
		int gridSize = 20;
		for (int i = 0; i < this.getHeight(); i += gridSize) {
			for (int j = 0; j < this.getWidth(); j += gridSize) {
				bufferGraphics.drawRect(i, j, gridSize, gridSize);
			}
		}
		for (Entity e : entities) {
			bufferGraphics.drawImage(e.getImage(), e.node.x-e.getImage().getWidth(null)/2,
												   e.node.y-e.getImage().getHeight(null)/2, null);
		}
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public void addEntityViewers() {
		JFrame father = (JFrame) SwingUtilities.getWindowAncestor(this);
		MouseListener mouse = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent me) {
				int selectionRatio = 10;
				Point point = me.getPoint();
				// System.out.println(point);
				for (Entity e : entities) {
					// System.out.println("ent "+e.node.x+" "+e.node.y);
					
					if (Math.sqrt(Math.pow(e.node.x - point.x, 2)+Math.pow(e.node.y - point.y, 2)) <= selectionRatio) {
						//openEntityDialog(e);
						if(e instanceof ActiveEntity)Viewer.this.entityViewer.setEntity(e);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		};
		this.addMouseListener(mouse);
	}


}
