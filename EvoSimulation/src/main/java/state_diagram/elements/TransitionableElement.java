package state_diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import state_diagram.Diagram;
import state_diagram.Util;

public abstract class TransitionableElement extends Element{
	protected Point over;
	protected Point base,pos;
	protected List<Transition>fromTs;
	protected List<Transition>toTs;
	CompoundState father;
	public TransitionableElement(Diagram diagram, Point base, Point pos) {
		super(diagram);
		this.base = base;
		this.pos = pos;
		this.over = null;
		this.fromTs = new ArrayList<>();
		this.toTs = new ArrayList<>();
	}
	public TransitionableElement(Diagram diagram, JSONObject ob, List<Element> es) {
		super(diagram,false);
		this.ID = ob.getInt("ID");
		IdGenerator.update(ID);
		this.base = diagram.getBase();
		this.pos = Util.pointFromJSON(ob.getJSONObject("pos"));
		if(ob.has("fatherID")) {
			CompoundState f = (CompoundState) es.stream().filter(e->e instanceof CompoundState && ((CompoundState)e).ID==ob.getInt("fatherID")).findFirst().get();
			f.insertChild(this);
		}
		this.over = null;
		this.fromTs = new ArrayList<>();
		this.toTs = new ArrayList<>();
	}
	public void paint(Graphics2D g2) {
		if(this.over!=null) {
			this.paintShadow(g2);
		}
	}
	protected abstract void paintShadow(Graphics2D g2);
	public abstract TransitionableElement containsShadow(Point p);
	public Point getOver() {
		return over;
	}
	public String id() {
		return this.getClass().getSimpleName();
	}
	@Override
	public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    protected void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.id()+"("+ID+")");
        buffer.append('\n');
    }
	public void setOver(Point over) {
		this.over = over;
	}
	public void move(int x, int y) {
		pos.x += x;
		pos.y += y;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public void addFromTransition(Transition t) {
		fromTs.add(t);
	}
	public void addToTransition(Transition t) {
		toTs.add(t);
	}
	public Point getRelativePosition(Point p) {
		return new Point(p.x-(this.base.x + this.pos.x), p.y-(this.base.y + this.pos.y));
	}
	public Point getBase() {
		return base;
	}
	public void setBase(Point base) {
		this.base = base;
	}
	public CompoundState getFather() {
		return father;
	}
	public void setFather(CompoundState father) {
		this.father = father;
	}
	public List<Transition> getFromTs() {
		return fromTs;
	}
	public List<Transition> getToTs() {
		return toTs;
	}
}
