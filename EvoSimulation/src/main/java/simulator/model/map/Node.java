package simulator.model.map;

import java.util.Objects;

import org.json.JSONObject;

import simulator.Constants.NODE_TYPE;

public class Node {
	public int x, y;
	public float att1, att2, att3, elevation;
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
		this.att1 = r;
		this.att2 = g;
		this.elevation = elevation;
		this.type = type;
	}

	public JSONObject toJSON() {
		return new JSONObject().put("x", x).put("y", y)
							   .put("att1", att1)
							   .put("att2", att2)
							   .put("att3", att3)
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

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return x == other.x && y == other.y;
	}

	public static void main(String args[]) {
		Node n = new Node(0,0,250/255f, 100/255f, 0, null);
		System.out.println(n.elevation);
		System.out.println(n.att1);
		System.out.println(n.att2);
	}
}
