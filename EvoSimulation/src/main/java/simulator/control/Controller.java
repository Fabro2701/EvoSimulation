package simulator.control;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import grammar.AbstractGrammar;
import grammar.StandardGrammar;
import simulator.RandomSingleton;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.model.map.creator.EntityPanel.EntityInfo;
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
	private List<StatsManager> statsManagers;//multiple models possible
	private IdGenerator idGenerator;
	private ImageController imgController;
	
	private SetupController setupCtrl;
	private GrammarController grammarController;
	private ActionsController actionsController;
	private InteractionsController interactionsController;
	private UpdatesController updatesController;
	private InitController initController;
	private GlobalController globalController;

	public Controller(EvoSimulator simulator, BuilderBasedFactory<Entity> entityFactory, BuilderBasedFactory<Event> eventFactory, EventManager eventManager, StatsManager statsManager) {
		this.simulator = simulator;
		this.entityFactory = entityFactory;
		this.eventFactory = eventFactory;
		
		this.eventManager = eventManager;
		
		this.statsManagers = new ArrayList<>();
		if(statsManager!=null)this.statsManagers.add(statsManager);
		
		this.idGenerator = new IdGenerator();
		this.imgController = new ImageController();
	}
	public void saveSimulation() {
		this.simulator.setSave(true);
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
	 * @throws EvaluationException 
	 * @throws IllegalArgumentException 
	 */
	public void run(int runs) throws IllegalArgumentException, EvaluationException {
		if (runs <= 0) return;
		for (int i = 0; i < runs; i++) {
			if(eventManager!=null)eventManager.update(this, simulator.getTime());
			simulator.step();
			for(StatsManager sm:this.statsManagers)sm.onStep(simulator);
		}
	}

	public void addRandomEntity() throws JSONException, EvaluationException {
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
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public void loadEntities(InputStream in) throws JSONException, EvaluationException {
		loadEntities(new JSONObject(new JSONTokener(in)).getJSONArray("entities"));
	}

	/**
	 * Create and insert the {@link Entity} found in the JSONArray into the {@link Controller#simulator}
	 * using the {@link Controller#entityFactory}
	 * @param jsonInput
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public void loadEntities(JSONArray jsonInput) throws JSONException, EvaluationException {
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
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public void loadEvents(InputStream in) throws JSONException, EvaluationException {
		loadEvents(new JSONObject(new JSONTokener(in)).getJSONArray("events"));
	}

	/**
	 * Create and insert the {@link Event}s found in the JSONArray into the {@link Controller#eventManager}
	 * using the {@link Controller#eventFactory}
	 * @param jsonInput
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public void loadEvents(JSONArray jsonInput) throws JSONException, EvaluationException {
		for (int i = 0; i < jsonInput.length(); i++) {
			eventManager.addEvent(eventFactory.createInstance(jsonInput.getJSONObject(i), this));
		}
	}

	/**
	 * Add a single {@link Event} into the {@link Controller#eventManager} using the {@link Controller#eventFactory}
	 * @param in
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public void addEvent(InputStream in) throws JSONException, EvaluationException {
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
	
	public void loadSetup(SetupController setup) {
		this.setupCtrl = setup;
		this.grammarController = (GrammarController) setupCtrl.getModule("GrammarController");
		this.grammarController =  Objects.requireNonNullElse(this.grammarController, new GrammarController());
		this.actionsController = (ActionsController) setupCtrl.getModule("ActionsController");
		this.actionsController =  Objects.requireNonNullElse(this.actionsController, new ActionsController());
		this.interactionsController = (InteractionsController) setupCtrl.getModule("InteractionsController");
		this.interactionsController =  Objects.requireNonNullElse(this.interactionsController, new InteractionsController());
		this.updatesController = (UpdatesController) setupCtrl.getModule("UpdatesController");
		this.updatesController =  Objects.requireNonNullElse(this.updatesController, new UpdatesController());
		this.initController = (InitController) setupCtrl.getModule("InitController");
		this.initController =  Objects.requireNonNullElse(this.initController, new InitController());
		this.globalController = (GlobalController) setupCtrl.getModule("GlobalController");
		this.globalController =  Objects.requireNonNullElse(this.globalController, new GlobalController());
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
	public void loadPE(List<EntityInfo> entitiesInfo) {
		for(EntityInfo ei:entitiesInfo) {
			String type = ei.getName();
			PasiveEntity pe=null;
			try {
				pe = new PasiveEntity(getNextId(),getNodeAt(ei.getX(),ei.getY()),this,ei.getCode());
			} catch (JSONException | EvaluationException e) {
				System.err.println("Error initializing PEs");
				e.printStackTrace();
			}
			
			pe.setAttribute(type,true);
			pe.setAttribute("info",type);
			pe.setImg(ImageController.getImage(type));
			
			/*String code = ei.getCode();
			if(code.length()>0)pe.apply(code);*/
			
			this.simulator.addEntity(pe);
		}
		
	}
	
	//GETTERS AND SETTERS
	public Map getMap() {
		return simulator.getMap();
	}
	public List<StatsManager> getStatsManagers() {
		return this.statsManagers;
	}
	public void addStatsManager(StatsManager m) {
		this.statsManagers.add(m);
	}
	public List<Entity>getEntities(){
		return this.simulator.getEntities();
	}

	public ImageController getImgController() {
		return imgController;
	}

	public void setImgController(ImageController imgController) {
		this.imgController = imgController;
	}
	public SetupController getSetupCtrl() {
		return setupCtrl;
	}
	public ActionsController getActionsController() {
		return actionsController;
	}
	public GrammarController getGrammarController() {
		return grammarController;
	}
	public InteractionsController getInteractionsController() {
		return interactionsController;
	}
	public UpdatesController getUpdatesController() {
		return updatesController;
	}
	public InitController getInitController() {
		return initController;
	}
	public EvoSimulator getSimulator() {
		return simulator;
	}
	public EventManager getEventManager() {
		return eventManager;
	}
}
