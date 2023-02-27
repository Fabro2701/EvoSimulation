package genome_editing.model.editor.block;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import genome_editing.model.editor.block.DrawElement.Shape;
import genome_editing.model.elements.Vector2D;

public class ChildrenBlock extends PredefinedBlock{
	List<Block> children;
	public ChildrenBlock(JSONArray parameters) {
		super(parameters);
		
	}
	@Override
	protected void config() {
		children = new ArrayList<>();
	}
	@Override
	protected void setParameter(String id, JSONObject value) {
		switch(id) {
			case "e":
				children.add(Block.forType(value));
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		float shifty = 0f;
		for(Block child:children) {
			if(child instanceof PredefinedBlock)((PredefinedBlock) child).setColor(color);
			child.setBase(new Vector2D(base.x+Block.inblockShift, base.y+shifty));
			child.paint(shapes);
			shifty += child.getHeight();
		}
		shapes.add(new DrawElement.Rectangle(base.x, base.y, Block.inblockShift, shifty, color));
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
	public Block find(Point point) {
		Block block = null;
		for(Block child:children) {
			block = child.find(point);
			if(block != null)return block;
		}
		return block;
	}


	


}
