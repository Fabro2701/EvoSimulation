
package state_diagram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import state_diagram.elements.CompoundState;
import state_diagram.elements.CompoundState.RESIZE_DIR;
import state_diagram.elements.Element;
import state_diagram.elements.IdGenerator;
import state_diagram.elements.InitState;
import state_diagram.elements.Transition;
import state_diagram.elements.TransitionableElement;
import state_diagram.product.FlowController;
import state_diagram.product.InitStateProduct;

public class Diagram extends JPanel{
	Point base;
	List<TransitionableElement>elems;
	List<Transition>ts;
	MouseAdapter mouse;
	Consumer<Graphics2D>gaux;
	private JScrollPane props;
	String fsmID="";
	public Diagram() {
		this.base = new Point(0,0);
		this.elems = new ArrayList<>();
		this.ts = new ArrayList<>();
		
		this.mouse = new CustomMouse();
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		this.addMouseWheelListener(mouse);
		
		/*this.elems.add(new SimpleState(this, base, new Point(200,300)));
		this.elems.add(new Splitter(this, base, new Point(400,300)));
		this.elems.add(new InitState(this, base, new Point(400,500)));
		this.elems.add(new Corner(this, base, new Point(100,100)));
		this.elems.add(new CompoundState(this, base, new Point(300,100)));*/
	}
	public static Diagram forName(String fsmID) {
		Diagram d = new Diagram();
		d.fsmID = fsmID;
		return d;
	}

	private class CustomMouse extends MouseAdapter{
		
		TransitionableElement currentElement = null;
		TransitionableElement shadowElement = null;
		CompoundState currentCompound = null;
		Transition currentTransition = null;
		Point currentPoint = null;
		boolean pressed = false;
		
