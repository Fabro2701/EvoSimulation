package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar;
import grammar.BiasedGrammar;
import grammar.Grammar;
import grammar.IntronGrammar;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.model.optimizer.Optimizer;
import simulator.model.optimizer.UniformGridOptimizer;

public class EvoSimulator {
	private Map map;
	private List<SimulatorObserver> observers;
	private List<Entity> entities;
	private List<Entity> entitiesBuffer;
	private int time;
	long startTime;
	boolean debug=false;
	Optimizer optimizer;
	AbstractGrammar commonGrammar;
	AbstractGrammar commonGrammar2;

	public EvoSimulator() {
		this("test1000");
	}
	public EvoSimulator(String map) {
		this.time = 0;
		this.map = new Map(map);
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<Entity>();
		this.entitiesBuffer = new ArrayList<Entity>();
		startTime=System.currentTimeMillis();
		this.optimizer = new UniformGridOptimizer(this,3,3);
		//this.optimizer = new BasicOptimizer(this);
		
		this.commonGrammar = new BiasedGrammar();
		this.commonGrammar.parseBNF("defaultBias");
		
		this.commonGrammar.calculateAttributes();
		this.commonGrammar2 = new IntronGrammar();
	}
	
	int max=500;
	public void step() {
		time++;
		if(entities.size()==0&&entitiesBuffer.size()==0)return;
		if(debug) {
			if(time%100==0) {
				long currentTime = System.currentTimeMillis();
				System.out.println("-------- Simulation velocity-1: "+(currentTime-this.startTime));
				startTime = currentTime;
			}
			
			if(time%1000==0) {
				System.out.println(time);
				for(int i=0;i<entities.size();i++) {
					if(entities.get(i).getAge()>max) {
						max=entities.get(i).getAge();
						System.out.println("new max "+max);
						System.out.println("id "+entities.get(i).getId());
						System.out.println(((GIndividual)entities.get(i)).getPhenotype().getVisualCode());
					}
				}
			}
		}
		
		//logic delegated to and optimizer
		this.optimizer.update(map, entities, time);
		
		if(entitiesBuffer.size()>0) {
			this.entities.addAll(entitiesBuffer);
			this.entitiesBuffer.clear();
		}
		
		
		if(time%300==0) {
			((BiasedGrammar)commonGrammar).globalUpdate(entities);
		}
		
		for (SimulatorObserver observer : observers) {
			observer.onUpdate(entities, map, time);
		}
		
		
	}

	public void addObserver(SimulatorObserver observer) {
		observers.add(observer);
		observer.onRegister(entities, map, time);
	}

	public void addEntity(Entity entity) {
		entitiesBuffer.add(entity);
	}
	public void addEntities(List<Entity> entities) {
		entitiesBuffer.addAll(entities);
	}

	public Node getNodeAt(int x, int y) {
		return map.getNodeAt(x, y);
	}

	public Map getMap() {
		return map;
	}

	public int getTime() {
		return time;
	}
	public Entity getEntity(String id) {
		for(Entity e:entities) {
			if(e.getId().equals(id))return e;
		}
		for(Entity e:entitiesBuffer) {
			if(e.getId().equals(id))return e;
		}
		return null;
	}
	public List<Entity> getEntities(){
		return this.entities;
	}
	public JSONObject toJSON() {
		JSONArray entitiesArr = new JSONArray();
		for (Entity e : entities) {
			if(e.getEnergy()>0.0f)entitiesArr.put(e.toJSON());
		}
		return new JSONObject().put("time", time)//.put("map", map.toJSON())// ?too heavy
							   .put("entities", entitiesArr)
							   .put("commonGrammar", commonGrammar.toString());
	}
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
	public void reset() {
		this.entities.clear();
		this.entitiesBuffer.clear();
		this.time = 0;
		this.commonGrammar = new BiasedGrammar();
		this.commonGrammar.parseBNF("defaultBias");
	}
}
