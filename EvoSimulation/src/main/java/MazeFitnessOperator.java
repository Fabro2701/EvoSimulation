

import java.util.Properties;
import java.util.Random;

import grammar.Evaluator;
import model.grammar.Parser;
import model.individual.Individual;
import model.module.operator.fitness.FitnessEvaluationOperator;
import post_analysis.fitness_tests.AbstractFitnessTest;
import post_analysis.fitness_tests.SimpleMazeFitnessTest;
import simulator.model.entity.individuals.MyIndividual;


public class MazeFitnessOperator extends FitnessEvaluationOperator{
	AbstractFitnessTest test;
	public MazeFitnessOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		test = new SimpleMazeFitnessTest();	
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}

	@Override
	public float evaluate(Individual ind) {
		test.reset();
	
		MyIndividual mi = new MyIndividual("-1", test.getCtrl().getNodeAt(0, 0), ind.getPhenotype().getVisualCode(), test.getCtrl());
		
		test.setEntity(mi);
		test.evaluate();
		return test.getResult();
	}
}
