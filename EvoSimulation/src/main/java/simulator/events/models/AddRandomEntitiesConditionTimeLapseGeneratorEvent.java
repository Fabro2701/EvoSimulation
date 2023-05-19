package simulator.events.models;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import simulator.events.TimeLapseEvent;
import simulator.model.entity.individuals.MyIndividual;
import simulator.model.entity.initializer.AbstractInitializer;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Node;

public class AddRandomEntitiesConditionTimeLapseGeneratorEvent extends TimeLapseEvent{
	private String typeTo;
	private int amount;
	private String id,init;
	JSONObject properties;
	String clazz;
	public AddRandomEntitiesConditionTimeLapseGeneratorEvent(int executionTime, int times, int interval, int timelapse, String typeTo, int amount, String id, String init, String clazz, JSONObject properties) {
		super(executionTime, times, interval, timelapse);
		this.typeTo = typeTo;
		this.amount = amount;
		this.id = id;
		this.init = init;
		this.properties = properties;
		this.clazz = clazz;
	}

	@Override
	protected void _execute(Controller ctrl) throws JSONException, EvaluationException {
		//System.out.println("exec");
		long count = ctrl.getEntities().stream().filter(e->e instanceof MyIndividual).count();
		if(count>amount)return;
		if(init != null){//depre
			
				AbstractInitializer initializer=null;
				
				try {
					initializer = (AbstractInitializer) Class.forName(init).getConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ctrl.loadEntities(initializer.createPopulation(ctrl, amount-(int)count, properties,clazz ));
			
		}
		else {
			JSONArray arr = new JSONArray();
			long amn = (amount-count)/this.cont;
			for(int i=0;i<amn/timelapse;i++) {
				JSONObject o = new JSONObject();
				o.put("type", typeTo);
				JSONObject data = new JSONObject();
				
				//if(id.equals("r"))data.put("id", String.valueOf(RandomSingleton.nextInt(1000)));
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
