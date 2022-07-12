package simulator.factories;


import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;

public interface Factory<T> {
	public T createInstance(JSONObject info, Controller controller);
}
