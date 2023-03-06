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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.view.module.FSMModule;
import setup.gui.view.module.InteractionModule;
import setup.gui.view.module.SeparatorModule;

public class ViewPanel extends JPanel{
	JFrame father;
	List<State>states;//change to less abstract and more organised dstruct
	List<Edge>edges;
	SetupEditorController ctrl;
	CustomMouseAdapter mouse;
	boolean recalculate = false;
	State fsms;
	
	public ViewPanel(JFrame father, SetupEditorController ctrl) {
		super();
		this.states = new ArrayList<>();
		edges = new ArrayList<>();
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
		this.states.clear();
		Point2D.Float root = new Point2D.Float(20f,20f);
		
		fsms = State.from("fsms", null, "FSMs", new CircleShape(root.x,root.y,30f));
		this.states.add(fsms);
		
		float i=1f;
		Map<Class<?>, EntitySeparator> seps = this.ctrl.pullSeparators();
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
				State vstate = State.from("separator", 
										  entry.getValue().getAtt(),
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
	private void checkforModifications() {
		//separator
		Set<State>del = new HashSet<>();
		Map<Class<?>, EntitySeparator> seps = this.ctrl.pullSeparators();
		for(int i=0;i<this.states.size();i++) {
			State state = this.states.get(i);
			if(state.clazz.equals("separator")) {
				boolean found=false;
				for(Entry<Class<?>, EntitySeparator> entry:seps.entrySet()) {
					if(state.getId().equals(entry.getValue().getAtt())) {
						found=true;
						if(!entry.getValue().getValues().contains(state.getName())) {
							del.add(state);
							System.out.println("delete "+state);
						}
					}
				}
				if(!found)System.out.println("2delete "+state);
			}
		}
		this.states.removeAll(del);
		
		for(Entry<Class<?>, EntitySeparator> entry:seps.entrySet()) {
			if(entry.getValue().getAtt()==null)continue;
			for(String v:entry.getValue().getValues()) {
				boolean found=false;
				for(int i=0;i<this.states.size();i++) {
					State state = this.states.get(i);
					if(state.clazz.equals("separator")) {
						if(state.id.equals(entry.getValue().getAtt())) {
							if(v.equals(state.name)) {
								found = true;
							}
						}
					}
				}
				/*if(!found)states.add(State.from("separator", 
										  entry.getValue().getAtt(),
										  v, 
										  new CircleShape(root.x+j*75f, 
												  		  root.y+i*100f, 
												  		  25f)
										  ));*/
			}
			
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(recalculate) {
			//this.checkforModifications();
			this.setStates();
			recalculate = false;
		}

		g2.setColor(Color.black);
		for(State state:this.states) {
			state.paint(g2);
		}
		for(Edge edge:this.edges) {
			edge.paint(g2);
		}
	}
	public static class State{
		String clazz;
		String name;
		Shape shape;
		Object id;
		Map<String, Object>attributes;
		Color color;
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
			if(color==null)g2.setColor(Color.black);
			else g2.setColor(color);
			
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
			return clazz+" "+id+"  "+name;
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
		public State setAttribute(String k, Object v) {
			this.attributes.put(k, v);
			return this;
		}
	}
	private class CustomMouseAdapter extends MouseAdapter{
		State selection;
		State selection2;
		boolean sel2;
		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			State aux = select(p);
			if(aux!=null) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					if(selection==aux) {
						selection.color = null;
						selection=null;
						ViewPanel.this.repaint();
						return;
					}
					if(selection2==aux) {
						selection2.color = null;
						selection2=null;
						ViewPanel.this.repaint();
						return;
					}
					if(selection==null) {
						selection = aux;
					}
					else {
						if(selection2==null) {
							selection2 = aux;
						}			
					}			
				}	
				if(SwingUtilities.isRightMouseButton(e)) {
					open(p, aux);
				}
			}
			if(selection!=null)selection.color = Color.RED;
			if(selection2!=null)selection2.color = Color.GREEN;
			
			ViewPanel.this.recalculate=false;
			ViewPanel.this.repaint();
		}
		private State select(Point p) {
			for(State state:states) {
				if(state.isInside(p)) {
					return state;
				}
			}
			return null;
		}
		
		JMenuItem separatorMenu;
		JMenuItem initMenu;
		JMenuItem interactionMenu;
		
		SeparatorModule separatorModule;
		InteractionModule interactionModule;
		FSMModule fsmsModule;
		public CustomMouseAdapter() {
			separatorMenu = new JMenuItem("Separator");
			separatorMenu.addActionListener((ActionEvent e)->accessSeparator(this.selection));
			initMenu = new JMenuItem("Init");
			interactionMenu = new JMenuItem("Interaction");
			interactionMenu.addActionListener((ActionEvent e)->accessInteraction(this.selection, this.selection2));

			separatorModule = new SeparatorModule(ViewPanel.this.father, ViewPanel.this, ViewPanel.this.ctrl);
			interactionModule = new InteractionModule(ViewPanel.this.father, ViewPanel.this, ViewPanel.this.ctrl);
			fsmsModule  = new FSMModule(ViewPanel.this.father, ViewPanel.this, ViewPanel.this.ctrl);
		}
		
		private void open(Point p, State state) {
			if(state.clazz.equals("fsms")) {
				accessFSMs(state);
				return;
			}
			JPopupMenu pm = new JPopupMenu();
			if(selection!=null&&selection2==null) {
				if(selection.clazz.equals("class"))pm.add(separatorMenu);
			}
			
			pm.add(initMenu);
			pm.add(interactionMenu);
			
			pm.show(ViewPanel.this, p.x, p.y);
		}
		public void accessSeparator(State state) {
			separatorModule.open(state);
			this.selection = null;
			this.selection2 = null;
		}
		public void accessInteraction(State state1, State state2) {
			ViewPanel.this.addEdge(state1, state2);
			interactionModule.open(state1, state2);

			this.selection = null;
			this.selection2 = null;

		}
		public void accessFSMs(State state) {
			fsmsModule.open(state);

			this.selection = null;
			this.selection2 = null;
		}
	}
	private static class Edge{
		State state1, state2;
		float x1,x2,y1,y2;
		public Edge(State state1, State state2) {
			this.state1 = state1;
			this.state2 = state2;
			Point2D.Float c1 = state1.shape.center();
			Point2D.Float c2 = state2.shape.center();
			x1 = c1.x;
			x2 = c2.x;
			y1 = c1.y;
			y2 = c2.y;
		}
		public void paint(Graphics2D g2) {
			g2.drawLine((int)x1, (int)y1,(int) x2, (int)y2);
			//g2.drawLine((int)state1.shape.x, (int)state1.shape.y, (int)state2.shape.x, (int)state2.shape.y);
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
	public void recalculate() {
		this.recalculate = true;
		this.mouse.selection = null;
		this.mouse.selection2 = null;
	}
	public void addEdge(State state1, State state2) {
		this.edges.add(new Edge(state1, state2));
		this.repaint();
		
	}
}
