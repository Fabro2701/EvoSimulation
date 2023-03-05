package block_manipulation.block;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.Vector2D;

public class ChildrenBlock extends PredefinedBlock{
	List<Block> children;
	public ChildrenBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
		
	}
	@Override
	protected void config() {
		children = new ArrayList<>();
	}
	@Override
	protected void setParameter(String id, JSONObject value) {
		switch(id) {
			case "e":
				children.add(Block.forType(manager, value));
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		bufferShapes.clear();
		float shifty = 0f;
		for(Block child:children) {
			if(child instanceof PredefinedBlock)((PredefinedBlock) child).setColor(color);
			child.setBase(new Vector2D(base.x+Block.inblockShift, base.y+shifty));
			child.paint(shapes);
			shifty += child.getHeight();
		}
		bufferShapes.add(new DrawElement.Rectangle(base.x, base.y, Block.inblockShift, shifty, color));
		
		shapes.addAll(bufferShapes);
	}

	@Override
	public float getHeight() {
		float sum = 0f;
		for(Block child:children) {
			sum += child.getHeight();
		}
		return sum;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Block findRecursive(Point point) {
		Block block = null;
		for(Block child:children) {
			if((block = child.findRecursive(point))!= null)return block;
		}
		return block;
	}
	@Override
	public void rightClick(Point point, Component c) {
		for(Block child:children) {
			child.rightClick(point, c);
		}
	}
	/*@Override
	public List<Shape> getSelectableShapes() {
		return List.of(this.bufferShapes.get(0));
	}*/
	@Override
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Block b:this.children) {
			arr.put(b.toJSON());
		}
		return new JSONObject().put("type", "ChildrenBlock")
							   .put("children", arr);
	}


	


}
