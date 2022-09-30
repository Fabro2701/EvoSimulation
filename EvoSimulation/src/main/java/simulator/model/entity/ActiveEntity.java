package simulator.model.entity;

import static simulator.Constants.ENTITY_FOOD_FACTOR;
import static simulator.Constants.FOOD_ENERGY_GIVEN_CONSTANT;
import static simulator.Constants.LIVE_ENERGY_COST_CONSTANT;
import static simulator.Constants.RECOVERY_REST_TIME;

import java.awt.Image;

import simulator.Constants.ACTION;
import simulator.Constants.STATE;
import simulator.control.Controller;
import simulator.control.fsm.State;
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
		this.increaseEnergy(foodEntity.getFoodAmount() * FOOD_ENERGY_GIVEN_CONSTANT);
		foodEaten++;
		this.setCurrentstate(STATE.EAT);
	}

	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		if (alive && energy <= 0.0f) {
			this.vanish();
			//evoSimulator.addEntity(new FoodEntity(this.id,this.node,3.f,ctrl));
		}
		this.decreaseEnergy((weight + this.node.temperature) * LIVE_ENERGY_COST_CONSTANT);
		
		this.setReproductionRestTime(this.getReproductionRestTime() - 1);
		this.setAttackRestTime(this.getAttackRestTime() - 1);

		
	}

	@Override
	public void myInteract(Entity e) {
		if(e instanceof PasiveEntity) {
			e.recieveActiveEntityInteraction(this);
			return;
		}
		if(action == ACTION.REPRODUCTION) {
			if(e.getAction() == ACTION.REPRODUCTION) {
				if(this.getReproductionRestTime()<=0 && e.getReproductionRestTime()<=0) {
					e.recieveActiveEntityReproductionInteraction(this);
					this.setReproductionRestTime(RECOVERY_REST_TIME);
					e.setReproductionRestTime(RECOVERY_REST_TIME);
				}
			}
		}
		else if(action == ACTION.ATTACK) {
			if(this.attackRestTime <= 0) {
				e.recieveActiveEntityAttackInteraction(this);
				this.setAttackRestTime((int) (RECOVERY_REST_TIME*0.5));
			}
			
		}
		//e.recieveActiveEntityInteraction(this);
	}
	@Override
	public boolean shouldInteract() {return true;}

	public void eatMe(ActiveEntity e) {
		e.increaseEnergy(this.weight * ENTITY_FOOD_FACTOR * FOOD_ENERGY_GIVEN_CONSTANT);
		this.vanish();
	}
	public ACTION getAction() {
		return action;
	}
	public void setAction(ACTION action) {
		this.action = action;
	}

}
