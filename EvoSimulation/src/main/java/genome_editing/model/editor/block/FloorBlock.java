package genome_editing.model.editor.block;

import java.awt.Point;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import genome_editing.model.editor.block.DrawElement.Shape;

public class FloorBlock  extends PredefinedBlock{
	StrBlock name;
	public FloorBlock(JSONArray parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setParameter(String id, JSONObject value) {
		switch(id) {
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		// TODO Auto-generated method stub
		shapes.add(new DrawElement.Rectangle(base.x, base.y, 40f, floorblockShift, color));
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return floorblockShift;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void config() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Block find(Point point) {
		return null;
	}

}
