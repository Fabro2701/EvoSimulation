package setup.gui.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;

public class ViewPanel extends JPanel{
	List<State>states;
	SetupEditorController ctrl;
	
	public ViewPanel(SetupEditorController ctrl) {
		super();
		this.states = new ArrayList<>();
		this.ctrl = ctrl;
		initView();
		this.repaint();
	}
	private void initView() {
		Point2D.Float root = new Point2D.Float(20f,20f);
		
		float i=0f;
		Map<Class<?>, EntitySeparator> seps = this.ctrl.getSeparators();
		for(Entry<Class<?>, EntitySeparator> entry:seps.entrySet()) {
			String name = entry.getKey().getSimpleName();
			State state = State.from("class", name, new RectangleShape(root.x, root.y+i*100f, 50f, 50f));
			this.states.add(state);
			float j=1f;
			for(String v:entry.getValue().getValues()) {
				State vstate = State.from(entry.getValue().getAtt(), 
										  v, 
										  new CircleShape(root.x+j*75f, 
												  		  root.y+i*100f, 
												  		  25f)
										  );
				this.states.add(vstate);
				j++;
			}
			i++;
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		g2.setColor(Color.black);
		for(State state:this.states) {
			state.paint(g2);
		}
	}
	private static class State{
		String clazz;
		String name;
		Shape shape;
		public static State from(String clazz, String name, Shape shape) {
			State s = new State();
			s.clazz = clazz;
			s.name = name;
			s.shape = shape;
			return s;
		}
		public void paint(Graphics2D g2) {
			shape.paint(g2);
			Point2D.Float c = shape.center();
			int width = g2.getFontMetrics().stringWidth(name);
			int height = g2.getFontMetrics().getHeight();
			g2.drawString(name, c.x-width/2f, c.y+height/2f);
		}
	}
	
	
	
	
	private abstract static class Shape{
		protected float x,y;
		protected Shape(float x, float y) {
			this.x = x;
			this.y = y;
		}
		public abstract boolean isInside(Point2D p);
		public abstract void paint(Graphics2D g2);
		public abstract Point2D.Float center();
	}
	private static class CircleShape extends Shape{
		private float r;
		public CircleShape(float x, float y, float r) {
			super(x, y);
			this.r = r;
		}
		@Override
		public boolean isInside(Point2D p) {
			double d = Math.sqrt(Math.pow(p.getX()-(x+r), 2)+Math.pow(p.getY()-(y+r), 2));
			return d<=r;
		}
		@Override
		public void paint(Graphics2D g2) {
			g2.drawOval((int)x, (int)y, (int)r*2, (int)r*2);
		}
		@Override
		public Point2D.Float center() {
			return new Point2D.Float(x+r, y+r);
		}
	}
	private static class RectangleShape extends Shape{
		private float w,h;
		public RectangleShape(float x, float y, float w, float h) {
			super(x, y);
			this.w = w;
			this.h = h;
		}
		@Override
		public boolean isInside(Point2D p) {
			return (p.getX()>=x&&p.getX()<=x+w) && (p.getY()>=y&&p.getY()<=y+h);
		}
		@Override
		public void paint(Graphics2D g2) {
			g2.drawLine((int)x, (int)y, (int)(x+w), (int)y);
			g2.drawLine((int)(x+w), (int)y, (int)(x+w), (int)(y+h));
			g2.drawLine((int)(x+w), (int)(y+h), (int)x, (int)(y+h));
			g2.drawLine((int)x, (int)(y+h), (int)x, (int)y);
		}
		@Override
		public Point2D.Float center() {
			return new Point2D.Float(x+w/2f, y+h/2f);
		}
	}
}
