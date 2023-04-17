package experiment.models.metro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RouteManager {
	Map<String, Node>nodes;
	public RouteManager() {
		this.nodes = new LinkedHashMap<>();
	}
	public static class Route{
		public Route(List<String>nodes) {
			
		}
	}
	public static class Node{
		int x,y;
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	public void addNode(String id, Node node) {
		nodes.put(id, node);
	}
	public void addNode(String id, int x, int y) {
		nodes.put(id, new Node(x,y));
	}
	public static void main(String args[]) {
		System.out.println(Node.class);
	}
}
