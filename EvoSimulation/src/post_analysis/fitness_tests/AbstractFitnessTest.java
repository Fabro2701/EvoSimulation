package post_analysis.fitness_tests;

import simulator.model.entity.Entity;
import util.Pair;

public abstract class AbstractFitnessTest {
	public AbstractFitnessTest() {
		
	}
	public abstract Pair<Integer,Float>  evaluate(Entity e);
}
