package state_diagram.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import org.json.JSONObject;

import state_diagram.Constants;
import state_diagram.Diagram;
import state_diagram.Util;
import state_diagram.elements.properties.TransitionProperties;

public class Transition extends Element{
	TransitionableElement from,to;
	Point fromShift,toShift;
	
	public enum TRANSITION_TYPE{TRUE,COND,STOCHASTIC}
	TRANSITION_TYPE type = TRANSITION_TYPE.TRUE;
	String code="";
	
	public Transition(Diagram diagram, TransitionableElement from, Point fromShift, TransitionableElement to) {
		super(diagram);
		this.from = from;
		this.fromShift = fromShift;
		this.to = to;
	}
	public Transition(Diagram diagram, TransitionableElement father, Point shift) {
		this(diagram, father, shift, null);
	}
	public Transition(Diagram diagram, JSONObject ob, List<Element> es) {
		super(diagram, false);
		this.ID = ob.getInt("ID");
		IdGenerator.update(ID);
		this.from = (TransitionableElement) es.stream().filter(e->e instanceof TransitionableElement && ((TransitionableElement)e).ID==ob.getInt("fromID")).findFirst().get();
		this.to = (TransitionableElement) es.stream().filter(e->e instanceof TransitionableElement && ((TransitionableElement)e).ID==ob.getInt("toID")).findFirst().get();
		this.fromShift = Util.pointFromJSON(ob.getJSONObject("fromShift"));
		this.toShift = Util.pointFromJSON(ob.getJSONObject("toShift"));
		this.code = ob.getString("code");
		this.type = ob.getEnum(TRANSITION_TYPE.class, "type");
	}
	@Override
	public void properties() {
		if(properties==null)properties = new TransitionProperties(diagram.getProps(),this);
		properties.load();
	}
	@Override
	public void paint(Graphics2D g2) {
		g2.setColor(Color.black);
		Point fromp = new Point(fromShift.x + from.base.x + from.pos.x,
								fromShift.y + from.base.y + from.pos.y);
		Point top = new Point(toShift.x + to.base.x + to.pos.x,
							  toShift.y + to.base.y + to.pos.y);
		
		

		g2.drawLine(fromp.x, fromp.y, top.x, top.y);
		
		Point mid = new Point(top.x-fromp.x, top.y-fromp.y);
		g2.drawString(String.valueOf(ID), fromp.x+mid.x/2, fromp.y+mid.y/2);
		
		if(to instanceof Corner)return;
		drawArrow(g2, fromp.x, fromp.y, top.x, top.y,Constants.ARROW_W,Constants.ARROW_H);
	}
	public void validate() {
		from.addFromTransition(this);
		to.addToTransition(this);
	}
	@Override
	public TransitionableElement contains(Point p) {return null;}
	
	public boolean containsLine(Point p) {
		Point fromp = new Point(fromShift.x + from.base.x + from.pos.x,
				fromShift.y + from.base.y + from.pos.y);
		Point top = new Point(toShift.x + to.base.x + to.pos.x,
			  toShift.y + to.base.y + to.pos.y);
		
       
        double k = 1.5;
        double d = Util.distance(p, fromp, top);

		return (d <= k ? true:false);
	}
	public void setDest(TransitionableElement to) {
		this.to = to;
	}
	public void setToShift(Point toShift) {
		this.toShift = toShift;
	}
	private void drawArrow(//
									Graphics2D g2, //
									int x1, int y1, //
									int x2, int y2, //
									int w, int h) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;
		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;
		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;
		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };
		//g2.drawLine(x1, y1, x2, y2);
		g2.fillPolygon(xpoints, ypoints, 3);
	}
	public TransitionableElement getFrom() {
		return from;
	}
	public TransitionableElement getTo() {
		return to;
	}
	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("class", "Transition")
							   .put("ID", ID)
							   .put("fromID", from!=null?from.ID:null)
							   .put("toID", to!=null?to.ID:null)
							   .put("fromShift", new JSONObject().put("x", fromShift.x).put("y", fromShift.y))
							   .put("toShift", new JSONObject().put("x", toShift.x).put("y", toShift.y))
							   .put("type", type)
							   .put("code", code);
	}
	public TRANSITION_TYPE getType() {
		return type;
	}
	public void setType(TRANSITION_TYPE type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
