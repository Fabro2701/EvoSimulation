package simulator.view.viewer;

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

import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

public class Viewer extends AbstractViewer {

	public Viewer(Controller ctrl) {
		super(ctrl,500,500);
		// initGUI();

		//addEntityViewers();
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

	@Override
	public void onUpdate(List<Entity> entities, Map map, int time) {
		if(active) {
			this.mapImg = map.getAttributesImage();
			this.entities = entities;
			// setEntitiesImage(entities);
			updateImage();
			repaint();
		}
	}

	public void updateImage() {
		
		bufferGraphics.drawImage(mapImg, 0, 0, null);
		int gridSize = 20;
		for (int i = 0; i < this.getHeight(); i += gridSize) {
			for (int j = 0; j < this.getWidth(); j += gridSize) {
				bufferGraphics.drawRect(i, j, gridSize, gridSize);
			}
		}
		for (Entity e : entities) {
			bufferGraphics.drawImage(e.getImage(), e.node.x, e.node.y, null);
		}
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// ImageIcon?
/*
		bufferGraphics.drawImage(mapImage, 0, 0, this.getWidth(), this.getHeight(), 0, 0, mapImage.getWidth(),
				mapImage.getHeight(), null);

		int gridSize = 20;
		for (int i = 0; i < this.getHeight(); i += gridSize) {
			for (int j = 0; j < this.getWidth(); j += gridSize) {
				bufferGraphics.drawRect(i, j, gridSize, gridSize);
			}
		}

		for (Entity e : entities) {
			bufferGraphics.drawImage(e.getImage(), e.node.x, e.node.y, null);
		}

		g.drawImage(bufferImage, 0, 0, null);*/
		// gr.setColor(Color.red);
		// gr.fillOval(0, 0, 20, 20);
		// gr.drawImage(mapImage,100,100,null);
		// gr.drawImage(entitiesImage, 0, 0, 500, 500, 0, 0, 500, 500, null);
	}

	private void addEntityViewers() {
		JFrame father = (JFrame) SwingUtilities.getWindowAncestor(this);
		MouseListener mouse = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent me) {
				int selectionRatio = 10;
				Point point = me.getPoint();
				// System.out.println(point);
				for (Entity e : entities) {
					// System.out.println("ent "+e.node.x+" "+e.node.y);
					if (Math.abs(e.node.x - point.x) + Math.abs(e.node.x - point.x) <= selectionRatio) {
						openEntityDialog(e);

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

	private void openEntityDialog(Entity e) {
		e.openDialog(this);

	}
}
