package simulator.model.entity;

import static simulator.Constants.MOVEMENT_ENERGY_COST_CONSTANT;
import static simulator.Constants.DEFAULT_INITIAL_ENERGY;
import static simulator.Constants.DEFAULT_INITIAL_WEIGHT;
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
	protected String type, id;
	protected Image img;
	public Node node;
	protected boolean alive;
	protected float energy, weight;
	protected int age, currentTime, reproductionRestTime, generation, foodEaten;
	protected Pheromone pheromone;
	protected Controller ctrl;

	public Entity(String id, Node n, Controller ctrl) {
		this.ctrl=ctrl;
		this.type = ".";
		this.id = id;
		this.node = n;
		this.alive = true;
		this.energy = DEFAULT_INITIAL_ENERGY;
		this.weight = DEFAULT_INITIAL_WEIGHT;
		this.age = 0;
		this.reproductionRestTime = DEFAULT_INITIAL_REST_TIME;
		this.generation = 0;
	}

	/**
	 * Updates the {@link Entity#age} and {@link Entity#currentTime}
	 * @param evoSimulator
	 */
	public void update(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
		age++;
	}
	
	/**
	 * Updates the {@link Entity#energy} depending on the {@link Entity#getTheMove} result
	 * @param observations
	 * @return resulting move
	 */
	public final MOVE getMove() {
		MOVE m = getTheMove();
		if (m != MOVE.NEUTRAL)
			energy -= weight  * MOVEMENT_ENERGY_COST_CONSTANT;
			//* this.node.elevation
		return m;
	}
	protected abstract MOVE getTheMove();
	
	public void updateObservations(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
		age++;
	}
	
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
	public int getAge() {
		return age;
	}
}
