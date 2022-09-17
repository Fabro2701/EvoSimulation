package simulator.model.entity;

import static simulator.Constants.FOOD_ENERGY_GIVEN_CONSTANT;
import static simulator.Constants.LIVE_ENERGY_COST_CONSTANT;

import simulator.Constants.ACTION;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public abstract class ActiveEntity extends Entity {
	ACTION action;
	public ActiveEntity(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		pheromone = new Pheromone();
		pheromone.init(this);
		this.action = ACTION.NOTHING;
	}

	@Override
	protected void getFood(FoodEntity foodEntity) {
		energy += (float) foodEntity.getFoodAmount() * FOOD_ENERGY_GIVEN_CONSTANT;
		energy = energy>=300.0f?300.0f:energy;
		foodEaten++;
	}

	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		if (alive && energy <= 0.0f) {
			this.vanish();
			//evoSimulator.addEntity(new FoodEntity(this.id,this.node,3.f,ctrl));
		}
		energy -= weight * LIVE_ENERGY_COST_CONSTANT;
		//energy -= this.node.temperature * HEAT_LIVE_ENERGY_COST_CONSTANT;
		
		setReproductionRestTime(getReproductionRestTime() - 1);
	}

	@Override
	public void myInteract(Entity e) {
		if(e instanceof PasiveEntity) {
			e.recieveActiveEntityInteraction(this);
			return;
		}
		if(action == ACTION.REPRODUCTION) {
			e.recieveActiveEntityReproductionInteraction(this);
		}
		else if(action == ACTION.ATTACK) {
			e.recieveActiveEntityAttackInteraction(this);
		}
		//e.recieveActiveEntityInteraction(this);
	}
	@Override
	public boolean shouldInteract() {return true;}

	public ACTION getAction() {
		return action;
	}

	public void setAction(ACTION action) {
		this.action = action;
	}

}
