

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import model.Experiment;
import model.algorithm.AbstractPipeline;
import model.algorithm.AbstractSearchAlgorithm;
import model.algorithm.BasicSearchAlgorithm;
import model.algorithm.SimplePipeline;
import model.grammar.AbstractGrammar;
import model.grammar.StandardGrammar;
import model.individual.Population;
import model.module.CollectorModule;
import model.module.CrossoverModule;
import model.module.FitnessModule;
import model.module.InitializationModule;
import model.module.JoinModule;
import model.module.MutationModule;
import model.module.SelectionModule;
import model.module.operator.Operator;
import model.module.operator.collector.FitnessCollectorOperator;
import model.module.operator.collector.SimilarityCollectorOperator;
import model.module.operator.crossover.CrossoverOperator;
import model.module.operator.crossover.OnePointCrossoverOperator;
import model.module.operator.fitness.FitnessEvaluationOperator;
import model.module.operator.initialization.InitializationOperator;
import model.module.operator.initialization.RandomInitializerOperator;
import model.module.operator.join.JoinOperator;
import model.module.operator.join.SimpleJoinOperator;
import model.module.operator.mutation.BasicCodonFlipMutationOperator;
import model.module.operator.mutation.MutationOperator;
import model.module.operator.selection.SelectionOperator;
import model.module.operator.selection.TournamentSelectionOperator;

