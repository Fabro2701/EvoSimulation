package simulator.events.models;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.events.OneTimeEvent;

public class AddFoodDistributionEvent extends OneTimeEvent{
	private int center;
	private int radius;
	private int amount;
	
	public AddFoodDistributionEvent(int executionTime, int center, int radius, int amount) {
		super(executionTime);
		this.center=center;
		this.radius=radius;
		this.amount=amount;
	}

	@Override
	protected void _execute(Controller ctrl) {
		JSONArray arr = new JSONArray();
		
		for(int i=0;i<amount;i++) {
			int x = RandomSingleton.nextInt(1+radius*2)-radius;
			int y = RandomSingleton.nextInt(1+radius*2)-radius;
			
			JSONObject o = new JSONObject();
			o.put("type", "f");
			JSONObject data = new JSONObject();
			data.put("id", "0");
			data.put("x", center+x);
			data.put("y", center+y);
			o.put("data", data);
			
			arr.put(o);
			
			
		}
		ctrl.loadEntities(arr);
	}

}
