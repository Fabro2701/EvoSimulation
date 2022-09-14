package simulator.control;

import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import grammar.AbstractGrammar;
import grammar.Grammar;
import simulator.RandomSingleton;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import statistics.StatsManager;
import util.IdGenerator;

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
	private StatsManager statsManager;
	private IdGenerator idGenerator;

	public Controller(EvoSimulator simulator, BuilderBasedFactory<Entity> entityFactory, BuilderBasedFactory<Event> eventFactory, EventManager eventManager, StatsManager statsManager) {
		this.simulator = simulator;
		this.entityFactory = entityFactory;
		this.eventFactory = eventFactory;
		this.eventManager = eventManager;
		this.statsManager = statsManager;
		this.idGenerator = new IdGenerator();
	}
	
	/**
	 * Add a {@link SimulatorObserver} to the simulator
	 * @param observer
	 */
	public void addObserver(SimulatorObserver observer) {
		simulator.addObserver(observer);
	}

	/**
	 * Steps the {@link Controller#simulator} and updates {@link Controller#eventManager} and {@link Controller#statsManager} 'runs' times
	 * @param runs Number of steps to be taken
	 */
	public void run(int runs) {
		if (runs <= 0) return;
		for (int i = 0; i < runs; i++) {
			eventManager.update(this, simulator.getTime());
			simulator.step();
			statsManager.onStep(simulator);
		}
	}

	public void addRandomEntity() {
		JSONObject o = new JSONObject().put("type", "mi").put("data",
				new JSONObject()
						//.put("id", String.valueOf(RandomSingleton.nextInt(1000)))
					    .put("id", "-1")
						.put("x", RandomSingleton.nextInt(simulator.getMap().WIDTH))
						.put("y", RandomSingleton.nextInt(simulator.getMap().HEIGHT)));
		simulator.addEntity(entityFactory.createInstance(o, this));
	}

	/**
	 * Create and insert the {@link Entity} found in the stream into the {@link Controller#simulator}
	 * using the {@link Controller#entityFactory}
	 * @param in
	 */
	public void loadEntities(InputStream in) {
		loadEntities(new JSONObject(new JSONTokener(in)).getJSONArray("entities"));
	}

	/**
	 * Create and insert the {@link Entity} found in the JSONArray into the {@link Controller#simulator}
	 * using the {@link Controller#entityFactory}
	 * @param jsonInput
	 */
	public void loadEntities(JSONArray jsonInput) {
		for (int i = 0; i < jsonInput.length(); i++) {
			simulator.addEntity(entityFactory.createInstance(jsonInput.getJSONObject(i), this));
		}
	}
	
	/**
	 * Insert the entities into the {@link Controller#simulator}
	 * @param entities Entities to be insterted
	 */
	public void loadEntities(List<Entity> entities) {
		simulator.addEntities(entities);
	}

	/**
	 * Create and insert the {@link Event}s found in the stream into the {@link Controller#eventManager}
	 * using the {@link Controller#eventFactory}
	 * @param in
	 */
	public void loadEvents(InputStream in) {
		loadEvents(new JSONObject(new JSONTokener(in)).getJSONArray("events"));
	}

	/**
	 * Create and insert the {@link Event}s found in the JSONArray into the {@link Controller#eventManager}
	 * using the {@link Controller#eventFactory}
	 * @param jsonInput
	 */
	public void loadEvents(JSONArray jsonInput) {
		for (int i = 0; i < jsonInput.length(); i++) {
			eventManager.addEvent(eventFactory.createInstance(jsonInput.getJSONObject(i), this));
		}
	}

	/**
	 * Add a single {@link Event} into the {@link Controller#eventManager} using the {@link Controller#eventFactory}
	 * @param in
	 */
	public void addEvent(InputStream in) {
		eventManager.addEvent(eventFactory.createInstance(new JSONObject(new JSONTokener(in)), this));
	}
	
	/**
	 * Auxliliary method for getting the {@link Node} of a given set of coords
	 * @param x
	 * @param y
	 * @return the {@link Node} with coords (x,y)
	 */
	public Node getNodeAt(int x, int y) {
		return simulator.getNodeAt(x, y);
	}

	/**
	 * 
	 * @return A valid random {@link Node}
	 */
	public Node randomNode() {
		return simulator.getMap().getRandomNode();
	}
	
	/**
	 * 
	 * @return The next id provided by {@link Controller#idGenerator}
	 */
	public String getNextId() {
		return idGenerator.getNextId();
	}
	
	/**
	 * 
	 * @return {@link Controller#simulator} JSON
	 */
	public JSONObject getSimulatorJSON() {
		return simulator.toJSON();
	}
	
	/**
	 * Resets dynamic elements like {@link Controller#simulator} {@link Controller#eventManager} {@link Controller#idGenerator}
	 * in order to reuse the {@link Controller} object without having to reload unnecesary attributes
	 */
	public void reset() {
		this.simulator.reset();
		this.eventManager.reset();
		this.idGenerator = new IdGenerator();
	}
	
	//GETTERS AND SETTERS
	public Map getMap() {
		return simulator.getMap();
	}
	public StatsManager getStatsManager() {
		return statsManager;
	}
	public List<Entity>getEntities(){
		return this.simulator.getEntities();
	}
	public AbstractGrammar getCommonGrammar() {
		return simulator.getCommonGrammar();
	}
	public void setCommonGrammar(AbstractGrammar commonGrammar) {
		simulator.setCommonGrammar(commonGrammar);
	}
	public AbstractGrammar getCommonGrammar2() {
		return simulator.getCommonGrammar2();
	}
}
