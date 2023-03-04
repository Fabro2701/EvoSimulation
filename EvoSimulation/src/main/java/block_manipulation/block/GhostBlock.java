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
		
		float sw = manager.getGraphics().getFontMetrics().stringWidth(reference.getRule());
		bufferShapes.add(new DrawElement.Diamond(this.base.x, 
												   this.base.y, 
												   sw*mult, 
												   stringHeight, 
												   manager.shouldBlockBeIluminated(reference.rule)?BlockUtil.iluminate(color):color)
							);
		bufferShapes.add(new StringShape(reference.getRule(), 
				   this.base.x+(this.getWidth()*0.5f)-(sw*0.5f), 
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
	public Block findRecursive(Point point) {
		return null;
	}
	public String getReferenceRule() {
		return this.reference.getRule();
	}

	@Override
	public void rightClick(Point point, Component c) {

	}

	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("type", "GhostBlock");
	}
	/*@Override
	public List<Shape> getSelectableShapes() {
		return bufferShapes;
	}*/




}
