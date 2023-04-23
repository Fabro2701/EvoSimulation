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


/**
 * EmptyViewer 
 * @author fabrizioortega
 *
 */
public class EmptyViewer extends AbstractViewer {

	public EmptyViewer(Controller ctrl) {
		super(ctrl,1,1);
	}

	@Override
	public void onRegister(List<Entity> entities, Map map, int time) { 

		
	}
	

	@Override
	public void onUpdate(List<Entity> entities, Map map, int time, java.util.Map<Object, ViewElement> viewElements) {
		
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	
	
}
