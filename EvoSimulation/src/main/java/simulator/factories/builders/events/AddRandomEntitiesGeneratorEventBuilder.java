package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.events.models.AddRandomEntitiesConditionGeneratorEvent;
import simulator.events.models.AddRandomEntitiesGeneratorEvent;

public class AddRandomEntitiesGeneratorEventBuilder extends EventBuilder{
	public AddRandomEntitiesGeneratorEventBuilder() {
		type = Constants.AddRandomEntitiesGeneratorEventBuilder_TYPE;
	}
	@Override
	public AddRandomEntitiesGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		if(o.has("properties")) {
			return new AddRandomEntitiesGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),o.getJSONObject("properties"));
		}
		return new AddRandomEntitiesGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),null);
	}

}
