

import java.util.Properties;
import java.util.Random;

import grammar.Evaluator;
import model.grammar.Parser;
import model.individual.Individual;
import model.module.operator.fitness.FitnessEvaluationOperator;
import post_analysis.fitness_tests.AbstractFitnessTest;
import post_analysis.fitness_tests.SimpleMazeFitnessTest;


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
		Parser parser = new Parser();
		Evaluator evaluator = new Evaluator(parser.parse(ind.getPhenotype().getVisualCode()));
		
		//MyIndividual mi = new MyIndividual(-1, test.getCtrl().getNodeAt(0, 0), evaluator., test.getCtrl());
		
		test.evaluate();
		return test.getResult();
	}
}
