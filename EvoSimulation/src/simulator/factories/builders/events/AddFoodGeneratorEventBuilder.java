package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddFoodGeneratorEvent;
import simulator.factories.builders.Builder;

public class AddFoodGeneratorEventBuilder extends Builder<Event>{
	public AddFoodGeneratorEventBuilder(){
		type="fg";
	}
	@Override
	public AddFoodGeneratorEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddFoodGeneratorEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getInt("centerX"),o.getInt("centerY"),o.getInt("radiusX"),o.getInt("radiusY"),o.getInt("amount"));
	}
}
