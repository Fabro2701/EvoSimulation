package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.SimpleRandomEntity;
import simulator.model.entity.SimpleUPEntity;

public class SimpleUPEntityBuilder extends SimpleRandomEntityBuilder{
	public SimpleUPEntityBuilder() {
		type="su";
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		return new SimpleUPEntity(o.getString("id"),
									  ctrl.getNodeAt(o.getInt("x"),o.getInt("y"))
									  );
	}
}
