package state_diagram.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import javax.swing.JMenuItem;

import org.json.JSONObject;

import state_diagram.Constants;
import state_diagram.Diagram;
import state_diagram.Util;
import state_diagram.elements.properties.InitStateProperties;
import state_diagram.elements.properties.SimpleStateProperties;

public class EndState extends TransitionableElement {
	static int shadowMargin = Constants.SHADOW_MARGIN;
	static int w=Constants.INIT_STATE_W,h=Constants.INIT_STATE_H;
	
	public EndState(Diagram diagram, Point base, Point pos) {
		super(diagram, base, pos);
	}
	public EndState(Diagram diagram, JSONObject ob, List<Element> es) {
		super(diagram, ob, es);
	}
	
	@Override
	public void paint(Graphics2D g2) {
		super.paint(g2);
		g2.setColor(Constants.INIT_COLOR);
		g2.fillOval(base.x+pos.x-w, base.y+pos.y-h, w, h);
		g2.setColor(Color.white);
		g2.fillOval(base.x+pos.x-w+3, base.y+pos.y-h+3, w-6, h-6);
		g2.setColor(Constants.INIT_COLOR);
		g2.fillOval(base.x+pos.x-w+5, base.y+pos.y-h+5, w-10, h-10);
		/*g2.setColor(Constants.SIMPLE_STATE_COLOR);
		g2.drawOval(base.x+pos.x-w, base.y+pos.y-h, w, h);*/
		if(father!=null) {
			g2.setColor(Color.gray);
			g2.drawString(String.valueOf(father.ID), base.x+pos.x-w, base.y+pos.y-h);
		}
		
	}
	@Override
	public void properties() {
		/*if(properties==null)properties = new InitStateProperties(diagram.getProps(),this);
		properties.load();*/
	}
	@Override
	protected void paintShadow(Graphics2D g2) {
		g2.setColor(Constants.SHADOW_COLOR);
		g2.fillOval(base.x+pos.x-w-shadowMargin, base.y+pos.y-h-shadowMargin, w+shadowMargin*2, h+shadowMargin*2);
	}

	@Override
	public TransitionableElement contains(Point p) {
		double centerx = base.x+pos.x-w/2;
		double centery = base.y+pos.y-h/2;
		double dx = Math.abs(p.x - centerx);
        double dy = Math.abs(p.y - centery);
        
        if( (Math.pow(dx, 2)/Math.pow(w/2, 2))+(Math.pow(dy, 2)/Math.pow(h/2, 2))<=1d)return this;
        return null;
	}

	@Override
	public TransitionableElement containsShadow(Point p) {
		TransitionableElement aux = null;
		if((aux=contains(p))!=null)return aux;
		double centerx = base.x+pos.x-w/2;
		double centery = base.y+pos.y-h/2;
		double dx = Math.abs(p.x - centerx);
        double dy = Math.abs(p.y - centery);
        
        if((Math.pow(dx, 2)/Math.pow(w/2+shadowMargin, 2))+(Math.pow(dy, 2)/Math.pow(h/2+shadowMargin, 2))<=1d)return this;
        return null;
	}

	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("class", "EndState")
							   .put("ID", ID)
							   .put("pos", new JSONObject().put("x", pos.x).put("y", pos.y))
							   .put("fatherID", father!=null?father.ID:null);
	}


}
