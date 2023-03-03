package block_manipulation.block;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;

public abstract class PredefinedBlock extends Block{
	Color color;
	protected List<Shape>bufferShapes;
	public PredefinedBlock(BlockManager manager, JSONArray parameters) {
		super(manager);
		bufferShapes = new ArrayList<Shape>();
		config();
		for(int i=0;i<parameters.length();i++) {
			JSONObject param = parameters.getJSONObject(i);
			this.setParameter(param.getString("name"),param.getJSONObject("value"));
		}
	}
	public List<Shape> getBufferShapes() {
		return bufferShapes;
	}
	protected void setColor(Color color) {
		this.color = color;
	}
	protected abstract void config();
	protected abstract void setParameter(String id, JSONObject value);
	public Block findPredefined(Point point) {
		for(Shape shape:bufferShapes) {
			if(shape.contains(point))return this;
		}
		return null;
	}
}
