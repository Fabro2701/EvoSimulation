package block_manipulation.block;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.block.DrawElement.StringShape;


public class StrBlock extends PredefinedBlock{
	String text;
	int textSize;
	public StrBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
	}

	@Override
	protected void config() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void setParameter(String id, JSONObject value) {
		switch(id) {
			case "text":
				text = value.getString("value");
				textSize = manager.getGraphics().getFontMetrics().stringWidth(text);
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		bufferShapes.clear(); 
		bufferShapes.add(new StringShape(text, 
								   this.base.x+2f, 
								   this.base.y+stringHeight-2f, 
								   Color.black,
								   manager.getGraphics().getFontMetrics().getStringBounds(text, manager.getGraphics())
								   )
				);
//		shapes.add(new DrawElement.Rectangle(this.base.x,     this.base.y, 
//				 1, 100, 
//				 color));
		shapes.addAll(bufferShapes);
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return stringHeight;
	}

	@Override
	public float getWidth() {
		return textSize+3f;
	}

	@Override
	public Block findRecursive(Point point) {
		return null;
	}
	
	@Override
	public void rightClick(Point point, Component c) {
	}
	@Override
	public int decisionsUsed() {
		return 0;
	}
	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("type", "StrBlock")
							   .put("text", this.text);
	}



}
