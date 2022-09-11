package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddRandomEntitiesConditionGeneratorEvent;
import simulator.events.models.AddRandomEntitiesGeneratorEvent;
import simulator.factories.builders.Builder;

public class AddRandomEntitiesConditionGeneratorEventBuilder extends EventBuilder{
	public AddRandomEntitiesConditionGeneratorEventBuilder() {
		type=simulator.Constants.AddRandomEntitiesConditionGeneratorBuilder_TYPE;
	}
	@Override
	public AddRandomEntitiesConditionGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		if(o.has("init")) {
			return new AddRandomEntitiesConditionGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),o.getString("init"),o.getString("class"),o.getJSONObject("properties"));
		}
		return new AddRandomEntitiesConditionGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),null,null,null);
	}

}
