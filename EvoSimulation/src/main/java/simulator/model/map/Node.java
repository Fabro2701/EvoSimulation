package simulator.model.map;

import org.json.JSONObject;

import simulator.Constants.NODE_TYPE;

public class Node {
	public int x, y;
	public float temperature, radiation, elevation;
	public NODE_TYPE type;

	/**
	 * Constructor
	 * @param x
	 * @param y
	 * @param r temperature (red)
	 * @param g radiation (green)
	 * @param elevation elevation (green)
	 */
	public Node(int x, int y, float r, float g, float elevation, NODE_TYPE type) {
		this.x = x;
		this.y = y;
		this.temperature = r;
		this.radiation = g;
		this.elevation = elevation;
		this.type = type;
	}

	public JSONObject toJSON() {
		return new JSONObject().put("x", x).put("y", y)
							   .put("temperature", temperature)
							   .put("radiaton", radiation)
							   .put("elevation", elevation)
							   .put("type", type);
	}

	@Override
	public String toString() {
		return this.toJSON().toString();
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public static void main(String args[]) {
		Node n = new Node(0,0,250/255f, 100/255f, 0, null);
		System.out.println(n.elevation);
		System.out.println(n.temperature);
		System.out.println(n.radiation);
	}
}
