package simulator.model.map;

import org.json.JSONObject;

public class Node {
	public int x,y;
	private float temperature, radiation, food, elevation;
	
	public Node(int x, int y, int r, int g, int b) {
		this.x=x;
		this.y=y;
		temperature = r;
		radiation = g;
		food = b;
	}
	public JSONObject toJSON() {
		return new JSONObject().put("x", x)
							   .put("y", y)
							   .put("temperature", temperature)
							   .put("radiaton", radiation)
							   .put("food", food)
							   .put("elevation", elevation);
	}
	
	@Override
	public String toString() {
		return this.toJSON().toString();
	}
	//@Override
	//public String toString() {
	//	return "("+Float.toString(temperature)+" - "+Float.toString(radiation)+" - "+Float.toString(food)+")";
	//}
}
