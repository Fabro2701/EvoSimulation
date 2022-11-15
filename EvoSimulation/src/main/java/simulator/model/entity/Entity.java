package simulator.model.entity;

import static simulator.Constants.jsonView;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import setup.OOPParser;
import setup.OOPTokenizer;
import simulator.Constants.STATE;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.fsm.State;
import simulator.model.EvoSimulator;
import simulator.model.evaluation.ActionEvaluator;
import simulator.model.map.Map;
import simulator.model.map.Node;

public abstract class Entity{
	protected String type, id;
	protected Image img;
	public Node node;
	protected boolean alive;
	protected int age, currentTime, generation;
	protected Pheromone pheromone;
	protected Controller ctrl;
	protected ImageController imgController;
	STATE currentstate;
	State<Image>currentImgState;
	protected java.util.Map<String, Object>attributes;

	public Entity(String id, Node n, Controller ctrl) {
		this.ctrl=ctrl;
		this.type = ".";
		this.id = id;
		this.node = n;
		//this.alive = true;
		this.age = 0;
		this.generation = 0;
		this.imgController = ctrl.getImgController();
		this.currentstate = STATE.REST;
		this.updateImage();
		attributes = new java.util.LinkedHashMap<String, Object>();
	}

	/**
	 * Updates the {@link Entity#age}, {@link Entity#currentTime}, {@link Entity#currentImgState}, {@link Entity#img} 
	 * @param evoSimulator
	 */
	public void update(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
		age++;
		//this.updateImage();
	}
	
	public abstract void perform(List<Entity>entities, Map map);
	/**
	 * Updates the {@link Entity#energy} depending on the {@link Entity#getTheMove} result
	 * @param observations
	 * @return resulting move
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
	public void apply(String code) {
		OOPParser parser = new OOPParser() {
			@Override
			protected JSONObject Program() {
				return new JSONObject().put("list", this.Especification());
			}
		};
		this.apply(parser.parse(code).getJSONArray("list"));
	}
	public void apply(JSONArray code) {
		ActionEvaluator eval = new ActionEvaluator(code);
		java.util.Map<String, Object>vars = new HashMap<String, Object>();
		vars.put("this", this);
		eval.evaluate(vars);
	}
	public void updateObservations(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
		age++;
	}
	
	public abstract boolean shouldInteract();
	
	public void interact(Entity e) {
		if (!this.alive || !e.isAlive())
			return;
		else
			myInteract(e);
	}
	public abstract void myInteract(Entity e2);
	
	public void vanish() {
		alive = false;
	}
	
	public Entity setAttribute(String key, Object value) {
		if(key.equals("imc"))img = ImageController.getImage((String) value);
		this.attributes.put(key, value);
		return this;//for chaining
	}
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	public JSONObject toJSON() {
		return new JSONObject().put("type", type).put("data",
				new JSONObject().put("id", id).put("x", node.x).put("y", node.y).put("age", age)
		// .put("image", JSONObject.NULL)
		);
	}

	protected void updateImage() {
		this.currentImgState = this.imgController.getNextImage(this.getClass(), currentImgState, currentstate);
		this.img = currentImgState.execute();
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

	public void setNewNode(Node n) {
		node = n;
	}
	public String getId() {return id;}
	public Pheromone getPheromone(){return pheromone;}

	public int getGeneration() {
		return this.generation;
	}
	public int getAge() {
		return age;
	}

	public STATE getCurrentstate() {
		return currentstate;
	}

	public void setCurrentstate(STATE state) {
		//System.out.println(this.currentstate+"  ----> "+state);
		//if(state.compareTo(this.currentstate)<0) this.currentstate = state;
		this.currentstate = state;
	}


}
