package simulator.view.viewer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

public class Viewer extends JPanel implements SimulatorObserver{
	
	private BufferedImage mapImage;
	
	public Viewer(Controller ctrl) {
	      //initGUI();
	      ctrl.addObserver(this);
	      repaint();
	}
	@Override
	public void onRegister(List<Entity> entities, Map map, int time) {
		mapImage=map.getImage();
		repaint();
	}

	@Override
	public void onUpdate(List<Entity> entities, int time) {
		// TODO Auto-generated method stub
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
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
	}

}
