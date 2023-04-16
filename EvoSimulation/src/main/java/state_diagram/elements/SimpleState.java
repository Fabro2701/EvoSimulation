package state_diagram.elements;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONObject;

import state_diagram.Constants;
import state_diagram.Diagram;
import state_diagram.Util;
import state_diagram.elements.properties.SimpleStateProperties;

public class SimpleState extends TransitionableElement {
	static int shadowMargin = Constants.SHADOW_MARGIN;
	static Stroke stroke = new BasicStroke(1);
	int w=Constants.SIMPLE_STATE_W,h=Constants.SIMPLE_STATE_H;
	int idW,idH;
	
	String id;
	int rest;
	String action="";
	boolean continuous;
	
	public SimpleState(Diagram diagram, Point base, Point pos) {
		super(diagram, base, pos);
		JMenuItem mEdit = new JMenuItem("Edit");
		mEdit.addActionListener(ae->edit());
		this.pm.add(mEdit);
		
		updateId("id");
	}
	public SimpleState(Diagram diagram, JSONObject ob, List<Element> es) {
		super(diagram, ob, es);
		updateId(ob.getString("id"));
		setRest(ob.getInt("rest"));
		setAction(ob.getString("action"));
		setContinuous(ob.getBoolean("continuous"));
		/*JMenuItem mEdit = new JMenuItem("Edit");
		mEdit.addActionListener(ae->edit());
		this.pm.add(mEdit);*/
	}
	@Override
	public void properties() {
		if(properties==null)properties = new SimpleStateProperties(diagram.getProps(),this);
		properties.load();
	}
	public void edit() {
		
		
	}
	public void updateId(String id) {
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
	public void paint(Graphics2D g2) {
		super.paint(g2);
		g2.setColor(Color.white);
		g2.fillRoundRect(base.x+pos.x-w, base.y+pos.y-h, w +idW, h, Constants.ARC_W, Constants.ARC_H);
		g2.setColor(Constants.SIMPLE_STATE_COLOR);
		g2.setStroke(stroke);
		/*g2.drawPolygon(new int[] {base.x+pos.x-w, base.x+pos.x +idW, base.x+pos.x +idW, base.x+pos.x-w}, 
					   new int[] {base.y+pos.y-h, base.y+pos.y-h, base.y+pos.y, base.y+pos.y}, 4);//idH not applied cause id is displayed always in one line
		*/
		g2.drawRoundRect(base.x+pos.x-w, base.y+pos.y-h, w +idW, h, Constants.ARC_W, Constants.ARC_H);

		g2.setColor(Color.black);
		g2.drawString(id, base.x+pos.x-w+(w)/2, base.y+pos.y-h+idH);
		
		if(father!=null) {
			g2.setColor(Color.gray);
			g2.drawString(String.valueOf(father.ID), base.x+pos.x-w, base.y+pos.y-h);
		}
	}

	@Override
	protected void paintShadow(Graphics2D g2) {
		g2.setColor(Constants.SHADOW_COLOR);
		g2.fillRoundRect(base.x+pos.x-w-shadowMargin, base.y+pos.y-h-shadowMargin, 
				    w+2*shadowMargin +idW, h+2*shadowMargin, 
				    Constants.ARC_W, Constants.ARC_H);
	}

	@Override
	public TransitionableElement contains(Point p) {
		if((p.x>=base.x+pos.x-w&&p.x<=base.x+pos.x +idW)&&
			   (p.y>=base.y+pos.y-h&&p.y<=base.y+pos.y))return this;
		return null;
	}

	@Override
	public TransitionableElement containsShadow(Point p) {
		TransitionableElement aux = null;
		if((aux=contains(p))!=null)return aux;
		if((p.x>=base.x+pos.x-w-shadowMargin&&p.x<=base.x+pos.x+shadowMargin +idW)&&
			   (p.y>=base.y+pos.y-h-shadowMargin&&p.y<=base.y+pos.y+shadowMargin))return this;
		return null;
	}
	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("class", "SimpleState")
							   .put("ID", ID)
							   .put("pos", new JSONObject().put("x", pos.x).put("y", pos.y))
							   .put("id", id)
							   .put("fatherID", father!=null?father.ID:null)
							   .put("rest", rest)
							   .put("action", action)
							   .put("continuous", continuous);
	}
	public String getId() {
		return id;
	}
	public int getRest() {
		return rest;
	}
	public void setRest(int rest) {
		this.rest = rest;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isContinuous() {
		return continuous;
	}
	public void setContinuous(boolean continuous) {
		this.continuous = continuous;
	}


}
