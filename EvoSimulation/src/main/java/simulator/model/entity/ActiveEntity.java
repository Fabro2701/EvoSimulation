package simulator.model.entity;


import simulator.control.Controller;
import simulator.control.fsm.State;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public abstract class ActiveEntity extends InteractiveEntity {
	public ActiveEntity(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		pheromone = new Pheromone();
		pheromone.init(this);
		this.attributes.put("pasive", false);
	}


	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		//this.decreaseEnergy((weight + this.node.temperature) * LIVE_ENERGY_COST_CONSTANT);
		
//		this.setReproductionRestTime(this.getReproductionRestTime() - 1);
//		this.setAttackRestTime(this.getAttackRestTime() - 1);

		
	}

//	@Override
//	public void myInteract(Entity e) {
//		
//		if(e instanceof PasiveEntity) {
//			e.recieveActiveEntityInteraction(this);
//			return;
//		}
//		if(action == ACTION.REPRODUCTION) {
//			if(e.getAction() == ACTION.REPRODUCTION) {
//				if(this.getReproductionRestTime()<=0 && e.getReproductionRestTime()<=0) {
//					e.recieveActiveEntityReproductionInteraction(this);
//					this.setReproductionRestTime(RECOVERY_REST_TIME);
//					e.setReproductionRestTime(RECOVERY_REST_TIME);
//				}
//			}
//		}
//		else if(action == ACTION.ATTACK) {
//			if(this.attackRestTime <= 0) {
//				e.recieveActiveEntityAttackInteraction(this);
//				this.setAttackRestTime((int) (RECOVERY_REST_TIME*0.5));
//			}
//			
//		}
		//e.recieveActiveEntityInteraction(this);
//	}
	@Override
	public boolean shouldInteract() {return true;}
//
//	public void eatMe(ActiveEntity e) {
//		e.increaseEnergy(this.weight * ENTITY_FOOD_FACTOR * FOOD_ENERGY_GIVEN_CONSTANT);
//		this.vanish();
//	}

}
