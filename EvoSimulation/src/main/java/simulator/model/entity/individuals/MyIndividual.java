package simulator.model.entity.individuals;

import static simulator.Constants.CHROMOSOME_LENGTH;
import static simulator.Constants.RECOVERY_REST_TIME;
import static simulator.Constants.REPRODUCTION_COST;
import static simulator.Constants.ATTACKER_ENERGY_COST;
import static simulator.Constants.ATTACKED_ENERGY_COST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import grammar.operator.crossover.SinglePointCrossover;
import grammar.operator.mutation.SingleCodonFlipMutation;
import model.Constants;
import simulator.Constants.ACTION;
import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.map.Node;
import util.Pair;

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
		moveGrammar = ctrl.getCommonGrammar();
		actionGrammar = ctrl.getCommonGrammar2();
		children = new ArrayList<Entity>();
	}
	/**
	 * Main constructor used by factories for creating random entities
	 * @param id
	 * @param n node
	 * @param ctrl
	 */
	public MyIndividual(String id, Node node, Controller ctrl) {
		this(ctrl, id, node);
		
		genotype = new Genotype();
		phenotype = new Phenotype();
		for(int i=0; i<Constants.PLOIDY;i++) {
			Chromosome c = new Chromosome(CHROMOSOME_LENGTH);
			genotype.addChromosome(c);
			LinkedList<Symbol> crom=null;
			if(i==0) crom = moveGrammar.parse(c);
			else if(i==1) crom = actionGrammar.parse(c);
			phenotype.setSymbol(i, crom);
			if(phenotype.valid==false) {
				ctrl.getStatsManager().onDeadOffSpring(0);
				vanish();
			}
		}
	}
	/**
	 * Constructor invoked by reproduction methods 
	 * @param geno
	 * @param ctrl
	 * @param generation The greatest generation of parents
	 */
	public MyIndividual(Genotype geno, Controller ctrl, int generation) {
		this(ctrl, ctrl.getNextId(), ctrl.randomNode());
		genotype = new Genotype(geno);
		phenotype = new Phenotype();
		mutate();
		for(int i=0; i<Constants.PLOIDY;i++) {
			Chromosome c = geno.getChromosome(i);
			LinkedList<Symbol> crom=null;
			if(i==0) crom = moveGrammar.parse(c);
			else if(i==1) crom = actionGrammar.parse(c);
			phenotype.setSymbol(i, crom);
			if(phenotype.valid==false) {
				ctrl.getStatsManager().onDeadOffSpring(0);
				vanish();
			}
		}
		
		this.generation =  generation+1;

	}
	/**
	 * Constructor invoked by initializers
	 * @param geno
	 * @param ctrl
	 * @param generation The greatest generation of parents
	 */
	public MyIndividual(String id, Node node, Chromosome c, Controller ctrl) {
		this(ctrl, id, node);
		//genotype = new Genotype(c); pending
		LinkedList<Symbol> crom = moveGrammar.parse(genotype.get(0));
		
		if(crom==null) {
			ctrl.getStatsManager().onDeadOffSpring(0);
			vanish();
			phenotype = new Phenotype();
		}
		else {
			
			///phenotype = new Phenotype(crom);pending
			//System.out.println(phenotype.getVisualCode());
		}
		
	}
	public MyIndividual(String id, Node node, String code, Controller ctrl) {
		this(ctrl, id, node);
		
		genotype = null;
		phenotype = new Phenotype(code);
	}
	public MyIndividual(String id, Node node, JSONObject genotype, JSONObject phenotype, float energy, Controller ctrl) {
		this(ctrl, id, node);
		this.energy=energy;
		this.genotype = new Genotype(genotype);
		
		if(phenotype.getString("code").equals("")) {
			vanish();
			this.phenotype = new Phenotype();
		}
		else this.phenotype = new Phenotype(phenotype);
		
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
	@Override
	public MOVE getTheMove() {
		MOVE move = phenotype.getNextMove(this.observationManager.getVariables());
		if(move==null)return MOVE.NEUTRAL;
		
		if(move.isPseudo()) {
			return calculatePseudoMove(move);
		}
		return move;
	}
	@Override
	public ACTION getAction() {		
		ACTION action = phenotype.getNextAction(this.observationManager.getVariables());
		if(action==null)return ACTION.NOTHING;
		return action;
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
	@Override
	public void recieveActiveEntityReproductionInteraction(Entity e) {
		if(this.getReproductionRestTime()<=0) {
			if(this.getClass().equals(e.getClass())){//&&this.energy>=REPRODUCTION_COST && e.getEnergy()>=REPRODUCTION_COST) {

				//System.out.println("reproductioning");
				ctrl.getStatsManager().onReproduction();// SinglePointCrossover EqualOffspringCrossover
				Pair<Genotype,Genotype> p = new SinglePointCrossover().crossover(this.genotype, ((MyIndividual)e).getGenotype());
				children.add(new MyIndividual(p.first,ctrl,Math.max(this.generation, e.getGeneration())));
				children.add(new MyIndividual(p.second,ctrl,Math.max(this.generation, e.getGeneration())));
				
				this.setReproductionRestTime(RECOVERY_REST_TIME);
				e.setReproductionRestTime(RECOVERY_REST_TIME);
				
				this.decreaseEnergy(REPRODUCTION_COST);
				e.decreaseEnergy(REPRODUCTION_COST);
			}
		}
	}
	@Override
	public void recieveActiveEntityAttackInteraction(Entity e) {
		//System.out.println("attacking");
		
		this.decreaseEnergy(ATTACKED_ENERGY_COST);
		e.decreaseEnergy(ATTACKER_ENERGY_COST);
		
		if(this.energy <= 0f) {
			this.eatMe((ActiveEntity)e);
		}
	}
	@Override
	public JSONObject toJSON() {
		JSONObject o = super.toJSON();
		o.getJSONObject("data").put("phenotype", phenotype.toJSON())
		   .put("genotype", genotype.toJSON())
				   .put("energy", energy)
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
