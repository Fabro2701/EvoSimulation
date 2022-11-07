package simulator.model;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar;
import simulator.control.ActionsController;
import simulator.control.GlobalController;
import simulator.control.GrammarController;
import simulator.control.InitController;
import simulator.control.InteractionsController;
import simulator.control.SetupController;
import simulator.control.UpdatesController;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.model.optimizer.BasicOptimizer;
import simulator.model.optimizer.Optimizer;
import simulator.model.optimizer.UniformGridOptimizer;

/**
 * EvoSimulator class
 * @author Fabrizio Ortega
 *
 */
public class EvoSimulator {
	private int time;
	private Map map;
	private List<SimulatorObserver> observers;
	private List<Entity> entities;
	private List<Entity> entitiesBuffer;
	
	private Optimizer optimizer;
	
	private long startTime;
	private boolean debug=false;
	private boolean save=false;
	
	private SetupController setupCtrl;
	private GrammarController grammarController;
	private ActionsController actionsController;
	private InteractionsController interactionsController;
	private UpdatesController updatesController;
	private InitController initController;
	private GlobalController globalController;
	
	public EvoSimulator() {
		this("test1000void2");
	}
	/**
	 * 
	 * @param map Map dir
	 */
	public EvoSimulator(String map) {
		this.time = 0;
		this.map = new Map(map);
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<Entity>();
		this.entitiesBuffer = new ArrayList<Entity>();
		
		this.optimizer = new UniformGridOptimizer(this,3,3);
		//this.optimizer = new BasicOptimizer(this);
		
		
		
		//this.commonGrammar = new BiasedGrammar();
//		this.commonGrammar = new StandardGrammar();
//		this.commonGrammar.parseBNF("default");
		
		//this.commonGrammar.calculateAttributes();
		
		
		this.startTime = System.currentTimeMillis();
	}
	public void loadSetup(SetupController setup) {
		setupCtrl = setup;
		this.grammarController = (GrammarController) setupCtrl.getModule("GrammarController");
		this.actionsController = (ActionsController) setupCtrl.getModule("ActionsController");
		this.interactionsController = (InteractionsController) setupCtrl.getModule("InteractionsController");
		this.updatesController = (UpdatesController) setupCtrl.getModule("UpdatesController");
		this.initController = (InitController) setupCtrl.getModule("InitController");
		this.globalController = (GlobalController) setupCtrl.getModule("GlobalController");

	}

	/**
	 * Step the simulator once
	 */
	public void step() {
		time++;
		if(entities.size()==0 && entitiesBuffer.size()==0)return;//for performance
		
		if(debug) {
			if(time%500==0) {
				long currentTime = System.currentTimeMillis();
				System.out.println("-------- Simulation velocity-1: "+(currentTime-this.startTime));
				startTime = currentTime;
				System.out.println("Current time: "+this.time);
			}
		}
		
		//logic delegated to and optimizer
		this.optimizer.update(map, entities, time);
		
		//insert the pending entities
		if(entitiesBuffer.size()>0) {
			this.entities.addAll(entitiesBuffer);
			this.entitiesBuffer.clear();
		}
		
		if(time%300==0) {
//			if(commonGrammar instanceof BiasedGrammar){
//				((BiasedGrammar)commonGrammar).globalUpdate(entities);
//			}
		}
		
		//update observers
		for (SimulatorObserver observer : observers) {
			observer.onUpdate(entities, map, time);
		}
		
		if(save) {
			this.saveSimulation();
			save=false;
		}
	}

	/**
	 * Save simulation in json format
	 */
	private void saveSimulation() {
		String filename = "test";
        JSONObject o = this.toJSON();
        try {
        	//File myObj = new File("resources/loads/simulations/"+filename+".json");myObj.createNewFile();
			PrintWriter out = new PrintWriter(new FileWriter("resources/loads/simulations/"+filename+".json"));
			out.write(o.toString());
			out.close();
			System.out.println("Simulation saved successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Add an {@link SimulatorObserver}
	 * @param observer
	 */
	public void addObserver(SimulatorObserver observer) {
		observers.add(observer);
		observer.onRegister(entities, map, time);
	}

	/**
	 * Add {@link Entity} into the pending to be inserted list
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		entitiesBuffer.add(entity);
	}
	/**
	 * Add {@link Entity}s into the pending to be inserted list
	 * @param entities
	 */
	public void addEntities(List<Entity> entities) {
		entitiesBuffer.addAll(entities);
	}

	/**
	 * Auxliliary method for getting the {@link Node} of a given set of coords
	 * @param x
	 * @param y
	 * @return the {@link Node} with coords (x,y)
	 */
	public Node getNodeAt(int x, int y) {
		return map.getNodeAt(x, y);
	}

	/**
	 * Returns the entity associated with the id
	 * @param id
	 * @return the entity if exists else null
	 */
	public Entity getEntity(String id) {
		for(Entity e:entities) {
			if(e.getId().equals(id))return e;
		}
		for(Entity e:entitiesBuffer) {
			if(e.getId().equals(id))return e;
		}
		return null;
	}
	
	/**
	 * Construct and returns the JSON representation
	 * @return JSON representation
	 */
	public JSONObject toJSON() {
		JSONArray entitiesArr = new JSONArray();
		for (Entity e : entities) {
			if(e.getEnergy()>0.0f)entitiesArr.put(e.toJSON());
		}
		JSONArray grammars = new JSONArray();
//		for(String key:this.commonGrammars.keySet()) {
//			grammars.put(this.commonGrammars.get(key).toString());
//		}
		return new JSONObject().put("time", time)//.put("map", map.toJSON())// ?too heavy
							   .put("entities", entitiesArr)
							   .put("map", map.getFileName())
							   .put("grammars", grammars);
	}
	
	/**
	 * Resets attributes that need a reset in order to be reused 
	 */
	public void reset() {
		this.entities.clear();
		this.entitiesBuffer.clear();
		this.time = 0;
//		for(String key:this.commonGrammars.keySet()) {
//			this.commonGrammars.get(key).reset();
//		}
	}

	
	//GETTERS AND SETTERS
	public void setDebug(boolean b) {
		this.debug=b;
	}
	public Map getMap() {
		return map;
	}
	public int getTime() {
		return time;
	}
	public List<Entity> getEntities(){
		return this.entities;
	}
	public void setSave(boolean save) {
		this.save = save;
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
}