		public void mouseClicked(MouseEvent ev) {
			Diagram.this.requestFocus();
	    	Point p = ev.getPoint();
			if (SwingUtilities.isRightMouseButton(ev)) {
    			TransitionableElement aux = null;
				for(TransitionableElement e:elems) {
		    		if((aux=e.contains(p))!=null) {
		    			aux.openMenu(p);
		    			break;
		    		}
		    	}
			}
			else if (SwingUtilities.isLeftMouseButton(ev)) {
    			TransitionableElement aux = null;
				for(TransitionableElement e:elems) {
		    		if((aux=e.contains(p))!=null) {
		    			aux.properties();
		    			break;
		    		}
		    	}
				for(var t:ts) {
		    		if(t.containsLine(p)) {
		    			t.properties();
		    			break;
		    		}
		    	}
			}
		}
	    public void mousePressed(MouseEvent ev) {
	    	Point p = ev.getPoint();
	    	pressed = true;
	    	currentPoint = p;
	    	if(currentElement==null) {
    			TransitionableElement aux = null;
	    		for(TransitionableElement e:elems) {
		    		if((aux=e.contains(p))!=null) {
		    			currentElement = aux;
		    			Diagram.this.setCursor(Constants.HAND_CURSOR);
		    			break;
		    		}
		    	}
	    	}
	    	
	    }
	    public void mouseReleased(MouseEvent ev) {
	    	Point p = ev.getPoint();
	    	if(currentTransition!=null) {
	    		for(TransitionableElement e:elems) {
	    			if(e==currentTransition.getFrom())continue;
	    			TransitionableElement aux = null;
		    		if((aux=e.containsShadow(p))!=null) {
		    			currentTransition.setDest(aux);
		    			currentTransition.setToShift(aux.getRelativePosition(p));
		    			currentTransition.validate();
		    			ts.add(currentTransition);
		    			break;
		    		}
		    	}
	    	}
	    	if(currentElement!=null) {
				boolean found = false;
	    		for(var e:elems) {//put elem inside a cmp
	    			if(currentElement == e)continue;
    				if(e instanceof CompoundState) {
    					CompoundState cse = (CompoundState)e;
    					TransitionableElement aux = null;
    					if((aux=cse.containsInside(p))!=null) {
    						currentCompound = (CompoundState) aux;
    						Diagram.this.setCursor(Constants.DEFAULT_CURSOR);
    						found = true;
    						break;
    					}
    				}
    			}
	    		if(found) {
	    			if(currentElement.getFather()!=null) {
	    				currentElement.getFather().removeChild(currentElement);
	    			}
	    			else Diagram.this.elems.remove(currentElement);
		    		currentCompound.insertChild(currentElement);
	    		}
			}
	    	if(currentElement!=null) {
	    		boolean found = false;
	    		CompoundState father=null,aux=null;
				if((father=currentElement.getFather())!=null) {
					if(currentElement != father) {
						if((aux=father.containsInside(p))!=father&&aux!=currentElement) {
							father.removeChild(currentElement);
							currentElement.setFather(aux);
							found = true;
						}
					}
					
				}
				if(found) {
					if(father.getFather()==null) {
						Diagram.this.elems.add(currentElement);
					}
					
				}
	    	}
	    	
	    	currentElement = null;
	    	if(shadowElement!=null)shadowElement.setOver(null);
	    	shadowElement = null;
	    	currentTransition = null;
			currentPoint = null;
			currentCompound = null;
	    	pressed = false;
	    	gaux = null;
	    	Diagram.this.setCursor(Constants.DEFAULT_CURSOR);
	    	repaint();
	    }
	    public void mouseEntered(MouseEvent ev) {}
	    public void mouseExited(MouseEvent ev) {}
	    public void mouseWheelMoved(MouseWheelEvent ev){}
	    public void mouseDragged(MouseEvent ev){
	    	Point p = ev.getPoint();
	    	if(pressed) {
	    		//temporal transition
	    		if(currentTransition!=null) {
	    			if (SwingUtilities.isLeftMouseButton(ev)) {
	    				Point from = shadowElement.getOver();
		    			gaux = g2->g2.drawLine(from.x, from.y, p.x, p.y);
		    			//shadowElement=null;
		    			for(TransitionableElement e:elems) {
	    					TransitionableElement aux = null;
		    	    		if((aux=e.containsShadow(p))!=null) {
		    	    			aux.setOver(p);
		    	    			repaint();
		    	    			break;
		    	    		}
		    	    	}
			    		repaint();
	    			}
	    			
	    		}
	    		//navigate
	    		else if(currentElement==null && shadowElement==null) {
		    		Diagram.this.base.move(base.x+p.x-currentPoint.x, 
		    				  			   base.y+p.y-currentPoint.y);
		    		currentPoint = p;
		    		repaint();
	    		}
	    		else if(currentElement!=null){
	    			Diagram.this.setCursor(Constants.HAND_CURSOR);
	    			for(var e:elems) {//put elem inside a cmp
	    				if(e instanceof CompoundState) {
	    					TransitionableElement aux = null;
	    					CompoundState cse = (CompoundState)e;
	    					if((aux=cse.containsInside(p))!=null) {
	    						currentCompound = (CompoundState) aux;
	    						Diagram.this.setCursor(Constants.WAIT_CURSOR);
	    					}
	    				}
	    			}
	    			//move elem
	    			currentElement.move(p.x-currentPoint.x, p.y-currentPoint.y);
	    			currentPoint = p;
	    			
	    			
		    		repaint();
	    		}
	    		//create trans
	    		else if(shadowElement!=null){
	    			if (SwingUtilities.isLeftMouseButton(ev)) {
	    				Transition t = new Transition(Diagram.this, shadowElement, shadowElement.getRelativePosition(p));
		    			currentTransition = t;
		    			//currentPoint = p;
			    		repaint();
	    			}
	    			else if (SwingUtilities.isRightMouseButton(ev)) {
	    				
	    				if(shadowElement instanceof CompoundState) {
	    					CompoundState cse = (CompoundState)shadowElement;
	    					RESIZE_DIR dir = cse.getResizeDir(p);
	    					if(dir==RESIZE_DIR.HORIZONTAL) {
	    						cse.increaseWidth(p.x>currentPoint.x?1:-1);
	    						currentPoint = p;
	    						Diagram.this.setCursor(Constants.E_RESIZE_CURSOR);
	    						repaint();
	    					}
	    					else {
	    						cse.increaseHeight(p.y>currentPoint.y?1:-1);
	    						currentPoint = p;
	    						Diagram.this.setCursor(Constants.S_RESIZE_CURSOR);
	    						repaint();
	    					}
	    				}
	    				
	    			}
	    		}
	    	}
	    }
	    public void mouseMoved(MouseEvent ev){
	    	Point p = ev.getPoint();
	    	if(pressed) {
	    		return;
	    	}
			Diagram.this.setCursor(Constants.DEFAULT_CURSOR);
	    	boolean r = false;
	    	if(shadowElement!=null) {
	    		shadowElement.setOver(null);
	    		shadowElement = null;
	    		r = true;
	    	}
	    	TransitionableElement aux = null;
	    	for(TransitionableElement e:elems) {
	    		if((aux=e.contains(p))!=null) {
	    			Diagram.this.setCursor(Constants.HAND_CURSOR);
	    			shadowElement = aux;
	    			r = true;
	    			break;
	    		}
	    		else if((aux=e.containsShadow(p))!=null) {
	    			Diagram.this.setCursor(Constants.CROSSHAIR_CURSOR);
	    			aux.setOver(p);
	    			shadowElement = aux;
	    			r = true;
	    			break;
	    		}
	    	}
	    	if(r)repaint();
	    }
	}
	static Stroke stroke = new BasicStroke(2);
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		g2.setFont(Constants.TEXT_FONT);
		for(Element e:elems)e.paint(g2);
		g2.setStroke(stroke);
		for(var t:ts) {
			t.paint(g2);
		}
		
