package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddEntitiesEvent;

public class AddEntitiesEventBuilder extends EventBuilder{
	public AddEntitiesEventBuilder() {
		type="ae";
	}
	@Override
	public AddEntitiesEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddEntitiesEvent(o.getInt("time"),o.getJSONArray("entities"));
	}

}
