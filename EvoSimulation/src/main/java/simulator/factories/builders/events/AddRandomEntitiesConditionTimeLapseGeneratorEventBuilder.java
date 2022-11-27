package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddRandomEntitiesConditionTimeLapseGeneratorEvent;
import simulator.factories.builders.Builder;

public class AddRandomEntitiesConditionTimeLapseGeneratorEventBuilder extends EventBuilder{
	public AddRandomEntitiesConditionTimeLapseGeneratorEventBuilder() {
		type=simulator.Constants.AddRandomEntitiesConditionTimeLapseGeneratorBuilder_TYPE;
	}
	@Override
	public AddRandomEntitiesConditionTimeLapseGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		if(o.has("init")) {
			return new AddRandomEntitiesConditionTimeLapseGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getInt("timelapse"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),o.getString("init"),o.getString("class"),o.getJSONObject("properties"));
		}
		if(o.has("properties")) {
			return new AddRandomEntitiesConditionTimeLapseGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getInt("timelapse"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),null,null,o.getJSONObject("properties"));
		}
		return new AddRandomEntitiesConditionTimeLapseGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getInt("timelapse"),o.getString("typeTo"),o.getInt("amount"),o.getString("id"),null,null,null);

	}

}