public class Test extends Experiment{
	public Test(AbstractSearchAlgorithm algo) {
		super(algo);
	}
	public Test() {
		super();
	}
	@Override
	public void setup(Properties properties) {
		AbstractPipeline initPipeline = new SimplePipeline();
		
		AbstractGrammar grammar = this.loadGrammar(properties);
		System.out.println(grammar);
	
		InitializationModule initModule = new InitializationModule(generalPopulation, properties,rnd);
		InitializationOperator rinitOp = this.loadInitializer(properties, grammar);
		initModule.addOperator(rinitOp);
		
		FitnessModule initFitnessModule = new FitnessModule(generalPopulation, properties,rnd);
		FitnessEvaluationOperator fitnessInitOp = new MazeFitnessOperator(properties,rnd);
		initFitnessModule.addOperator(fitnessInitOp);
		
		CollectorModule fitnesscollModule = new CollectorModule(generalPopulation, properties,rnd);
		Operator fitnesscollOp = new FitnessCollectorOperator(properties,rnd);
		fitnesscollModule.addOperator(fitnesscollOp);

		//loop
		AbstractPipeline loopPipeline = new SimplePipeline();
		
		SelectionModule selectionModule = new SelectionModule(generalPopulation, properties, rnd, selectedPopulation);
		SelectionOperator selectionOp = this.loadSelection(properties);
		selectionModule.addOperator(selectionOp);
		
		CrossoverModule crossoverModule = new CrossoverModule(selectedPopulation, properties, rnd);
		CrossoverOperator crossoverOp = this.loadCrossover(properties);
		crossoverModule.addOperator(crossoverOp);
		
		MutationModule mutationModule = new MutationModule(selectedPopulation, properties, rnd);
		MutationOperator mutationOp = this.loadMutation(properties);
		mutationModule.addOperator(mutationOp);
		
		FitnessModule fitnessModule = new FitnessModule(selectedPopulation, properties,rnd);
		FitnessEvaluationOperator fitnessOp = new MazeFitnessOperator(properties,rnd);
		fitnessModule.addOperator(fitnessOp);
		
		JoinModule joinModule = new JoinModule(generalPopulation, properties, rnd, selectedPopulation);
		JoinOperator joinOp = this.loadJoin(properties);
		joinModule.addOperator(joinOp);
		
		CollectorModule simicollModule = new CollectorModule(generalPopulation, properties,rnd);
		Operator simicollOp = new SimilarityCollectorOperator(properties,rnd);
		simicollModule.addOperator(simicollOp);

		
		initPipeline.addModule(initModule);
		initPipeline.addModule(initFitnessModule);
		initPipeline.addModule(fitnesscollModule);

		loopPipeline.addModule(selectionModule);
		loopPipeline.addModule(crossoverModule);
		loopPipeline.addModule(mutationModule);
		loopPipeline.addModule(fitnessModule);
		loopPipeline.addModule(joinModule);
		loopPipeline.addModule(fitnesscollModule);
		loopPipeline.addModule(simicollModule);
		
		this.algorithm.setInitPipeline(initPipeline);
		this.algorithm.setLoopPipeline(loopPipeline);
	}
	public static void main(String args[]) {
		Properties properties = new Properties();
		try { 
			properties.load(new FileInputStream(new File("resources/properties/default.properties")));
		} catch (IOException e) {e.printStackTrace(); } 
		
		Test test = new Test();
		test.setup(properties);
		test.run(properties);
	}
	public static void main2(String args[]) {
		AbstractGrammar grammar = new StandardGrammar();
		grammar.parseBNF("default");
		System.out.println(grammar.toString());
		Properties properties = new Properties();
		try { 
			properties.load(new FileInputStream(new File("resources/properties/default.properties")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		} 
		Random rnd = new Random(7);
		 
		
		AbstractSearchAlgorithm algo = new BasicSearchAlgorithm();
		Population generalPopulation = new Population();
		Population selectedPopulation = new Population();
		
		//init
		AbstractPipeline initPipeline = new SimplePipeline();
		
		InitializationModule initModule = new InitializationModule(generalPopulation, properties,rnd);
		InitializationOperator rinitOp = new RandomInitializerOperator(properties,rnd,grammar);
		initModule.addOperator(rinitOp);
		
		FitnessModule initFitnessModule = new FitnessModule(generalPopulation, properties,rnd);
		FitnessEvaluationOperator fitnessOp = new MazeFitnessOperator(properties,rnd);
		initFitnessModule.addOperator(fitnessOp);
		
		CollectorModule fitnesscollModule = new CollectorModule(generalPopulation, properties,rnd);
		Operator fitnesscollOp = new FitnessCollectorOperator(properties,rnd);
		fitnesscollModule.addOperator(fitnesscollOp);

		//loop
		AbstractPipeline loopPipeline = new SimplePipeline();
		
		SelectionModule selectionModule = new SelectionModule(generalPopulation, properties, rnd, selectedPopulation);
		SelectionOperator selectionOp = new TournamentSelectionOperator(properties, rnd);
		selectionModule.addOperator(selectionOp);
		
		CrossoverModule crossoverModule = new CrossoverModule(selectedPopulation, properties, rnd);
		CrossoverOperator crossoverOp = new OnePointCrossoverOperator(properties,rnd);
		crossoverModule.addOperator(crossoverOp);
		
		MutationModule mutationModule = new MutationModule(selectedPopulation, properties, rnd);
		MutationOperator mutationOp = new BasicCodonFlipMutationOperator(properties,rnd);
		mutationModule.addOperator(mutationOp);
		
		FitnessModule fitnessModule = new FitnessModule(selectedPopulation, properties,rnd);
		fitnessModule.addOperator(fitnessOp);
		
		JoinModule joinModule = new JoinModule(generalPopulation, properties, rnd, selectedPopulation);
		JoinOperator joinOp = new SimpleJoinOperator(properties,rnd);
		joinModule.addOperator(joinOp);
		
//		initModule.setPopulation(generalPopulation);
//		initFitnessModule.setPopulation(generalPopulation);
//		
//		selectionModule.setPopulation(generalPopulation);
//		crossoverModule.setPopulation(selectedPopulation);
//		mutationModule.setPopulation(selectedPopulation);
//		fitnessModule.setPopulation(selectedPopulation);
//		joinModule.setPopulation(generalPopulation);
//		
//		fitnesscollModule.setPopulation(generalPopulation);
		
		initPipeline.addModule(initModule);
		initPipeline.addModule(initFitnessModule);
		initPipeline.addModule(fitnesscollModule);

		loopPipeline.addModule(selectionModule);
		loopPipeline.addModule(crossoverModule);
		loopPipeline.addModule(mutationModule);
		loopPipeline.addModule(fitnessModule);
		loopPipeline.addModule(joinModule);
		loopPipeline.addModule(fitnesscollModule);
		
		algo.setInitPipeline(initPipeline);
		algo.setLoopPipeline(loopPipeline);
		
		algo.run(200);
		
	}
}