		if(gaux!=null)gaux.accept(g2);
		
		StringBuilder sb = new StringBuilder();
		for(var e:elems) sb.append(e.toString());
		String tree = sb.toString();
		
		float h=0;
		g2.setColor(Color.gray);
		g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
		for(String line:tree.split("\n")) {
			Rectangle2D r = g2.getFontMetrics().getStringBounds(line, g2);
			h += r.getHeight();
			g2.drawString(line, 0f, h);
		}
		//System.out.println(tree);
	}
	public void insertElement(TransitionableElement e, int x) {
		e.ID = IdGenerator.nextId();
		e.setBase(base);
		e.setPos(new Point(x-base.x,-base.y));
		e.setDiagram(this);
		this.elems.add(e);
		((CustomMouse)mouse).currentElement = e;
	}
	public void save(String filename) {
		System.out.println("Saving");
		JSONObject ob = new JSONObject();
		ob.put("base", new JSONObject().put("x", base.x).put("y", base.y));
		ob.put("fsmid", this.fsmID);
		JSONArray arr = new JSONArray();
		for(var e:elems) {
			arr.put(e.toJSON());
			if(e instanceof CompoundState) {
				arr.putAll(extractElems((CompoundState) e));
			}
		}
		for(var e:ts) {
			arr.put(e.toJSON());
		}
		ob.put("elems", arr);
		try {
			FileWriter file = new FileWriter(filename);
			file.write(ob.toString(4));
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private JSONArray extractElems(CompoundState cse) {
		JSONArray arr = new JSONArray();
		for(var e:cse.getChildren()) {
			arr.put(e.toJSON());
			if(e instanceof CompoundState) {
				arr.putAll(extractElems((CompoundState) e));
			}
		}
		return arr;
	}
	public void load(String filename) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		System.out.println("Loading");
		this.ts.clear();
		this.elems.clear();
		JSONObject ob = null;
		try {
			ob = new JSONObject(new JSONTokener(new FileInputStream(filename)));
		} catch (JSONException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.base = Util.pointFromJSON(ob.getJSONObject("base"));
		this.fsmID = ob.getString("fsmid");
		JSONArray arr = ob.getJSONArray("elems");
		
		List<Element>tmpElements = new ArrayList<>();
		for(int i=0;i<arr.length();i++) {
			JSONObject o = arr.getJSONObject(i);
			String type = o.getString("class");
			
			Element e = (Element) Class.forName("state_diagram.elements."+type).getConstructor(Diagram.class,JSONObject.class,List.class).newInstance(this,o,tmpElements);
			tmpElements.add(e);
			if(e instanceof Transition) {
				this.ts.add((Transition) e);
				((Transition) e).validate();
			}
			else {
				if(((TransitionableElement) e).getFather()==null) {
					this.elems.add((TransitionableElement) e);
				}
				
			}
		}
		repaint();
		
		
	}
	public FlowController createFlow(String pck) {
		FlowController ctrl = new FlowController(pck, getFsmID());
		InitState ini = this.getInit();
		ctrl.setCurrent(new InitStateProduct(ctrl, ini));
		return ctrl;
	}
	public Point getBase() {
		return base;
	}
	public void setPropertiesPanel(JScrollPane props) {
		this.props = props;
	}
	public JScrollPane getProps() {
		return props;
	}
	public List<TransitionableElement> getElems() {
		return elems;
	}
	public InitState getInit() {
		return (InitState) elems.stream().filter(e->e instanceof InitState).findAny().get();
	}
	public List<Transition> getTransitions() {
		return ts;
	}
	public String getFsmID() {
		return fsmID;
	}
}
