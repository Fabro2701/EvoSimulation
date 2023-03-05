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

public class InnerBlock extends PredefinedBlock{
	List<Block>names;
	float xmargin = 5f*mult;
	float ymargin = 3f*mult;
	float interChildx = 5f*mult;
	public InnerBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
	}

	@Override
	protected void config() {
		// TODO Auto-generated method stub
		names = new ArrayList<>();
	}
	@Override
	protected void setParameter(String id, JSONObject value) {

		switch(id) {
			case "names":
				names.add(Block.forType(manager, value));
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		bufferShapes.clear();

		float shiftx =  xmargin;
		for(Block child:names) {
			child.setBase(new Vector2D(base.x+shiftx, this.base.y + ymargin));
			child.paint(shapes);
			shiftx+=child.getWidth()+interChildx;
		}
		
		float w = shiftx-interChildx+xmargin;
		float h = getHeight();
		
		bufferShapes.add(new DrawElement.Rectangle(this.base.x, 
												   this.base.y, 
												   w, ymargin, 
												   color)
						);
		bufferShapes.add(new DrawElement.Rectangle(this.base.x, 
												   this.base.y, 
												   xmargin, h, 
												   color)
						);
		bufferShapes.add(new DrawElement.Rectangle(this.base.x+w-xmargin, 
												   this.base.y, 
												   xmargin, h, 
												   color)
						);
		bufferShapes.add(new DrawElement.Rectangle(this.base.x, 
												   this.base.y+h-ymargin, 
												   w, ymargin, 
												   color)
						);
		float acc = xmargin;
		for(int i=0;i<this.names.size()-1;i++) {
			Block child = this.names.get(i);
			float cw = child.getWidth();
			bufferShapes.add(new DrawElement.Rectangle(
					   this.base.x+acc+cw, 
					   this.base.y, 
					   interChildx, h, 
					   color)
					);
			acc += cw+interChildx;
		}
		shapes.addAll(bufferShapes);
	}

	@Override
	public float getHeight() {
		float m = 0f;
		for(Block child:names) {
			m = Math.max(m, child.getHeight());
		}
		return m+ymargin*2f;
	}

	@Override
	public float getWidth() {
		float sum = 0f;
		for(Block child:names) {
			sum += child.getWidth();
		}
		return sum+(xmargin*2f)+(interChildx*(names.size()-1));
	}
	@Override
	public Block findRecursive(Point point) {
		Block block = null;
		for(Block child:names) {
			if((block = child.findRecursive(point))!= null)return block;
		}
		return block;
	}
	@Override
	public void rightClick(Point point, Component c) {

		for(Block child:names) {
			child.rightClick(point, c);
		}
		
	}
	@Override
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Block b:this.names) {
			arr.put(b.toJSON());
		}
		return new JSONObject().put("type", "InnerBlock")
							   .put("names", arr);
	}
	/*@Override
	public List<Shape> getSelectableShapes() {
		return this.bufferShapes;
	}*/




}
