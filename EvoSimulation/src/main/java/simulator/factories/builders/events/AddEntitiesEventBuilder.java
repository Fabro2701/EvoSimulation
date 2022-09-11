package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.events.models.AddEntitiesEvent;


/**
 * AddEntitiesEventBuilder AddEntitiesEvent builder
 * @author fabrizioortega
 *
 */
public class AddEntitiesEventBuilder extends EventBuilder{
	public AddEntitiesEventBuilder() {
		type = Constants.AddEntitiesEventBuilder;
	}
	@Override
	public AddEntitiesEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddEntitiesEvent(o.getInt("time"),o.getJSONArray("entities"));
	}

}
