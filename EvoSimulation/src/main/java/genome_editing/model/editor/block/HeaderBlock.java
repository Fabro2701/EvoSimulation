package genome_editing.model.editor.block;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import genome_editing.model.editor.block.DrawElement.Shape;
import genome_editing.model.editor.block.DrawElement.StringShape;
import genome_editing.model.elements.Vector2D;

public class HeaderBlock extends PredefinedBlock{
	Block name;
	List<Block>right;
	public HeaderBlock(JSONArray parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
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
				name = Block.forType(value);
				break;
			case "right":
				right.add(Block.forType(value));
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		//shapes.add(new DrawElement.Rectangle(this.base.x, this.base.y, 100, 1, defaultColor));
		bufferShapes.clear();
		
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
			
		shapes.addAll(bufferShapes);
		
	}

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
	@Override
	public Block find(Point point) {
		Block block = null;
		if(name!=null) {
			block = name.find(point);
			if(block != null)return block;
		}
		for(Block child:right) {
			block = child.find(point);
			if(block != null)return block;
		}
		return block;
	}




}
