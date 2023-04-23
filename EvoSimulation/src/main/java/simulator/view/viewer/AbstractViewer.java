package simulator.view.viewer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Scrollable;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.view.EntityViewer;

/**
 * AbstractViewer class is used to switch between View models in the simulation
 * @author fabrizioortega
 *
 */
public abstract class AbstractViewer extends JLabel implements SimulatorObserver, Scrollable{
	
	//two variables that get updated as the simulation runs
	protected List<Entity> entities;
	protected Image mapImg;
	protected EntityViewer entityViewer;
	
	protected BufferedImage bufferImage;
	protected Graphics2D bufferGraphics;
	protected boolean active = false;
	Controller ctrl;
	
	
	public AbstractViewer(Controller ctrl, int width, int height) {
		this.ctrl = ctrl;
		bufferImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		bufferGraphics = bufferImage.createGraphics();
		ctrl.addObserver(this);
		
		active=false;
		this.setIcon(new ImageIcon(bufferImage));
		
	}
	/**
	 * Activate the viewer
	 */
	public void activate() {
		active=true;
	}
	/**
	 * Deactivate the viewer
	 */
	public void deactivate() {
		active=false;
	}
	public Image getImage() {return bufferImage;}
	public void addEntityObserver(EntityViewer entityViewer) {
		this.entityViewer = entityViewer;
	}
	@Override
	public abstract void onRegister(List<Entity> entities, Map map, int time);
	@Override
	public abstract void onUpdate(List<Entity> entities, Map map, int time, java.util.Map<Object, ViewElement> viewElements);
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
