package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.SimpleRandomEntity;

public class SimpleRandomEntityBuilder extends EntityBuilder{
	public SimpleRandomEntityBuilder() {
		type="sr";
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		return new SimpleRandomEntity(o.getString("id"),
									  ctrl.getNodeAt(o.getInt("x"),o.getInt("y"))
									  );
	}

}
