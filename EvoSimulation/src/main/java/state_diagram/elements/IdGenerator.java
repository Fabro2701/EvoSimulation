package state_diagram.elements;

public class IdGenerator {
	private static int current = 0;
	public static int nextId() {
		return current++;
	}
	public static void update(int id) {
		current = Math.max(current, id+1);
	}
}
