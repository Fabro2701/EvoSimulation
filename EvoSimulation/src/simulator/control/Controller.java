package simulator.control;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Node;

public class Controller {
	private EvoSimulator simulator;
	private BuilderBasedFactory factory;
	public Controller(EvoSimulator simulator, BuilderBasedFactory factory) {
		this.simulator = simulator;
		this.factory = factory;
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
	public void addEntity(Entity entity) {
		simulator.addEntity(entity);
		
	}
	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray a = jsonInput.getJSONArray("entities");
		for(int i=0;i<a.length();i++) {
			simulator.addEntity(factory.createInstance(a.getJSONObject(i),this));
		}
	}
	public Node getNodeAt(int x, int y) {
		return simulator.getNodeAt(x,y);
	}
}
