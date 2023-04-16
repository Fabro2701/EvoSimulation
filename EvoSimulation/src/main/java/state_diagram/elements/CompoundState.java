package state_diagram.elements;

import static state_diagram.Constants.COMPOUND_STATE_HEADER;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;


import state_diagram.Constants;
import state_diagram.Diagram;
import state_diagram.Util;
import state_diagram.elements.properties.CompoundStateProperties;
import state_diagram.elements.properties.CornerProperties;

public class CompoundState extends TransitionableElement {
	static int shadowMargin = Constants.SHADOW_MARGIN;
	static Stroke stroke = new BasicStroke(1);
	int w=Constants.COMPOUND_STATE_W,h=Constants.COMPOUND_STATE_H;
	
	String id;
	int idW,idH;
	int exW,exH;
	
	List<TransitionableElement>children;
	public CompoundState(Diagram diagram, Point base, Point pos) {
		super(diagram, base, pos);
		this.children = new ArrayList<>();
		updateId("compid");
	}
	public CompoundState(Diagram diagram, JSONObject ob, List<Element> es) {
		super(diagram, ob, es);
		this.children = new ArrayList<>();
		updateId(ob.getString("id"));
		exW = ob.getInt("exW");
		exH = ob.getInt("exH");
	}
	
	public void insertChild(TransitionableElement e) {
		this.children.add(e);
		e.setFather(this);
	}
	public void removeChild(TransitionableElement e) {
		this.children.remove(e);
		e.setFather(null);
	}
	
	@Override
	public void paint(Graphics2D g2) {
		super.paint(g2);
		//System.out.println(children.size());
		g2.setColor(Color.white);
		g2.fillRoundRect(base.x+pos.x-w, base.y+pos.y-h, w +exW, h +exH, Constants.ARC_W, Constants.ARC_H);
		g2.setColor(Constants.SIMPLE_STATE_COLOR);
		g2.setStroke(stroke);
		g2.drawRoundRect(base.x+pos.x-w, base.y+pos.y-h, w+exW, h+exH, Constants.ARC_W, Constants.ARC_H);
		g2.drawLine(base.x+pos.x-w, base.y+pos.y-h +idH+COMPOUND_STATE_HEADER, base.x+pos.x+exW, base.y+pos.y-h +idH+COMPOUND_STATE_HEADER);

		g2.setColor(Color.black);
		g2.drawString(id, base.x+pos.x-w+(w-idW)/2, base.y+pos.y-h+idH);
		
		g2.setColor(Color.gray);
		g2.drawString((father!=null?father.ID+"-":"")+String.valueOf(ID), base.x+pos.x-w, base.y+pos.y-h);
		
		for(var e:children)e.paint(g2);
	}
	@Override
	public void move(int x, int y) {
		pos.x += x;
		pos.y += y;
		for(var e:children)e.move(x, y);
	}
	@Override
	protected void paintShadow(Graphics2D g2) {
		g2.setColor(Constants.SHADOW_COLOR);
		g2.fillRoundRect(base.x+pos.x-w-shadowMargin, base.y+pos.y-h-shadowMargin, 
				    	 w+2*shadowMargin +exW, 	  h+2*shadowMargin +exH, 
				    	 Constants.ARC_W, Constants.ARC_H);
	}

	@Override
	public TransitionableElement contains(Point p) {
		if((p.x>=base.x+pos.x-w&&p.x<=base.x+pos.x +exW)&&
		(p.y>=base.y+pos.y-h&&p.y<=base.y+pos.y-(h-(COMPOUND_STATE_HEADER+idH))))return this;

		TransitionableElement aux = null;
		for(var child:children) {
			if((aux=child.contains(p))!=null)return aux;
		}
		return null;
	}

	@Override
	public TransitionableElement containsShadow(Point p) {
		TransitionableElement aux = null;
		for(var child:children) {
			if((aux=child.containsShadow(p))!=null)return aux;
		}
		if((p.x>=base.x+pos.x-w&&p.x<=base.x+pos.x +exW)&&
		   (p.y>=base.y+pos.y-h&&p.y<=base.y+pos.y +exH))return null;
		
		if( (p.x>=base.x+pos.x-w-shadowMargin&&p.x<=base.x+pos.x+shadowMargin +exW)&&
			   (p.y>=base.y+pos.y-h-shadowMargin&&p.y<=base.y+pos.y+shadowMargin +exH))return this;
		return null;
	}
	public CompoundState containsInside(Point p) {
		CompoundState aux = null;
		if((p.x>=base.x+pos.x-w&&p.x<=base.x+pos.x +exW)&&
				(p.y>=base.y+pos.y-h&&p.y<=base.y+pos.y-(h-(COMPOUND_STATE_HEADER+idH))))return null;
		
		for(var child:children) {
			if(child instanceof CompoundState) {
				if((aux=((CompoundState)child).containsInside(p))!=null)return aux;
			}
			
		}
		if( ((p.x>=base.x+pos.x-w&&p.x<=base.x+pos.x +exW)&&
				(p.y>=base.y+pos.y-h&&p.y<=base.y+pos.y +exH)))return this;
		return null;
	}
	@Override
	public void properties() {
		if(properties==null)properties = new CompoundStateProperties(diagram.getProps(),this);
		properties.load();
	}
	public enum RESIZE_DIR{
		HORIZONTAL,VERTICAL
	}
	public RESIZE_DIR getResizeDir(Point point) {
		if(point.x>base.x+pos.x+exW)return RESIZE_DIR.HORIZONTAL;
		else return RESIZE_DIR.VERTICAL; 
	}

	public void increaseWidth(int i) {
		this.exW += i;
		for(var t:this.fromTs) {
			if(t.fromShift.x>0)t.fromShift.x += i;
		}
		for(var t:this.toTs) {
			if(t.fromShift.x>0)t.toShift.x += i;
		}
	}
	public void increaseHeight(int i) {
		this.exH += i;
		for(var t:this.fromTs) {
			if(t.fromShift.y>0)t.fromShift.y += i;
		}
		for(var t:this.toTs) {
			if(t.fromShift.y>0)t.toShift.y += i;
		}
	}
	private void updateId(String id) {
		int lastw = idW;
		this.id=id;
		Rectangle2D rec = getIdBounds();
		this.idW = (int) rec.getWidth();
		this.idH = (int) rec.getHeight();
		
		for(var t:this.fromTs) {
			if(t.fromShift.x>0)t.fromShift.x += idW-lastw;
		}
		for(var t:this.toTs) {
			if(t.fromShift.x>0)t.toShift.x += idW-lastw;
		}
		
	}
	private Rectangle2D getIdBounds() {
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		return Constants.TEXT_FONT.getStringBounds(id, frc);
	}

	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("class", "CompoundState")
				   			   .put("ID", ID)
				   			   .put("id", id)
							   .put("pos", new JSONObject().put("x", pos.x).put("y", pos.y))
							   .put("fatherID", father!=null?father.ID:null)
							   .put("exW", exW)
							   .put("exH", exH);

	}

	public List<TransitionableElement> getChildren() {
		return children;
	}
	@Override
	protected void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.id()+"("+ID+")");
        buffer.append('\n');
        for (Iterator<TransitionableElement> it = children.iterator(); it.hasNext();) {
        	TransitionableElement next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
	@Override
    public String id() {
		return this.getClass().getSimpleName();
	}

}
