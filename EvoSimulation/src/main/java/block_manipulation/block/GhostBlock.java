package block_manipulation.block;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.block.DrawElement.StringShape;
import block_manipulation.Vector2D;

public class GhostBlock extends PredefinedBlock{
	RecursiveBlock reference;
	public GhostBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
	}

	@Override
	protected void config() {
	}
	@Override
	protected void setParameter(String id, JSONObject value) {

		switch(id) {
			case "reference":
				reference = (RecursiveBlock) value.get("value");
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		bufferShapes.clear();
		
		bufferShapes.add(new DrawElement.Rectangle(this.base.x, 
												   this.base.y, 
												   manager.getGraphics().getFontMetrics().stringWidth(reference.getRule())*mult, 
												   stringHeight, 
												   color)
							);
		bufferShapes.add(new StringShape(reference.getRule(), 
				   this.base.x+2f, 
				   this.base.y+stringHeight-2f, 
				   Color.black));
		
		shapes.addAll(bufferShapes);
		
		
	}

	@Override
	public float getHeight() {
		return stringHeight*mult;
	}

	@Override
	public float getWidth() {
		return manager.getGraphics().getFontMetrics().stringWidth(reference.getRule())*mult;
	}
	
	@Override
	public Block find(Point point) {
		return null;
	}

	@Override
	public List<Shape> getSelectableShapes() {
		return bufferShapes;
	}




}
