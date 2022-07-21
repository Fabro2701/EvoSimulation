package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddRandomEntitiesGeneratorEvent;
import simulator.factories.builders.Builder;

public class AddRandomEntitiesGeneratorEventBuilder extends Builder<Event>{
	public AddRandomEntitiesGeneratorEventBuilder() {
		type="reg";
	}
	@Override
	public AddRandomEntitiesGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddRandomEntitiesGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("typeTo"),o.getInt("amount"));
	}

}
