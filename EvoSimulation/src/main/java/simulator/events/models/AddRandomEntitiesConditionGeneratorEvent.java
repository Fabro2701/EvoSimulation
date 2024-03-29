package simulator.events.models;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.individuals.MyIndividual;
import simulator.model.entity.initializer.AbstractInitializer;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Node;

public class AddRandomEntitiesConditionGeneratorEvent extends MultipleTimeEvent{
	private String typeTo;
	private int amount;
	private String id,init;
	JSONObject properties;
	String clazz;
	public AddRandomEntitiesConditionGeneratorEvent(int executionTime, int times, int interval, String typeTo, int amount, String id, String init, String clazz, JSONObject properties) {
		super(executionTime, times, interval);
		this.typeTo = typeTo;
		this.amount = amount;
		this.id = id;
		this.init = init;
		this.properties = properties;
		this.clazz = clazz;
	}

	@Override
	protected void _execute(Controller ctrl) throws JSONException, EvaluationException {
		long count=0;
		switch(typeTo) {
			case "mi":{
				count = ctrl.getEntities().stream().filter(e->e instanceof MyIndividual).count();
				break;
			}
			case "ae":{
				count = ctrl.getEntities().stream().filter(e->e instanceof ActiveEntity).count();
				break;
			}
			case "pe":{
				count = ctrl.getEntities().stream().filter(e->e instanceof simulator.model.entity.PasiveEntity).count();
				break;
			}
				
		}
		if(init != null){
			
				AbstractInitializer initializer=null;
				
				try {
					initializer = (AbstractInitializer) Class.forName(init).getConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ctrl.loadEntities(initializer.createPopulation(null, ctrl, amount-(int)count, properties,clazz ));
			
		}
		else {
			JSONArray arr = new JSONArray();
			//System.out.println(amount-count);
			for(int i=0;i<amount-count;i++) {
				JSONObject o = new JSONObject();
				o.put("type", typeTo);
				JSONObject data = new JSONObject();
				
				if(id.equals("r"))data.put("id", ctrl.getNextId());
				else data.put("id", this.id);
				
				Node node = ctrl.randomNode();
				data.put("x", node.x);
				data.put("y", node.y);
				data.put("properties", properties);
				
				o.put("data", data);
				arr.put(o);
			}
			ctrl.loadEntities(arr);
		}
		
		
	}

}
