package simulator.model;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import experiment.models.metro.MetroViewElements;
import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.model.optimizer.BasicOptimizer;
import simulator.model.optimizer.Optimizer;
import simulator.view.viewer.ViewElement;
import simulator.view.viewer.ViewElementsController;

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
	
	private List<ViewElementsController>viewElementsCtrls;
	protected java.util.Map<Object,ViewElement>viewElements;
	
	private int imgRefreshRate = 1;
	private long delay = 10;
	
	public EvoSimulator() throws IllegalArgumentException, IOException {
		this("test1000void2");
		this.setOptimizer(new BasicOptimizer(this));
	}
	/**
	 * 
	 * @param map Map dir
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	public EvoSimulator(String map) throws IllegalArgumentException, IOException {		
		this.time = 0;
		this.map = new Map(map);
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<Entity>();
		this.entitiesBuffer = new ArrayList<Entity>();
		
		viewElements = new java.util.HashMap<>();
		viewElementsCtrls = new ArrayList<>();
		
		//this.commonGrammar = new BiasedGrammar();
//		this.commonGrammar = new StandardGrammar();
//		this.commonGrammar.parseBNF("default");
		
		//this.commonGrammar.calculateAttributes();
		//viewElementsCtrls.add(new MetroViewElements(viewElements));
		
		this.startTime = System.currentTimeMillis();
	}
	

	/**
	 * Step the simulator once
	 * @throws EvaluationException 
	 * @throws IllegalArgumentException 
	 */
	public void step() throws IllegalArgumentException, EvaluationException {
		//System.out.println(entities.size());
		if(this.delay>0) {
			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		time++;
		if(entities.size()==0 && entitiesBuffer.size()==0) {
			for (SimulatorObserver observer : observers) {
				observer.onUpdate(entities, map, time, this.viewElements);
			}
			return;//for performance
		}
		
		if(debug) {
			if(time%500==0) {
				long currentTime = System.currentTimeMillis();
				System.out.println("-------- Simulation velocity-1: "+(currentTime-this.startTime));
				startTime = currentTime;
				System.out.println("Current time: "+this.time);
			}
		}
		
		//logic delegated to an optimizer
		if(!entities.isEmpty())this.optimizer.update(map, entities, time-1);
		
		//insert the pending entities
		if(entitiesBuffer.size()>0) {
			this.entities.addAll(entitiesBuffer);
			this.entitiesBuffer.clear();
		}
		
		
		
		//update observers
		if(time%this.imgRefreshRate==0) {
			//float[][]grad = new float[][] {{0,0,1},{0,1,1},{0,1,0},{1,1,0},{1,0,0}};
			for(ViewElementsController vec:this.viewElementsCtrls) {
				vec.produce(entities, map);
			}
			for (SimulatorObserver observer : observers) {
				observer.onUpdate(entities, map, time, this.viewElements);
			}
			this.viewElements.clear();
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

	public void addViewElementsController(String classname) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, ClassNotFoundException {
		this.viewElementsCtrls.add(((ViewElementsController)Class.forName(classname).getConstructors()[0].newInstance(this.viewElements)));
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
			if(e.isAlive())entitiesArr.put(e.toJSON());
		}
		
		

		return new JSONObject().put("time", time)
							   .put("entities", entitiesArr)
							   .put("map", map.getFileName());//.put("map", map.toJSON())// ?too heavy
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
	
	public void setOptimizer(Optimizer optimizer) {
		this.optimizer = optimizer;
	}
	public void setImgRefreshRate(int imgRefreshRate) {
		this.imgRefreshRate = imgRefreshRate;
	}
	public long getDelay() {
		return delay;
	}
	public void setDelay(long delay) {
		this.delay = delay;
	}
}
