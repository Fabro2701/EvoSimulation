package simulator.model.evaluation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Environment {
	Environment parent;
	Map<String, Object> record;
	
	public Environment(Environment parent) {
		this.parent = parent;
		record = new HashMap<String, Object>();
	}
	public Object search(String key) {
		return resolve(key).record.get(key);
	}
	private Environment resolve(String key) {
		Objects.requireNonNull(key);
		
		if(this.record.containsKey(key)) {
			return this;
		}
		else {
			return parent==null?null:parent.resolve(key);
		}
	}
	public Object assign(String key, Object v) {
		return resolve(key).record.put(key, v);
	}
	public Object define(String key, Object v) {
		this.record.put(key, v);
		return v;
	}
	public void clear() {
		this.record.clear();
	}
	@Override
	public String toString() {
		return record.toString();
	}
}
