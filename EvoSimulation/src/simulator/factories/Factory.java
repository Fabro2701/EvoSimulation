package simulator.factories;


import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;

public interface Factory {
	public Entity createInstance(JSONObject info, Controller controller);
}
