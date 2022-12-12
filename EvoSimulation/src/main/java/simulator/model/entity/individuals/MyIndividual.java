package simulator.model.entity.individuals;


import static simulator.Constants.CHROMOSOME_LENGTH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import grammar.operator.mutation.SingleCodonFlipMutation;
import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import util.Util;

public class MyIndividual extends GIndividual{
	protected List<Entity>children;

	/**
	 * Base Constructor
	 * @param ctrl
	 * @param id
	 * @param n
	 */
	private MyIndividual(Controller ctrl, String id, Node n) {
		super(id, n, ctrl);
		type = "mi";
		//img = new ImageIcon("resources/entities/myentity.png").getImage();
		if(this.img == null)this.img = ImageController.getImage(this.getClass());
		children = new ArrayList<Entity>();
	}
	/**
	 * Main constructor used by factories to create random entities
	 * @param id
	 * @param n node
	 * @param ctrl
	 */
	public MyIndividual(String id, Node node, Controller ctrl) {
		this(ctrl, id, node);
		
		genotype = new Genotype();
		phenotype = new Phenotype();
		for(String key:grammars.keySet()) {
			Chromosome<Chromosome.Codon> c = new Chromosome<Chromosome.Codon>(CHROMOSOME_LENGTH, Chromosome.Codon::new);
			genotype.addChromosome(c);
			LinkedList<Symbol> crom = (LinkedList<Symbol>) grammars.get(key).mapChromosome(c);
			phenotype.setSymbol(key, crom);
			if(phenotype.valid==false) {
				ctrl.getStatsManager().onDeadOffSpring(0);
				dispose();
			}
		}
		Chromosome<Boolean> c = new Chromosome<Boolean>(CHROMOSOME_LENGTH, ()->RandomSingleton.nextBoolean());
		GIndividual.Genes genesMapper = new GIndividual.Genes();
		HashSet<String> genes = genesMapper.mapGenes(c);
		phenotype.setGenes(genes);
	}
	/**
	 * Constructor invoked by reproduction methods 
	 * @param geno
	 * @param ctrl
	 * @param generation The greatest generation of parents
	 */
	public static MyIndividual fromParents(Genotype geno, Controller ctrl, int generation) {
		MyIndividual ind = new MyIndividual(ctrl, ctrl.getNextId(), ctrl.randomNode());
		ind.genotype = new Genotype(geno);
		ind.phenotype = new Phenotype();
		ind.mutate();
		for(String key:ind.grammars.keySet()) {
			Chromosome c = new Chromosome(CHROMOSOME_LENGTH);
			ind.genotype.addChromosome(c);
			LinkedList<Symbol> crom=null;
			crom = ind.grammars.get(key).parse(c);
			ind.phenotype.setSymbol(key, crom);
			if(ind.phenotype.valid==false) {
				ctrl.getStatsManager().onDeadOffSpring(0);
				ind.dispose();
			}
		}
		
		ind.generation =  generation+1;
		return ind;
	}

	public static MyIndividual fromChromosome(String id, Node node, List<Chromosome>cs, Controller ctrl) {
		Objects.requireNonNull(cs);
		
		MyIndividual ind = new MyIndividual(ctrl, id, node);
		ind.genotype = new Genotype();
		ind.phenotype = new Phenotype();
		
		assert cs.size()==ind.grammars.size();
		
		int i=0;
		for(String key:ind.grammars.keySet()) {
			ind.genotype.addChromosome(cs.get(i));
			LinkedList<Symbol> crom = ind.grammars.get(key).parse(cs.get(i));
			ind.phenotype.setSymbol(key, crom);
			if(ind.phenotype.valid==false) {
				ctrl.getStatsManager().onDeadOffSpring(0);
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
	public MyIndividual(String id, Node node, String code, Controller ctrl) {
		this(ctrl, id, node);
		
		genotype = null;
		phenotype = new Phenotype(code);
	}
	public MyIndividual(String id, Node node, JSONObject genotype, JSONObject phenotype, float energy, Controller ctrl) {
		this(ctrl, id, node);
		this.genotype = new Genotype(genotype);
		
		if(phenotype.getString("code").equals("")) {
			dispose();
			this.phenotype = new Phenotype();
		}
		else this.phenotype = new Phenotype(phenotype);
		
	}
	public Node nextNodeTowards(Map map, Entity entity) {
		Node node = entity.node;
		int x = (node.x-this.node.x); if(x!=0)x=x>0?1:-1;
		int y = (node.y-this.node.y); if(y!=0)y=y>0?1:-1;
		return map.getNodeAt(this.node.x+x, this.node.y+y);
	}
	public Entity getEntityClosestAttribute(EvoSimulator simulator, String attribute) {
		List<Entity>entities = simulator.getEntities();
		Entity entity=null;
		double mindist = Double.MAX_VALUE;
		double dist;
		for(Entity e:entities) {
			if(e instanceof PasiveEntity &&e.hasAttribute(attribute)&&(boolean) e.getAttribute(attribute) && (dist=Util.nodeDistance(node, e.node))<mindist) {
				mindist = dist;
				entity = e;
			}
		}
		return entity;
	}
	public Entity getEntityRandomAttribute(EvoSimulator simulator, String attribute) {
		Entity entity=null;
		List<Entity>entities = simulator.getEntities().stream().filter(e->e instanceof PasiveEntity &&e.hasAttribute(attribute)&&(boolean) e.getAttribute(attribute)).collect(Collectors.toList());
		if(entities.size()>0) 
			entity = entities.get(RandomSingleton.nextInt(entities.size()));
		return entity;
	}
	public void mutate() {
		if(RandomSingleton.nextFloat()<this.node.radiation+0.01f) {
			new SingleCodonFlipMutation().mutate(genotype);
			ctrl.getStatsManager().onMutation();
		}
	}
	@Override
	public void update(EvoSimulator simulator) {
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
				   .put("generation", this.generation);
		return o;
	}
	
	public static void maina(String args[]) {
		MyIndividual m = new MyIndividual("-2",new Node(0,0,255,255,255,null),null);
		System.out.println(m.phenotype.getVisualCode());
		
		HashMap<String,String>r = new HashMap<String,String>();
		
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
