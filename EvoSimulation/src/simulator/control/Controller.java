package simulator.control;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.RandomSingleton;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Node;

public class Controller {
	private EvoSimulator simulator;
	private BuilderBasedFactory<Entity> entityFactory;
	public Controller(EvoSimulator simulator, BuilderBasedFactory<Entity> entityFactory) {
		this.simulator = simulator;
		this.entityFactory = entityFactory;
	}
	public void addObserver(SimulatorObserver observer) {
		simulator.addObserver(observer);
		
	}
	public void run(int runs) {
		if(runs<=0)return;
		
		for(int i=0;i<runs;i++) {
			simulator.update();

		}
	}
	public void addRandomEntity() {
		JSONObject o = new JSONObject().put("type","su")
									   .put("data",new JSONObject().put("id",String.valueOf(RandomSingleton.nextInt(1000)))
											   					   .put("x",RandomSingleton.nextInt(simulator.getMap().WIDTH))
											   					   .put("y",RandomSingleton.nextInt(simulator.getMap().HEIGHT))
											)
									   ;
		simulator.addEntity(entityFactory.createInstance(o,this));
		
	}
	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray a = jsonInput.getJSONArray("entities");
		for(int i=0;i<a.length();i++) {
			simulator.addEntity(entityFactory.createInstance(a.getJSONObject(i),this));
		}
	}
	public Node getNodeAt(int x, int y) {
		return simulator.getNodeAt(x,y);
	}
}
