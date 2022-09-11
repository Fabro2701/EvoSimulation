package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.SimpleUPEntity;

/**
 * MyIndividualBuilder instantiates a single SimpleUPEntityBuilder
 * @author fabrizioortega
 *
 */
public class SimpleUPEntityBuilder extends SimpleRandomEntityBuilder{
	public SimpleUPEntityBuilder() {
		type = Constants.SimpleUPEntityBuilder;
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		return new SimpleUPEntity(o.getString("id"),
									  ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),ctrl
									  );
	}
}
