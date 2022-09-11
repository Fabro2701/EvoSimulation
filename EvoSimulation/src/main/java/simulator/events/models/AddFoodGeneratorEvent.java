package simulator.events.models;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;

public class AddFoodGeneratorEvent extends MultipleTimeEvent{
	private int centerX;
	private int centerY;
	private int radiusX;
	private int radiusY;
	private int amount;
	public AddFoodGeneratorEvent(int executionTime, int times, int interval, int centerX, int centerY, int radiusX, int radiusY, int amount) {
		super(executionTime, times, interval);
		this.centerX=centerX;
		this.centerY=centerY;
		this.radiusX=radiusX;
		this.radiusY=radiusY;
		this.amount=amount;
	}

	@Override
	protected void _execute(Controller ctrl) {
		
		if(radiusX==-1) {
			centerX = ctrl.getMap().WIDTH/2;
			radiusX = ctrl.getMap().WIDTH/2 -1;
		}
		if(radiusY==-1) {
			centerY = ctrl.getMap().HEIGHT/2;
			radiusY = ctrl.getMap().HEIGHT/2 -1;
		}
		JSONArray arr = new JSONArray();
		
		for(int i=0;i<amount;i++) {
			int x = RandomSingleton.nextInt(1+radiusX*2)-radiusX;
			int y = RandomSingleton.nextInt(1+radiusY*2)-radiusY;
			
			JSONObject o = new JSONObject();
			o.put("type", "f");
			JSONObject data = new JSONObject();
			data.put("id", "0");
			data.put("x", centerX+x);
			data.put("y", centerY+y);
			o.put("data", data);
			
			arr.put(o);
			
			
		}
		ctrl.loadEntities(arr);
	
	}

}
