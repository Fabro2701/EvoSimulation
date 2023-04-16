package state_diagram.product;

import java.util.HashMap;
import java.util.Map;

public class Test {
	Map<String, Object>attributes;
	public Test() {
		this.attributes = new HashMap<>();
	}
	public Object getAttribute(String id) {
		return this.attributes.get(id);
	}
	public void setAttribute(String id, Object value) {
		this.attributes.put(id, value);
	}
}
