package simulator.events.models;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import simulator.model.entity.Entity;
import simulator.model.entity.FoodEntity;

/**
 * AddFoodConditionGeneratorEvent creates an certain amount of food 
 * depending on the number of entities already present
 * @author fabrizioortega
 *
 */
public class AddFoodConditionGeneratorEvent extends MultipleTimeEvent{
	private float factor;
	/**
	 * AddFoodConditionGeneratorEvent constructor
	 * @param executionTime
	 * @param times
	 * @param interval
	 * @param factor amountOfFood = factor*entities
	 */
	public AddFoodConditionGeneratorEvent(int executionTime, int times, int interval, float factor) {
		super(executionTime, times, interval);
		this.factor = factor;
	}

	@Override
	protected void _execute(Controller ctrl) {
		//get information from ctrl
		List<Entity>entities = ctrl.getEntities();
		long entitiesCount = entities.stream().filter(e ->!(e instanceof FoodEntity)).count();
		
		float foodAmount = ((float)entitiesCount)*factor - ((float)entities.size()-entitiesCount);
						   // foodAmountObjetive - foodAlreadyPresent
		
		JSONArray arr = new JSONArray();
		

		int centerX = ctrl.getMap().WIDTH/2;
		int centerY= ctrl.getMap().WIDTH/2;
		int radiusX = ctrl.getMap().WIDTH/2 -1;
		int radiusY = ctrl.getMap().HEIGHT/2 -1;
		for(int i=0;i<foodAmount;i++) {
			int x = RandomSingleton.nextInt(1+radiusX*2)-radiusX;
			int y = RandomSingleton.nextInt(1+radiusY*2)-radiusY;
			
			//we create a JSONObject, ctrl will instantiate the real object
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
