package simulator.model.entity;

import static simulator.Constants.jsonView;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import setup.OOPParser;
import setup.OOPTokenizer;

import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.fsm.State;
import simulator.model.EvoSimulator;
import simulator.model.evaluation.ActionEvaluator;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;
import statistics.StatsManager;

public abstract class Entity{
	protected String type, id;
	protected Image img;
	public Node node;
	protected boolean alive;
	protected int currentTime, generation;
	protected Pheromone pheromone;
	protected Controller ctrl;
	protected ImageController imgController;
	State<Image>currentImgState;
	protected java.util.Map<String, Object>attributes;

	public static String attId;
	public static Function<Entity,Object>groupF;
	
	
	public Entity(String id, Node n, Controller ctrl) {
		this.ctrl=ctrl;
		this.type = ".";
		this.id = id;
		this.node = n;
		//this.alive = true;
		this.generation = 0;
		this.imgController = ctrl.getImgController();
		//this.updateImage();
		attributes = new java.util.LinkedHashMap<String, Object>();
	}

	/**
	 * Updates the {@link Entity#age}, {@link Entity#currentTime}
	 * @param evoSimulator
	 * @throws EvaluationException 
	 * @throws IllegalArgumentException 
	 */
	public void update(EvoSimulator evoSimulator) throws IllegalArgumentException, EvaluationException {
		currentTime=evoSimulator.getTime();
		//this.updateImage();
	}
	
	public abstract void perform(List<Entity>entities, Map map) throws EvaluationException;
	/**
	 * Updates the {@link Entity#energy} depending on the {@link Entity#getTheMove} result
	 * @param observations
	 * @return resulting move
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
//	public final MOVE getMove() {
//		MOVE m = getTheMove();
//		if (m != MOVE.NEUTRAL) {
//			this.setCurrentstate(STATE.MOVE);
//			this.decreaseEnergy((weight + this.node.elevation)  * MOVEMENT_ENERGY_COST_CONSTANT);
//			//actually we should take the resulting node of the move
//		}
//		else {
//			this.setCurrentstate(STATE.REST);
//		}
//		return m;
//	}
//	public ACTION getAction() {
//		return ACTION.NOTHING;
//	}
//	protected abstract MOVE getTheMove();
	public void apply(String code) throws JSONException, EvaluationException {
		OOPParser parser = new OOPParser() {
			@Override
			protected JSONObject Program() {
				return new JSONObject().put("list", this.Especification());
			}
		};
		this.apply(parser.parse(code).getJSONArray("list"));
	}
	public void apply(JSONArray code) throws EvaluationException {
		ActionEvaluator eval = new ActionEvaluator(code);
		java.util.Map<String, Object>vars = new HashMap<String, Object>();
		vars.put("this", this);
		try {
			eval.evaluate(vars);
		} catch (EvaluationException e) {
			System.out.println("Error applying init code");
			e.printStackTrace();
			throw e;
		}
	}
	public void updateObservations(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
	}
	
	public abstract boolean shouldInteract();
	
	public void interact(Entity e) throws IllegalArgumentException, EvaluationException {
		if (!this.alive || !e.isAlive())
			return;
		else
			myInteract(e);
	}
	public abstract void myInteract(Entity e2) throws IllegalArgumentException, EvaluationException;
	
	public void dispose() {
		alive = false;
		this.attributes.clear();
	}
	
	public Entity setAttribute(String key, Object value) {
		if(key.equals(Entity.attId))this.img = ImageController.getImage(value);
		this.attributes.put(key, value);
		return this;//for chaining
	}
	public boolean hasAttribute(String key) {
		return this.attributes.containsKey(key);
	}
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}
	public void notifyEvent(String id) {
		for(StatsManager sm:ctrl.getStatsManagers())sm.onEvent(id);
	}

	public JSONObject toJSON() {
		return new JSONObject().put("type", type).put("data",
				new JSONObject().put("id", id).put("x", node.x).put("y", node.y)
		// .put("image", JSONObject.NULL)
		);
	}

	@Override
	public String toString() {
		return toJSON().toString(jsonView);
	}
	public boolean isAlive() {
		return alive;
	}
	public final Image getImage() {
		return img;
	}
	public void setImg(Image img) {
		this.img = img;
	}
	public void setNewNode(Node n) {
		node = n;
	}
	public String getId() {return id;}
	public Pheromone getPheromone(){return pheromone;}

	public int getGeneration() {
		return this.generation;
	}


	

	public Controller getCtrl() {
		return ctrl;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return node;
	}



}
