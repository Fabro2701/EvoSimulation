package simulator.model.entity;


import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.InteractionsController.InteractionsInt;
import simulator.control.fsm.State;
import simulator.model.EvoSimulator;
import simulator.model.InteractionI;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;

public class ActiveEntity extends AliveEntity {
	private java.util.Map<String, java.util.Map<String,Integer>>interactionsMap;
	public ActiveEntity(String id, Node n, Controller ctrl, String code) throws JSONException, EvaluationException {
		super(id, n, ctrl, code);
		pheromone = new Pheromone();
		pheromone.init(this);
		this.attributes.put("pasive", false);
		interactionsMap = new HashMap<>();
		this.interactions.getInteractions().keySet().stream().forEach(i->interactionsMap.put(i, new HashMap<>()));
		if(this.img == null)this.img = ImageController.getImage(this.getClass());
		if(this.getClass().equals(ActiveEntity.class))init();
	}

	@Override
	public void myInteract(Entity e2) throws IllegalArgumentException, EvaluationException {
		int time = ctrl.getSimulator().getTime();
		String e2id = e2.getId();
		java.util.Map<String, InteractionsInt> interactions_l = interactions.getInteractions();
		java.util.Map<String, Integer> interactionsFreq = interactions.getInteractionsFreq();
		for(String intid:interactions_l.keySet()) {
			if(interactions.match(intid, this.getClass(), e2.getClass())) {
				java.util.Map<String, Integer> intMap = this.interactionsMap.get(intid);
				if(intMap.containsKey(e2id)){
					if((time-intMap.get(e2id))%interactionsFreq.get(intid)!=0) {
						continue;
					}
				}
				interactions_l.get(intid).perform(this, e2, ctrl.getMap(), time);
				intMap.put(e2id, time);
			}
		}
	}
	@Override
	public void update(EvoSimulator evoSimulator) throws IllegalArgumentException, EvaluationException {
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

	@Override
	public void perform(List<Entity> entities, Map map) throws EvaluationException {
		// TODO Auto-generated method stub
		
	}

}
