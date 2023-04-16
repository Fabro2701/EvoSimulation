package state_diagram;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import state_diagram.elements.CompoundState;
import state_diagram.elements.Corner;
import state_diagram.elements.Element;
import state_diagram.elements.EndState;
import state_diagram.elements.InitState;
import state_diagram.elements.SimpleState;
import state_diagram.elements.Splitter;
import state_diagram.elements.TransitionableElement;


public class ToolBar extends JPanel{
	Point base;
	List<TransitionableElement>elems;
	MouseAdapter mouse;
	Diagram diagram;
	public ToolBar(Diagram diagram) {
		this.diagram = diagram;
		this.base = new Point(0,0);
		this.elems = new ArrayList<>();
		
		this.mouse = new CustomMouse();
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		this.addMouseWheelListener(mouse);
		
		init();
	}
	public void init() {
		this.elems.clear();
		this.elems.add(new InitState(null, base, new Point(40,(70+Constants.INIT_STATE_H)/2)));
		this.elems.add(new SimpleState(null, base, new Point(80,(70+Constants.SIMPLE_STATE_H)/2)));
		this.elems.add(new Splitter(null, base, new Point(140,(70+Constants.SPLITTER_H)/2)));
		this.elems.add(new Corner(null, base, new Point(170,(70+Constants.CORNER_H)/2)));
		this.elems.add(new CompoundState(null, base, new Point(290,(70+Constants.COMPOUND_STATE_H)/2)));
		this.elems.add(new EndState(null, base, new Point(320,(70+Constants.SIMPLE_STATE_H)/2)));
	}

	private class CustomMouse extends MouseAdapter{
		TransitionableElement currentElement = null;
		Point currentPoint = null;
		boolean pressed = false;
		boolean out = false;
		
		public void mouseClicked(MouseEvent ev) {
		}
	    public void mousePressed(MouseEvent ev) {
	    	Point p = ev.getPoint();
	    	pressed = true;
	    	currentPoint = p;
	    	if(currentElement==null) {
	    		for(TransitionableElement e:elems) {
		    		if(e.contains(p)!=null) {
		    			currentElement = e;
		    			break;
		    		}
		    	}
	    	}
	    	
	    }
	    public void mouseReleased(MouseEvent ev) {
	    	Point p = ev.getPoint();
	    	if(out) {
				EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
				eventQueue.postEvent(new MouseEvent(ToolBar.this.diagram, MouseEvent.MOUSE_RELEASED,1,0,p.x,p.y-ToolBar.this.getHeight(),1,false));
			}
	    	currentElement = null;
			currentPoint = null;
	    	pressed = false;
			out = false;
			init();
			repaint();
	    }
	    public void mouseEntered(MouseEvent ev) {}
	    public void mouseExited(MouseEvent ev) {}
	    public void mouseWheelMoved(MouseWheelEvent ev){}
	    public void mouseDragged(MouseEvent ev){
	    	Point p = ev.getPoint();
	    	if(pressed) {
	    		if(currentElement!=null){
	    			if(p.y>ToolBar.this.getHeight()) {
	    				out = true;
	    				diagram.insertElement(currentElement,p.x);
						EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
						eventQueue.postEvent(new MouseEvent(ToolBar.this.diagram, MouseEvent.MOUSE_PRESSED,1,0,p.x,0,1,false));
	    				elems.remove(currentElement);
	    				currentElement = null;
		    			init();
			    		
	    			}
	    			else {
	    				currentElement.move(p.x-currentPoint.x, p.y-currentPoint.y);
		    			currentPoint = p;
	    			}
	    			repaint();
	    		}
	    		if(out) {
					EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
					eventQueue.postEvent(new MouseEvent(ToolBar.this.diagram, MouseEvent.MOUSE_DRAGGED,1,0,p.x,p.y-ToolBar.this.getHeight(),1,false));
				}
	    	}
	    }
	    public void mouseMoved(MouseEvent ev){
	    }
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		g2.setFont(Constants.TEXT_FONT);
		for(Element e:elems)e.paint(g2);

	}
}
