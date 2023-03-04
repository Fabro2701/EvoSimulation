package block_manipulation.block;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.Vector2D;


public abstract class Block implements BlockRenderer{
	protected BlockManager manager;
	
	protected Vector2D base;
	
	protected static float mult = 1f;
	
	protected static float inblockShift = 8f*mult;
	protected static float stringHeight = 15f;
	protected static float floorblockShift = 4f*mult;
	
	
	
	protected static Font font = new Font("Courier New", 1, 13);
	protected Color defaultColor = new Color(255,179,0,150);

	public static Color white = new Color(255,255,255,255);
	
	public Block(BlockManager manager) {
		this.manager = manager;
	}
	protected static Block forType(BlockManager manager, JSONObject source) {
		String type = source.getString("type");
		try {
			if(type.equals("PredefinedBlock")) {
				String name = source.getString("id")+"Block";
				Class<?>clazz = Class.forName("block_manipulation.block."+name);
				JSONArray parameters = source.getJSONArray("params");
				PredefinedBlock block = (PredefinedBlock)clazz.getConstructors()[0].newInstance(manager, parameters);
				return block;
			}
			else if(type.equals("RecursiveBlock")) {
				return new RecursiveBlock(manager, source.getString("id"));
			}
			else {
				System.err.println(type+" unknown block type");
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public abstract void rightClick(Point point, Component c);
	public abstract Block findRecursive(Point point);
	public abstract Block findPredefined(Point point);
	public Vector2D getBase() {
		return base;
	}
	public void setBase(Vector2D base) {
		this.base = base;
	}
	public static void changeMult(float change) {
		mult += change;
	}
}
