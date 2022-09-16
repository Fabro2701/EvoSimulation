package simulator.model.entity.individuals;

import static simulator.Constants.CHROMOSOME_LENGTH;
import static simulator.Constants.REPRODUCTION_COST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import grammar.IntronGrammar;
import grammar.operator.crossover.SinglePointCrossover;
import grammar.operator.mutation.SingleCodonFlipMutation;
import post_analysis.fitness_tests.SimpleMazeFitnessTest;
import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.map.Node;
import util.Pair;

public class MyIndividual2 extends MyIndividual{

	/**
	 * Main constructor used by factories for creating random entities
	 * @param id
	 * @param n node
	 * @param ctrl
	 */
	public MyIndividual2(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		type = "mi2";
		img = new ImageIcon("resources/entities/myentity2.png").getImage();
		grammar = ctrl.getCommonGrammar2();
		Chromosome c = new Chromosome(CHROMOSOME_LENGTH);
		
						   
		if(id.equals("-2"))c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0,  0);

		LinkedList<Symbol> crom = grammar.parse(c);
		
		genotype = new Genotype(c);
		
		if(crom==null) {
			ctrl.getStatsManager().onDeadOffSpring(0);
			vanish();
			phenotype = new Phenotype();
		}
		else phenotype = new Phenotype(crom);
		
		childs = new ArrayList<Entity>();

	}
	/**
	 * Constructor invoked by reproduction methods 
	 * @param geno
	 * @param ctrl
	 * @param generation The greatest generation of parents
	 */
	public MyIndividual2(Genotype geno, Controller ctrl, int generation) {
		super(ctrl.getNextId(), ctrl.randomNode(), ctrl);
		type = "mi2";
		img = new ImageIcon("resources/entities/myentity2.png").getImage();
		//grammar = new Grammar("s");
		grammar = ctrl.getCommonGrammar2();
		genotype = new Genotype(geno.get(0));
		mutate();
		LinkedList<Symbol> crom = grammar.parse(genotype.get(0));
		
		if(crom==null) {
			ctrl.getStatsManager().onDeadOffSpring(1);
			vanish();
			phenotype = new Phenotype();
		}
		else phenotype = new Phenotype(crom);
		
		childs = new ArrayList<Entity>();
		this.generation =  generation+1;

	}
	
	public MyIndividual2(String id, Node n, JSONObject genotype, JSONObject phenotype, float energy, Controller ctrl) {
		super(id, n, ctrl);
		this.energy=energy;
		type = "mi2";
		img = new ImageIcon("resources/entities/myentity2.png").getImage();
		//grammar = new Grammar("s");
		grammar = ctrl.getCommonGrammar2();
		

		
		this.genotype = new Genotype(genotype);
		
		if(phenotype.getString("code").equals("")) {
			vanish();
			this.phenotype = new Phenotype();
		}
		else this.phenotype = new Phenotype(phenotype);
		
		childs = new ArrayList<Entity>();

		
	}
	public void mutate() {
		if(RandomSingleton.nextFloat()<this.node.radiation+0.05f) {
			new SingleCodonFlipMutation().mutate(genotype);
			ctrl.getStatsManager().onMutation();
		}
	}
	@Override
	public void update(EvoSimulator simulator) {
		super.update(simulator);
		
		if(childs.size()!=0) {
			simulator.addEntity(childs.get(0));
			simulator.addEntity(childs.get(1));
			childs.clear();
		}
		
	}
	@Override
	public MOVE getTheMove() {
		MOVE move = phenotype.getNext(this.observationManager.getVariables());
		if(move==null)return MOVE.NEUTRAL;		
		
		return move;
	}
	@Override
	public void recieveActiveEntityInteraction(Entity e) {
		if(this.getReproductionRestTime()<=0) {
			if(this.getClass().equals(e.getClass())&&this.energy>=REPRODUCTION_COST && e.getEnergy()>=REPRODUCTION_COST) {
				ctrl.getStatsManager().onReproduction();// SinglePointCrossover EqualOffspringCrossover
				Pair<Genotype,Genotype> p = new SinglePointCrossover().crossover(this.genotype, ((MyIndividual)e).getGenotype());
				childs.add(new MyIndividual2(p.first,ctrl,Math.max(this.generation, e.getGeneration())));
				childs.add(new MyIndividual2(p.second,ctrl,Math.max(this.generation, e.getGeneration())));
				
				this.setReproductionRestTime(300);
				e.setReproductionRestTime(300);
				
				this.energy-=REPRODUCTION_COST;
				e.setEnergy(e.getEnergy()-REPRODUCTION_COST);;
			}
		}
	}
	

}
