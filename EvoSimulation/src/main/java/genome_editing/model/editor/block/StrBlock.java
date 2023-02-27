package genome_editing.model.editor.block;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import genome_editing.model.editor.block.DrawElement.Shape;
import genome_editing.model.editor.block.DrawElement.StringShape;

public class StrBlock extends PredefinedBlock{
	String text;
	int textSize;
	public StrBlock(JSONArray parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
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
				textSize = BlockManager.g2.getFontMetrics().stringWidth(text);
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		shapes.add(new StringShape(text, 
								   this.base.x+2f, 
								   this.base.y+stringHeight-2f, 
								   Color.black));
//		shapes.add(new DrawElement.Rectangle(this.base.x,     this.base.y, 
//				 1, 100, 
//				 color));
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
	public Block find(Point point) {
		return null;
	}




}
