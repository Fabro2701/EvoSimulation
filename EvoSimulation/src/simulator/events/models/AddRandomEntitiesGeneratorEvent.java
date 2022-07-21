package simulator.events.models;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;

public class AddRandomEntitiesGeneratorEvent extends MultipleTimeEvent{
	private String typeTo;
	private int amount;
	public AddRandomEntitiesGeneratorEvent(int executionTime, int times, int interval, String typeTo, int amount) {
		super(executionTime, times, interval);
		this.typeTo = typeTo;
		this.amount = amount;
	}

	@Override
	public void execute(Controller ctrl) {
		JSONArray arr = new JSONArray();
		for(int i=0;i<amount;i++) {
			JSONObject o = new JSONObject();
			o.put("type", typeTo);
			JSONObject data = new JSONObject();
			data.put("id", String.valueOf(RandomSingleton.nextInt(1000)));
			data.put("x", RandomSingleton.nextInt(500));
			data.put("y", RandomSingleton.nextInt(500));
			o.put("data", data);
			arr.put(o);
		}
		ctrl.loadEntities(arr);
		
		if(times>0) {
			executionTime = executionTime+interval;
			times--;
		}
		else executionTime=-1;
	}

}
