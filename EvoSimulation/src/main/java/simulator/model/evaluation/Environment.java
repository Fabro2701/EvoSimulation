package simulator.model.evaluation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Environment {
	Environment parent;
	Map<String, Object> record;
	
	public Environment(Environment parent) {
		this.parent = parent;
		record = new LinkedHashMap<String, Object>();
	}
	public Object search(String key)  throws EvaluationException{
		Objects.requireNonNull(key);
		return resolve(key).record.get(key);
	}
	private Environment resolve(String key) throws EvaluationException{
		
		if(this.record.containsKey(key)) {
			return this;
		}
		else {
			if(parent==null) {
				throw new EvaluationException(key+" not found");
			}
			else {
				return parent.resolve(key);
			}
		}
	}
	public Object assign(String key, Object v)  throws EvaluationException{
		return resolve(key).record.put(key, v);
	}
	public Object define(String key, Object v) {
		if(this.record.containsKey(key))System.err.println("Variable "+key+" already exists");
		this.record.put(key, v);
		return v;
	}
	public Map<String, Object> getRecord(){
		return record;
	}
	public void clear() {
		this.record.clear();
	}
	@Override
	public String toString() {
		return record.toString();
	}
}
