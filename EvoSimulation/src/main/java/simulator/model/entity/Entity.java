package simulator.model.entity;

import static simulator.Constants.MOVEMENT_ENERGY_COST_CONSTANT;
import static simulator.Constants.DEFAULT_INITIAL_ENERGY;
import static simulator.Constants.DEFAULT_WEIGHT;
import static simulator.Constants.DEFAULT_INITIAL_REST_TIME;
import static simulator.Constants.jsonView;

import java.awt.Image;
import java.util.HashMap;
import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public abstract class Entity implements IInteract{
	protected String type;
	protected String id;
	protected Image img;
	public Node node;
	public boolean active;
	protected boolean alive;
	protected float energy;
	protected float weight;
	protected int age;
	protected int currentTime;
	protected Pheromone pheromone;
	protected int reproductionRestTime;
	protected  Controller ctrl;
	protected int generation;
	protected int foodEaten;

	public Entity(String id, Node n, Controller ctrl) {
		this.ctrl=ctrl;
		this.type = ".";
		this.id = id;
		this.node = n;
		this.alive = true;
		this.energy = DEFAULT_INITIAL_ENERGY;
		this.weight = DEFAULT_WEIGHT;
		this.age = 0;
		this.reproductionRestTime = DEFAULT_INITIAL_REST_TIME;
		this.generation = 0;
	}

	public void update(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
		age++;
	}

	public final MOVE getMove(HashMap<String,String>observations) {
		MOVE m = getTheMove(observations);
		if (m != MOVE.NEUTRAL)
			energy -= weight  * MOVEMENT_ENERGY_COST_CONSTANT;
			//* this.node.elevation
		return m;
	}
	public final int getAge() {return age;}
	public abstract MOVE getTheMove(HashMap<String,String>observations);
	public abstract boolean shouldInteract();
	@Override
	public void interact(Entity e) {
		if (!this.alive || !e.isAlive())
			return;
		else
			myInteract(e);
	}
	
	/**
	 * Receive food from a foodentity
	 * @param foodEntity
	 */
	protected abstract void getFood(FoodEntity foodEntity);

	public void vanish() {
		alive = false;
	}

	public JSONObject toJSON() {
		return new JSONObject().put("type", type).put("data",
				new JSONObject().put("id", id).put("x", node.x).put("y", node.y).put("age", age)
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
	public float getEnergy() {
		return this.energy;
	}
	public final Image getImage() {
		return img;
	}

	public void setNewNode(Node n) {
		node = n;
	}
	public String getId() {return id;}
	public Pheromone getPheromone(){return pheromone;}

	public int getReproductionRestTime() {
		return reproductionRestTime;
	}

	public void setReproductionRestTime(int reproductionRestTime) {
		this.reproductionRestTime = reproductionRestTime;
	}
	public void setEnergy(float energy) {
		this.energy = energy;
	}
	public int getGeneration() {
		return this.generation;
	}
	public int getFoodEaten() {
		return this.foodEaten;
	}
}
