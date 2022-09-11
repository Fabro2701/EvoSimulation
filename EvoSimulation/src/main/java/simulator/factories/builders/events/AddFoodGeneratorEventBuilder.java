package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.events.models.AddFoodGeneratorEvent;

public class AddFoodGeneratorEventBuilder extends EventBuilder{
	public AddFoodGeneratorEventBuilder(){
		type = Constants.AddFoodGeneratorEventBuilder_TYPE;
	}
	@Override
	public AddFoodGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddFoodGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getInt("centerX"),o.getInt("centerY"),o.getInt("radiusX"),o.getInt("radiusY"),o.getInt("amount"));
	}
}
