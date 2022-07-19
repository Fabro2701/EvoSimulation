package simulator.control;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.RandomSingleton;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Node;

/**
 * Controller class
 * 
 * @author fabrizioortega
 * 
 */
public class Controller {
	private EvoSimulator simulator;
	private BuilderBasedFactory<Entity> entityFactory;
	private BuilderBasedFactory<Event> eventFactory;
	private EventManager eventManager;

	public Controller(EvoSimulator simulator, BuilderBasedFactory<Entity> entityFactory,
			BuilderBasedFactory<Event> eventFactory, EventManager eventManager) {
		this.simulator = simulator;
		this.entityFactory = entityFactory;
		this.eventFactory = eventFactory;
		this.eventManager = eventManager;
	}

	public void addObserver(SimulatorObserver observer) {
		simulator.addObserver(observer);

	}

	public void run(int runs) {
		if (runs <= 0)
			return;

		for (int i = 0; i < runs; i++) {
			eventManager.update(this, simulator.getTime());
			simulator.update();

		}
	}

	public void addRandomEntity() {
		JSONObject o = new JSONObject().put("type", "su").put("data",
				new JSONObject().put("id", String.valueOf(RandomSingleton.nextInt(1000)))
						.put("x", RandomSingleton.nextInt(simulator.getMap().WIDTH))
						.put("y", RandomSingleton.nextInt(simulator.getMap().HEIGHT)));
		simulator.addEntity(entityFactory.createInstance(o, this));

	}

	public void loadEntities(InputStream in) {
		loadEntities(new JSONObject(new JSONTokener(in)).getJSONArray("entities"));
	}

	public void loadEntities(JSONArray jsonInput) {
		for (int i = 0; i < jsonInput.length(); i++) {
			simulator.addEntity(entityFactory.createInstance(jsonInput.getJSONObject(i), this));
		}
	}

	public void loadEvents(InputStream in) {
		loadEvents(new JSONObject(new JSONTokener(in)).getJSONArray("events"));
	}

	public void loadEvents(JSONArray jsonInput) {
		for (int i = 0; i < jsonInput.length(); i++) {
			eventManager.addEvent(eventFactory.createInstance(jsonInput.getJSONObject(i), this));
		}
	}

	public Node getNodeAt(int x, int y) {
		return simulator.getNodeAt(x, y);
	}

	public void addEvent(InputStream in) {
		eventManager.addEvent(eventFactory.createInstance(new JSONObject(new JSONTokener(in)), this));
	}

}
