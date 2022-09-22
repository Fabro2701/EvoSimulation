package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar;
import grammar.BiasedGrammar;
import grammar.Grammar;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.model.optimizer.BasicOptimizer;
import simulator.model.optimizer.Optimizer;

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
	
	private AbstractGrammar commonGrammar;
	private AbstractGrammar commonGrammar2;

	private long startTime;
	private boolean debug=false;
	
	
	public EvoSimulator() {
		this("test1000");
	}
	/**
	 * 
	 * @param map Map direction
	 */
	public EvoSimulator(String map) {
		this.time = 0;
		this.map = new Map(map);
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<Entity>();
		this.entitiesBuffer = new ArrayList<Entity>();
		
		//this.optimizer = new UniformGridOptimizer(this,3,3);
		this.optimizer = new BasicOptimizer(this);
		
		//this.commonGrammar = new BiasedGrammar();
		this.commonGrammar = new Grammar();
		this.commonGrammar.parseBNF("default");
		
		//this.commonGrammar.calculateAttributes();
		this.commonGrammar2 = new Grammar();
		this.commonGrammar2.parseBNF("default2");
		
		this.startTime = System.currentTimeMillis();
	}
	
	/**
	 * Step the simulator once
	 */
	public void step() {
		time++;
		if(entities.size()==0 && entitiesBuffer.size()==0)return;//for performance
		
		if(debug) {
			if(time%100==0) {
				long currentTime = System.currentTimeMillis();
				System.out.println("-------- Simulation velocity-1: "+(currentTime-this.startTime));
				startTime = currentTime;
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
			if(commonGrammar instanceof BiasedGrammar){
				((BiasedGrammar)commonGrammar).globalUpdate(entities);
			}
		}
		
		//update observers
		for (SimulatorObserver observer : observers) {
			observer.onUpdate(entities, map, time);
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
		return new JSONObject().put("time", time)//.put("map", map.toJSON())// ?too heavy
							   .put("entities", entitiesArr)
							   .put("commonGrammar", commonGrammar.toString())
							   .put("map", map.getFileName());
	}
	
	/**
	 * Resets attributes that need a reset in order to be reused 
	 */
	public void reset() {
		this.entities.clear();
		this.entitiesBuffer.clear();
		this.time = 0;
		this.commonGrammar.reset();
	}
	
	//GETTERS AND SETTERS
	public void setDebug(boolean b) {
		this.debug=b;
	}
	public AbstractGrammar getCommonGrammar() {
		return commonGrammar;
	}
	public AbstractGrammar getCommonGrammar2() {
		return commonGrammar2;
	}
	public void setCommonGrammar(AbstractGrammar commonGrammar) {
		this.commonGrammar = commonGrammar;
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
}
