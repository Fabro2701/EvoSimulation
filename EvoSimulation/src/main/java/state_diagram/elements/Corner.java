package state_diagram.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import state_diagram.Constants;
import state_diagram.Diagram;
import state_diagram.Util;
import state_diagram.elements.properties.CornerProperties;
import state_diagram.elements.properties.SimpleStateProperties;

public class Corner extends TransitionableElement {
	static int shadowMargin = Constants.SHADOW_MARGIN;
	int w=Constants.CORNER_W,h=Constants.CORNER_H;
	public Corner(Diagram diagram, Point base, Point pos) {
		super(diagram, base, pos);
	}
	public Corner(Diagram diagram, JSONObject ob, List<Element> es) {
		super(diagram, ob, es);
	}
	
	@Override
	public void paint(Graphics2D g2) {
		super.paint(g2);
		
		g2.setColor(Constants.CORNER_COLOR);
		g2.fillRect(base.x+pos.x-w, base.y+pos.y-h, w, h);
		
	}

	@Override
	protected void paintShadow(Graphics2D g2) {
		g2.setColor(Constants.SHADOW_COLOR);
		g2.fillRect(base.x+pos.x-w-shadowMargin, base.y+pos.y-h-shadowMargin, w+2*shadowMargin, h+2*shadowMargin);
		
	}
	@Override
	public void properties() {
		if(properties==null)properties = new CornerProperties(diagram.getProps(),this);
		properties.load();
	}

	@Override
	public TransitionableElement contains(Point p) {
		if((p.x>=base.x+pos.x-w&&p.x<=base.x+pos.x)&&
			   (p.y>=base.y+pos.y-h&&p.y<=base.y+pos.y))return this;
		return null;
	}

	@Override
	public TransitionableElement containsShadow(Point p) {
		TransitionableElement aux = null;
		if((aux=contains(p))!=null)return aux;
		if((p.x>=base.x+pos.x-w-shadowMargin&&p.x<=base.x+pos.x+shadowMargin)&&
			   (p.y>=base.y+pos.y-h-shadowMargin&&p.y<=base.y+pos.y+shadowMargin))return this;
		return null;
	}

	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("class", "Corner")
							   .put("ID", ID)
							   .put("pos", new JSONObject().put("x", pos.x).put("y", pos.y))
							   .put("fatherID", father!=null?father.ID:null);
	}


}
