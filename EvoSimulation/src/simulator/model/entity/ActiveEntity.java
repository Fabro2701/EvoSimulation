package simulator.model.entity;

import static simulator.Constants.FOOD_ENERGY_GIVEN_CONSTANT;
import static simulator.Constants.HEAT_LIVE_ENERGY_COST_CONSTANT;
import static simulator.Constants.LIVE_ENERGY_COST_CONSTANT;

import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public abstract class ActiveEntity extends Entity {

	public ActiveEntity(String id, Node n) {
		super(id, n);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getFood(FoodEntity foodEntity) {
		energy += (float) foodEntity.getFoodAmount() * FOOD_ENERGY_GIVEN_CONSTANT;
	}

	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		age++;
		if (alive && energy <= 0.0f) {
			this.vanish();
		}
		energy -= weight * LIVE_ENERGY_COST_CONSTANT;
		energy -= this.node.temperature * HEAT_LIVE_ENERGY_COST_CONSTANT;
	}

	@Override
	public void myInteract(Entity e) {
		e.recieveActiveEntityInteraction(this);
	}
	@Override
	public boolean shouldInteract() {return true;}

}
