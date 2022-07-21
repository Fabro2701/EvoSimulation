package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.builders.Builder;
import simulator.model.entity.Entity;

public abstract class EntityBuilder extends Builder<Entity>{

	@Override
	public abstract Entity createTheInstance(JSONObject o, Controller ctrl);

}
