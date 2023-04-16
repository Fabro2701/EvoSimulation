package state_diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPopupMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import state_diagram.Diagram;
import state_diagram.elements.properties.ElementProperties;



public abstract class Element {
	public int ID;
	protected JPopupMenu pm;
	protected Diagram diagram;
	protected ElementProperties properties;
	public Element(Diagram diagram, boolean newid) {
		this.diagram = diagram;
		this.pm = new JPopupMenu();
		if(newid)this.ID = IdGenerator.nextId();
	}
	public Element(Diagram diagram) {
		this(diagram, true);
	}
	
	public abstract void paint(Graphics2D g2);

	public void openMenu(Point p) {
		pm.show(diagram,p.x,p.y);
	}
	public abstract void properties();
	public abstract TransitionableElement contains(Point p);
	
	public abstract JSONObject toJSON();
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
	public Diagram getDiagram() {
		return diagram;
	}
}
