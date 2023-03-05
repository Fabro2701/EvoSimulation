package block_manipulation.block;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.block.DrawElement.StringShape;
import block_manipulation.Vector2D;

public class HeaderBlock extends PredefinedBlock{
	Block name;
	List<Block>right;
	float xmargin = 5f*mult;
	float ymargin = 3f*mult;
	float interChildx = 7f*mult;
	public HeaderBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
	}

	@Override
	protected void config() {
		// TODO Auto-generated method stub
		right = new ArrayList<>();
	}
	@Override
	protected void setParameter(String id, JSONObject value) {

		switch(id) {
			case "name":
				name = Block.forType(manager, value);
				break;
			case "right":
				right.add(Block.forType(manager, value));
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		bufferShapes.clear();
		
		float xmargin = this.xmargin;		
		xmargin *= right.size()>0?1f:0f;
		
		float shiftx =  name.getWidth()+xmargin;
		float shifty = name.getHeight();
		for(Block child:right) {
			child.setBase(new Vector2D(base.x+shiftx, this.base.y+ymargin));
			child.paint(shapes);
			shiftx+=child.getWidth()+interChildx;
			shifty = Math.max(child.getHeight(), shifty);
		}

		
	
		if(name!=null) {
			name.setBase(new Vector2D(this.base.x, this.base.y));
			name.paint(shapes);

			bufferShapes.add(new DrawElement.Rectangle(this.base.x, 
													   this.base.y, 
													   name.getWidth(), shifty+ymargin*2f, 
													   color)
							);
		}
		
		
		if(this.right.size()>0) {
			float nw = name.getWidth();
			float nh = name.getHeight();
			float h = getHeight();
			float w = getWidth()-nw;
			bufferShapes.add(new DrawElement.Rectangle(
					   this.base.x+nw, 
					   this.base.y, 
					   w, ymargin, 
					   color)
			);
			bufferShapes.add(new DrawElement.Rectangle(
					   this.base.x+nw, 
					   this.base.y, 
					   xmargin, h, 
					   color)
			);
			bufferShapes.add(new DrawElement.Rectangle(
					   this.base.x+nw+w-xmargin, 
					   this.base.y, 
					   xmargin, h, 
					   color)
			);
			bufferShapes.add(new DrawElement.Rectangle(
					   this.base.x+nw, 
					   this.base.y+h-ymargin, 
					   w, ymargin, 
					   color)
			);
			
			float acc = xmargin;
			for(int i=0;i<this.right.size()-1;i++) {
				Block child = this.right.get(i);
				float cw = child.getWidth();
				bufferShapes.add(new DrawElement.Rectangle(
						   this.base.x+nw+acc+cw, 
						   this.base.y, 
						   interChildx, h, 
						   color)
						);
				acc += cw+interChildx;
			}
			
		}
		
		shapes.addAll(bufferShapes);
		
		/*
		if(name!=null) {
			name.setBase(new Vector2D(this.base.x, this.base.y));
			name.paint(shapes);
			bufferShapes.add(new DrawElement.Rectangle(this.base.x,     this.base.y, 
											     name.getWidth(), name.getHeight(), 
											     color));
		}
		
		float shiftx =  (name!=null?name.getWidth():0f);
		for(Block child:right) {
			child.setBase(new Vector2D(base.x+shiftx, this.base.y));
			
			child.paint(shapes);
			shiftx+=child.getWidth();
		}
		
		shiftx =  (name!=null?name.getWidth():0f);
		for(Block child:right) {
			if(child instanceof PredefinedBlock) {
				bufferShapes.add(new DrawElement.Rectangle(this.base.x+shiftx,     this.base.y, 
						 child.getWidth(), stringHeight, 
						 color));
			}
			shiftx+=child.getWidth();
		}
			
		shapes.addAll(bufferShapes);*/
	}

	@Override
	public float getHeight() {
		float m = name.getHeight();
		for(Block child:this.right) {
			m = Math.max(m, child.getHeight());
		}
		return m+this.ymargin*2f;
	}

	@Override
	public float getWidth() {
		float sum = (name!=null?name.getWidth():0f)+this.xmargin*2f;
		for(Block child:right) {
			sum += child.getWidth();
		}
		if(right.size()>0)return sum+(interChildx*(right.size()-1));
		return sum;
	}
	/*
	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return stringHeight;
	}

	@Override
	public float getWidth() {
		float sum = (name!=null?name.getWidth():0f);
		for(Block child:right) {
			sum += child.getWidth();
		}
		return sum;
	}
	 */
	@Override
	public Block findRecursive(Point point) {
		Block block = null;
		if(name!=null) {
			if((block = name.findRecursive(point))!= null)return block;
		}
		for(Block child:right) {
			if((block = child.findRecursive(point))!= null)return block;
		}
		return block;
	}

	@Override
	public void rightClick(Point point, Component c) {
		if(name!=null) {
			name.rightClick(point, c);
		}
		for(Block child:right) {
			child.rightClick(point, c);
		}
		
	}

	@Override
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Block b:this.right) {
			arr.put(b.toJSON());
		}
		return new JSONObject().put("type", "HeaderBlock")
				   			   .put("name", this.name.toJSON())
							   .put("right", arr);
	}

	/*@Override
	public List<Shape> getSelectableShapes() {
		return List.of(this.bufferShapes.get(0));
	}*/




}
