package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.events.models.AddFoodDistributionEvent;

public class AddFoodDistributionEventBuilder extends EventBuilder{
	public AddFoodDistributionEventBuilder(){
		type = Constants.AddFoodDistributionEventBuilder_TYPE;
	}
	@Override
	public AddFoodDistributionEvent createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new AddFoodDistributionEvent(o.getInt("time"),o.getInt("center"),o.getInt("radius"),o.getInt("amount"));
	}

}
