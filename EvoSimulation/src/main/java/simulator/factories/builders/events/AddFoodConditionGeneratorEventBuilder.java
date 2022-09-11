package simulator.factories.builders.events;

import static simulator.Constants.AddFoodConditionGeneratorBuilder_TYPE;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddFoodConditionGeneratorEvent;
import simulator.factories.builders.Builder;

public class AddFoodConditionGeneratorEventBuilder extends EventBuilder{
	public AddFoodConditionGeneratorEventBuilder(){
		type=AddFoodConditionGeneratorBuilder_TYPE;
	}
	@Override
	public AddFoodConditionGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddFoodConditionGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getFloat("factor"));
	}
}
