package setup.diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.json.JSONObject;


import setup.diagram.CodePanel;
import setup.diagram.elements.Element.Shape;
import setup.diagram.elements.EntityElement.EntityShape;

public class PendingElement extends Element {
	Element father;
	BiConsumer<Element,Element>f;
	Predicate<Element>filter;
	Point connection;

	public PendingElement(Point point) {
		super(point);
		this.shape = new PendingShape(7, 7);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	@Override
	public String fileName() {
		return "pending";
	}
	public void connect(Element e) {
		//if(filter.test(e))
		f.accept(father, e);
		//this.setConnection(e.getPos());
	}



	@Override
	public void draw(Graphics2D g2) {
		this.shape.draw(g2);
	}
	public void setConnection(Point point) {
		connection = point;
	}



	protected class PendingShape extends Shape{
		int w,h;
		protected PendingShape(int w, int h){
			this.w = w;
			this.h = h;
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.drawOval(pos.x-w/2, pos.y-h/2, w, h);
		}

		@Override
		public boolean contains(Point point) {
			// square simplification
			return (point.x>=pos.x-w/2&&point.x<=pos.x+w/2)&&(point.y>=pos.y-h/2&&point.y<=pos.y+h/2);		
		}

		@Override
		public Point leftPoint() {
			return new Point(pos.x-w,pos.y);
		}

		@Override
		public Point rightPoint() {
			return new Point(pos.x+w,pos.y);
		}
		@Override
		public void update() {
			// TODO Auto-generated method stub
			
		}

		
	}
	@Override
	public Object clone() {
		PendingElement e = new PendingElement(pos);
		e.setF(f);
		e.setConnection(connection);
		e.setFather(father);
		e.setPos(new Point(pos));
		e.setDiagram(diagram);
		return e;
	}

	public void setF(BiConsumer<Element, Element> f) {
		this.f = f;
	}

	public Point getConnection() {
		return connection;
	}

	public Element getFather() {
		return father;
	}

	public void setFather(Element father) {
		this.father = father;
	}

	public Predicate<Element> getFilter() {
		return filter;
	}

	public void setFilter(Predicate<Element> filter) {
		this.filter = filter;
	}

	@Override
	protected void properties() {
		// TODO Auto-generated method stub
		
	}

}
