package map;

public class Node {
	private float temperature, radiation, food;
	
	public Node(int r, int g, int b) {
		temperature = r;
		radiation = g;
		food = b;
	}
	@Override
	public String toString() {
		return "("+Float.toString(temperature)+" - "+Float.toString(radiation)+" - "+Float.toString(food)+")";
	}
}
