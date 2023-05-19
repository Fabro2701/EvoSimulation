package simulator.model.optimizer;

import java.util.HashMap;
import java.util.List;

import simulator.model.EvoSimulator;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import util.Util;

public class BasicOptimizer implements Optimizer{
	EvoSimulator simulator;
	
	public BasicOptimizer(EvoSimulator simulator) {
		this.simulator = simulator;
	}
	@Override
	public void update(Map map, List<Entity> entities, int time) throws IllegalArgumentException, EvaluationException {
		

		for (Entity e : entities) {
			if(e.isAlive())e.update(simulator);
		}

		// entities actions
		for (Entity e : entities) {
			if(e.isAlive())e.perform(entities, map);
		}

		// entities interactions
		for (Entity e1 : entities) {	
			if(e1.isAlive()&&e1 instanceof ActiveEntity) {
				for (Entity e2 : entities) {
					if(e1!=e2&&e2.isAlive()) {
						if (Util.areCloseEnough(e1, e2)) {
							e1.interact(e2);
						}
					}
				}
			}
		}


		// remove entities
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isAlive()) {
				entities.remove(i);
				i--;
			}
		}

		
	}
	
}
