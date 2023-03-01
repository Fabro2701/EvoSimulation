package setup.gui.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.view.module.SeparatorModule;

public class ViewPanel extends JPanel{
	JFrame father;
	List<State>states;
	SetupEditorController ctrl;
	CustomMouseAdapter mouse;
	
	public ViewPanel(JFrame father, SetupEditorController ctrl) {
		super();
		this.states = new ArrayList<>();
		this.ctrl = ctrl;
		this.father = father;
		initView();
		
		mouse = new CustomMouseAdapter();
		this.addMouseListener(mouse);
		
		this.repaint();
	}
	private void initView() {
		setStates();
	}
	private void setStates() {
		this.states = new ArrayList<>();
		Point2D.Float root = new Point2D.Float(20f,20f);
		
		float i=0f;
		Map<Class<?>, EntitySeparator> seps = this.ctrl.getSeparators();
		for(Entry<Class<?>, EntitySeparator> entry:seps.entrySet()) {
			Class<?> k = entry.getKey();
			State state = State.from("class", 
									 k, 
									 k.getSimpleName(),
									 new RectangleShape(root.x, 
														root.y+i*100f, 
														50f, 
														50f)
									 );
			this.states.add(state);
			float j=1f;
			for(String v:entry.getValue().getValues()) {
				State vstate = State.from(entry.getValue().getAtt(), 
										  v,
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
		
		this.setStates();

		g2.setColor(Color.black);
		for(State state:this.states) {
			state.paint(g2);
		}
	}
	public static class State{
		String clazz;
		String name;
		Shape shape;
		Object id;
		boolean selected = false;
		Map<String, Object>attributes;
		public static State from(String clazz, Object id, String name, Shape shape) {
			State s = new State();
			s.clazz = clazz;
			s.id = id;
			s.name = name;
			s.shape = shape;
			s.attributes = new HashMap<>();
			return s;
		}
		public void paint(Graphics2D g2) {
			if(selected)g2.setColor(Color.red);
			else g2.setColor(Color.black);
			shape.paint(g2);
			Point2D.Float c = shape.center();
			int width = g2.getFontMetrics().stringWidth(name);
			int height = g2.getFontMetrics().getHeight();
			g2.drawString(name, c.x-width/2f, c.y+height/2f);
		}
		public boolean isInside(Point p) {
			return shape.isInside(p);
		}
		@Override 
		public String toString() {
			return clazz+" "+name;
		}
		public String getClazz() {
			return clazz;
		}
		public String getName() {
			return name;
		}
		public Object getId() {
			return id;
		}
		public Object getAttribute(String k) {
			return this.attributes.get(k);
		}
	}
	private class CustomMouseAdapter extends MouseAdapter{
		State selection;
		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			if(selection==null) {
				select(p);
			}
			else {
				selection.selected = false;
				if(!selection.isInside(p))select(p);
			}
			if(selection!=null) {
				open(p);
			}
			ViewPanel.this.repaint();
		}
		private boolean select(Point p) {
			for(State state:states) {
				if(state.isInside(p)) {
					selection = state;
					selection.selected = true;
					return true;
				}
			}
			return false;
		}
		
		JMenuItem separatorMenu;
		JMenuItem initMenu;
		JMenuItem interactionMenu;
		
		SeparatorModule separatorModule;
		public CustomMouseAdapter() {
			separatorMenu = new JMenuItem("Separator");
			separatorMenu.addActionListener((ActionEvent e)->accessSeparator(this.selection));
			initMenu = new JMenuItem("Init");
			interactionMenu = new JMenuItem("Interaction");
			
			separatorModule = new SeparatorModule(ViewPanel.this.father, ViewPanel.this.ctrl);
		}
		
		private void open(Point p) {
			JPopupMenu pm = new JPopupMenu();
			
			if(this.selection.clazz.equals("class"))pm.add(separatorMenu);
			pm.add(initMenu);
			pm.add(interactionMenu);
			
			pm.show(ViewPanel.this, p.x, p.y);
		}
		public Object accessSeparator(State state) {//create one class for each access
			separatorModule.open(state);
			
			
			ViewPanel.this.repaint();
			return null;
		}
	}

	
	
	
	private abstract static class Shape{
		protected float x,y;
		
		protected Shape(float x, float y) {
			this.x = x;
			this.y = y;
		}
		public abstract boolean isInside(Point p);
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
		public boolean isInside(Point p) {
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
		public boolean isInside(Point p) {
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
