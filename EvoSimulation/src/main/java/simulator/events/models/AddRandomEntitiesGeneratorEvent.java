package simulator.events.models;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;

public class AddRandomEntitiesGeneratorEvent extends MultipleTimeEvent{
	private String typeTo;
	private int amount;
	private String id;
	JSONObject properties;
	public AddRandomEntitiesGeneratorEvent(int executionTime, int times, int interval, String typeTo, int amount, String id, JSONObject properties) {
		super(executionTime, times, interval);
		this.typeTo = typeTo;
		this.amount = amount;
		this.id = id;
		this.properties = properties;
	}

	@Override
	protected void _execute(Controller ctrl) {
		JSONArray arr = new JSONArray();
		for(int i=0;i<amount;i++) {
			JSONObject o = new JSONObject();
			o.put("type", typeTo);
			JSONObject data = new JSONObject();
			
			//if(id.equals("r"))data.put("id", String.valueOf(RandomSingleton.nextInt(1000)));
			if(id.equals("r"))data.put("id", ctrl.getNextId());
			else data.put("id", this.id);
			
			data.put("x", RandomSingleton.nextInt(ctrl.getMap().WIDTH));
			data.put("y", RandomSingleton.nextInt(ctrl.getMap().HEIGHT));
//			data.put("x", 100);
//			data.put("y", 100);
			data.put("properties", properties);
			o.put("data", data);
			arr.put(o);
		}
		ctrl.loadEntities(arr);
		
		
	}

}
