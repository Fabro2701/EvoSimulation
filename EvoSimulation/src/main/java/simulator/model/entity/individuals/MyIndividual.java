package simulator.model.entity.individuals;


import static simulator.Constants.CHROMOSOME_LENGTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;
import simulator.model.entity.individuals.genome.Phenotype;
import simulator.model.entity.individuals.genome.PolymorphismController;
import simulator.model.entity.individuals.genome.PolymorphismController.VARIATION;
import simulator.model.entity.individuals.genome.operator.crossover.SinglePointCrossover;
import simulator.model.entity.individuals.genome.operator.mutation.SingleCodonFlipMutation;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;
import statistics.StatsManager;
import util.Pair;
import util.Util;

public class MyIndividual extends GIndividual{
	protected List<Entity>children;
	
	

	/**
	 * Base Constructor
	 * @param ctrl
	 * @param id
	 * @param n
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public MyIndividual(String id, Node node, Controller ctrl, String code) throws JSONException, EvaluationException {
		super(id, node, ctrl, code);
		type = "mi";
		//img = new ImageIcon("resources/entities/myentity.png").getImage();
		if(this.img == null)this.img = ImageController.getImage(this.getClass());
		children = new ArrayList<Entity>();
		//init();
	}
	public MyIndividual(String id, Node node, Controller ctrl) throws JSONException, EvaluationException {
		this(id,node,ctrl,"");
	}

	/**
	 * Constructor invoked by reproduction methods 
	 * @param geno
	 * @param ctrl
	 * @param generation The greatest generation of parents
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public static MyIndividual fromParents(Genotype geno, Controller ctrl, int generation) throws JSONException, EvaluationException {
		MyIndividual ind = new MyIndividual(ctrl.getNextId(),  ctrl.randomNode(), ctrl,null);
		ind.genotype = new Genotype(geno);
		ind.phenotype = new Phenotype();
		ind.mutate();
		
		//grammars chroms
		int i=0;
		for(String key:ind.grammars.keySet()) {
			Chromosome<Chromosome.Codon> c = ind.genotype.getChromosome(i);
			LinkedList<Symbol> crom = (LinkedList<Symbol>) ind.grammars.get(key).mapChromosome(c);
			ind.phenotype.setSymbol(key, crom);
			if(ind.phenotype.isValid()==false) {
				//for(StatsManager sm:ctrl.getStatsManagers())sm.onEvent("offspringdeath");;
				ind.dispose();
				return ind;
			}
			i++;
		}
		
		//genes chrom
		Chromosome<Boolean> c = ind.genotype.getChromosome(ind.genotype.size()-2);
		GIndividual.Genes genesMapper = new GIndividual.Genes();
		HashSet<String> genes = genesMapper.mapGenes(c);
		ind.phenotype.setGenes(genes);
		
		//polymorphims chrom
		Chromosome<Float> c2 = ind.genotype.getChromosome(ind.genotype.size()-1);
		java.util.Map<String, VARIATION> polys = new PolymorphismController().mapPolymorphisms(c2);
		ind.phenotype.setPolymorphims(polys);
		
		ind.generation =  generation+1;
		
		ind.init();
		return ind;
	}

	public static MyIndividual fromChromosome(String id, Node node, List<Chromosome>cs, Controller ctrl) throws JSONException, EvaluationException {
		Objects.requireNonNull(cs);
		
		MyIndividual ind = new MyIndividual(id, node,ctrl, null);
		ind.genotype = new Genotype();
		ind.phenotype = new Phenotype();
		
		assert cs.size()==ind.grammars.size();
		
		int i=0;
		for(String key:ind.grammars.keySet()) {
			ind.genotype.addChromosome(cs.get(i));
			LinkedList<Symbol> crom = (LinkedList<Symbol>) ind.grammars.get(key).mapChromosome(cs.get(i));
			ind.phenotype.setSymbol(key, crom);
			if(ind.phenotype.isValid()==false) {
				//for(StatsManager sm:ctrl.getStatsManagers())sm.onEvent("offspringdeath");
				ind.dispose();
			}
		}
		return ind;
	}
//	public MyIndividual(String id, Node node, Chromosome c, Controller ctrl) {
//		this(ctrl, id, node);
//		//genotype = new Genotype(c); pending
//		LinkedList<Symbol> crom = moveGrammar.parse(genotype.get(0));
//		
//		if(crom==null) {
//			ctrl.getStatsManager().onDeadOffSpring(0);
//			vanish();
//			phenotype = new Phenotype();
//		}
//		else {
//			
//			///phenotype = new Phenotype(crom);pending
//			//System.out.println(phenotype.getVisualCode());
//		}
//		
//	}
	public static MyIndividual fromJSON(String id, Node node, JSONObject genotype, JSONObject phenotype, JSONObject properties, Controller ctrl) throws JSONException, EvaluationException {
		MyIndividual ind = new MyIndividual(id, node,ctrl, null);
		ind.genotype = new Genotype(genotype);
		
		if(phenotype.getString("code").equals("")) {
			ind.dispose();
			ind.phenotype = new Phenotype();
		}
		else ind.phenotype = new Phenotype(phenotype);
		return ind;
	}
	
	public void reproduce(MyIndividual i2) throws JSONException, EvaluationException {
		for(StatsManager sm:ctrl.getStatsManagers())sm.onEvent("reproduction");

		Pair<Genotype, Genotype> childs = new SinglePointCrossover().crossover(this.genotype, i2.genotype);
		this.ctrl.getSimulator().addEntity(MyIndividual.fromParents(childs.first, ctrl, Math.max(this.generation, i2.generation)+1));
		this.ctrl.getSimulator().addEntity(MyIndividual.fromParents(childs.second, ctrl, Math.max(this.generation, i2.generation)+1));
	}
	public void mutate() {
		new SingleCodonFlipMutation().mutate(genotype,0.10f);
		//ctrl.getStatsManager().onMutation();
	}
	@Override
	public void update(EvoSimulator simulator) throws IllegalArgumentException, EvaluationException {
		super.update(simulator);
		if(children.size()!=0) {
			simulator.addEntity(children.get(0));
			simulator.addEntity(children.get(1));
			children.clear();
		}
	}

	private MOVE calculatePseudoMove(MOVE move) {
		String[] rs = move.toString().split("_");
		String action = rs[0];
		MOVE m = MOVE.valueOf(rs[1]);
		
		Entity e = this.observationManager.getClosestEntity().get(m);
		if(e==null)return MOVE.NEUTRAL;
		
		
		if(action.equals("CHASE")) {
			return util.Util.getNextMoveTo(this.node, e.node, ctrl.getMap());
		}
		else if(action.equals("RUNAWAY")) {
			return util.Util.getNextMoveAwayFrom(this.node, e.node, ctrl.getMap());
		}
		
		System.err.println("Action: "+action+" not found");
		return null;
		
	}
//	@Override
//	public void recieveActiveEntityReproductionInteraction(Entity e) {
//		if(this.getClass().equals(e.getClass())){//&&this.energy>=REPRODUCTION_COST && e.getEnergy()>=REPRODUCTION_COST) {
//
//			//System.out.println("reproductioning");
//			ctrl.getStatsManager().onReproduction();// SinglePointCrossover EqualOffspringCrossover
//			Pair<Genotype,Genotype> p = new SinglePointCrossover().crossover(this.genotype, ((MyIndividual)e).getGenotype());
//			children.add(MyIndividual.fromParents(p.first,ctrl,Math.max(this.generation, e.getGeneration())));
//			children.add(MyIndividual.fromParents(p.second,ctrl,Math.max(this.generation, e.getGeneration())));
//			
//			
//			
//			//this.decreaseEnergy(REPRODUCTION_COST);
//			//e.decreaseEnergy(REPRODUCTION_COST);
//		}
//		
//	}
	@Override
	public JSONObject toJSON() {
		JSONObject o = super.toJSON();
		o.getJSONObject("data").put("phenotype", phenotype.toJSON())
		   					   .put("genotype", genotype.toJSON())
		   					   .put("properties", new JSONObject())
		   					   .put("generation", this.generation);
		return o;
	}
	
	public static void maina(String args[]) {
		MyIndividual m;
		try {
			m = new MyIndividual("-2",new Node(0,0,255,255,255,null),null);
			System.out.println(m.phenotype.getVisualCode());
			
			HashMap<String,String>r = new HashMap<String,String>();
		} catch (JSONException | EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		r.put("countUP", "0");
//		r.put("countRIGHT", "1");
//		r.put("countLEFT", "0");
//		r.put("countDOWN", "0");
//	
//		System.out.println(m.getTheMove(r));
//		System.out.println(m.getTheMove(r));
//		System.out.println(m.getTheMove(r));
//		System.out.println(m.getTheMove(r));
		
		//new SimpleMazeFitnessTest(100).evaluate(m.toJSON());
		//System.out.println(m.toJSON().toString(4));
	}
	

}
