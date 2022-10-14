package simulator.model.entity;

import static simulator.Constants.DEFAULT_INITIAL_ENERGY;
import static simulator.Constants.DEFAULT_INITIAL_REST_TIME;
import static simulator.Constants.DEFAULT_INITIAL_WEIGHT;
import static simulator.Constants.MAX_ENERGY;
import static simulator.Constants.MOVEMENT_ENERGY_COST_CONSTANT;
import static simulator.Constants.jsonView;

import java.awt.Image;

import org.json.JSONObject;

import simulator.Constants.ACTION;
import simulator.Constants.MOVE;
import simulator.Constants.STATE;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.fsm.State;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public abstract class Entity implements IInteract{
	protected String type, id;
	protected Image img;
	public Node node;
	protected boolean alive;
	protected float energy, weight;
	protected int age, currentTime, reproductionRestTime, generation, foodEaten, attackRestTime;
	protected Pheromone pheromone;
	protected Controller ctrl;
	protected ImageController imgController;
	STATE currentstate;
	State<Image>currentImgState;

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
		this.attackRestTime = DEFAULT_INITIAL_REST_TIME;
		this.generation = 0;
		this.imgController = ctrl.getImgController();
		this.currentstate = STATE.REST;
		this.updateImage();
	}

	/**
	 * Updates the {@link Entity#age}, {@link Entity#currentTime}, {@link Entity#currentImgState}, {@link Entity#img} 
	 * @param evoSimulator
	 */
	public void update(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
		age++;
		this.updateImage();
	}
	
	/**
	 * Updates the {@link Entity#energy} depending on the {@link Entity#getTheMove} result
	 * @param observations
	 * @return resulting move
	 */
	public final MOVE getMove() {
		MOVE m = getTheMove();
		if (m != MOVE.NEUTRAL) {
			this.setCurrentstate(STATE.MOVE);
			this.decreaseEnergy((weight + this.node.elevation)  * MOVEMENT_ENERGY_COST_CONSTANT);
			//actually we should take the resulting node of the move
		}
		else {
			this.setCurrentstate(STATE.REST);
		}
		return m;
	}
	public ACTION getAction() {
		return ACTION.NOTHING;
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
	public float decreaseEnergy(float d) {
		this.energy-=d;
		Math.min(energy, MAX_ENERGY);		
		return this.energy;
	}
	public float increaseEnergy(float d) {
		this.energy+=d;
		Math.min(energy, MAX_ENERGY);		
		return this.energy;
	}

	public STATE getCurrentstate() {
		return currentstate;
	}

	public void setCurrentstate(STATE state) {
		//System.out.println(this.currentstate+"  ----> "+state);
		//if(state.compareTo(this.currentstate)<0) this.currentstate = state;
		this.currentstate = state;
	}

	public int getAttackRestTime() {
		return attackRestTime;
	}

	public void setAttackRestTime(int attackRestTime) {
		this.attackRestTime = attackRestTime;
	}
}
