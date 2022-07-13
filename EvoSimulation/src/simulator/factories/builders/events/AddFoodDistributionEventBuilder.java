package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.AddFoodDistributionEvent;
import simulator.factories.builders.Builder;

public class AddFoodDistributionEventBuilder extends Builder<Event>{
	public AddFoodDistributionEventBuilder(){
		type="fe";
	}
	@Override
	public AddFoodDistributionEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddFoodDistributionEvent(o.getInt("time"),o.getInt("center"),o.getInt("radius"),o.getInt("amount"));
	}

}
