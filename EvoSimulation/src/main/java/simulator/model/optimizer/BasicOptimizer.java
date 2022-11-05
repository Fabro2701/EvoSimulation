package simulator.model.optimizer;

import java.util.HashMap;
import java.util.List;

import simulator.model.EvoSimulator;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.entity.FoodEntity;
import simulator.model.map.Map;
import util.Util;

public class BasicOptimizer extends Optimizer{
	EvoSimulator simulator;
	
	public BasicOptimizer(EvoSimulator simulator) {
		this.simulator = simulator;
	}
	@Override
	public void update(Map map, List<Entity> entities, int time) {
		

		for (Entity e : entities) {
			e.update(simulator);
		}

		// entities movements
//		for (Entity e : entities) {
//			MOVE move = e.getMove();
//			e.setNewNode(map.getValidMove(e.node, move));
//		}
		
		// entities actions
		for (Entity e : entities) {
			if(e.isAlive())e.perform(entities, map);
//			if(e instanceof ActiveEntity) {
//				((ActiveEntity)e).setAction(e.getAction());
//			}
		}

		// entities interactions
//		for (Entity e1 : entities) {	
//			if(e1 instanceof ActiveEntity) {
//				for (Entity e2 : entities) {
//					if(e1!=e2) {
//						if (Util.areCloseEnough(e1, e2)) {
//							e1.interact(e2);
//						}
//					}
//				}
//			}
//		}

		// remove entities
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isAlive()) {
				entities.remove(i);
				i--;
			}
		}

		
	}
	
}
