package simulator.view.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import util.Util;

public class Viewer extends JPanel implements SimulatorObserver{
	
	private BufferedImage mapImage;
	private BufferedImage entitiesImage;
	public Viewer(Controller ctrl) {
	      //initGUI();
	      ctrl.addObserver(this);
	      repaint();
	}
	@Override
	public void onRegister(List<Entity> entities, Map map, int time) {
		mapImage = Util.copyImage(map.getImage());
		repaint();
	}

	@Override
	public void onUpdate(List<Entity> entities, Map map, int time) {
		mapImage = Util.copyImage(map.getImage());
		setEntitiesImage(entities);
		repaint();
	}
	
	private void setEntitiesImage(List<Entity> entities) {
		/*BufferedImage img = new BufferedImage(500, 500,BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = (Graphics2D) mapImage.getGraphics();
		for(Entity e:entities) {
			gr.drawImage(e.getImage(), e.getX(), e.getY(), null);
		}*/
		/*Graphics2D gr = (Graphics2D) mapImage.getGraphics();
		for(Entity e:entities) {
			gr.setColor(Color.blue);
			gr.fill(e.getImage());
		}*/
		Graphics2D gr = (Graphics2D) mapImage.getGraphics();
		for(Entity e:entities) {
			gr.drawImage(e.getImage(), e.getX(), e.getY(), null);
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//ImageIcon?
		Graphics2D gr = (Graphics2D) g;
		gr.drawImage(mapImage, 0, 0, 500, 500, 0, 0, 500, 500, null);
							 //this            mapimage
		int gridSize=20;
		for(int i=0;i<this.getHeight();i+=gridSize) {
			for(int j=0;j<this.getWidth();j+=gridSize) {
				gr.drawRect(i, j, gridSize, gridSize);
			}
		}
		
		//gr.drawImage(mapImage,100,100,null);
		//gr.drawImage(entitiesImage, 0, 0, 500, 500, 0, 0, 500, 500, null);
	}

}
