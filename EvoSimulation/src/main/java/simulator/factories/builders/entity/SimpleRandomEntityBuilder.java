package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.SimpleRandomEntity;

/**
 * MyIndividualBuilder instantiates a single SimpleRandomEntityBuilder
 * @author fabrizioortega
 *
 */
public class SimpleRandomEntityBuilder extends EntityBuilder{
	public SimpleRandomEntityBuilder() {
		type = Constants.SimpleRandomEntityBuilder_TYPE;
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		return new SimpleRandomEntity(o.getString("id"),
									  ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
									  ctrl
									  );
	}

}
