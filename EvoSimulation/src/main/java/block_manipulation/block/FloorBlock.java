package block_manipulation.block;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;

public class FloorBlock  extends PredefinedBlock{
	StrBlock name;
	public FloorBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
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
		bufferShapes.clear();
		bufferShapes.add(new DrawElement.Rectangle(base.x, base.y, 40f*mult, floorblockShift, color));

		shapes.addAll(bufferShapes);
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
	public Block findRecursive(Point point) {
		return null;
	}

	@Override
	public void rightClick(Point point, Component c) {
	}
	/*@Override
	public List<Shape> getSelectableShapes() {
		return List.of(this.bufferShapes.get(0));
	}*/

}
