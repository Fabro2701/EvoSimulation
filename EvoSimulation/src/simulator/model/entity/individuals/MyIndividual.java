package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.json.JSONObject;

import grammar.Grammar;
import grammar.Grammar.Symbol;
import grammar.operator.BasicOnePointOperator;
import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.map.Node;
import util.Pair;

public class MyIndividual extends GIndividual{
	private List<Entity>childs;
	public MyIndividual(MyIndividual e) {
		super(e.id, e.node);
		type = "mi";
		img = new ImageIcon("resources/entities/myentity.png").getImage();
		grammar = new Grammar("s");
		genotype = new Genotype(e.genotype.get(0));
		mutate();
		//if(id.equals("-1"))c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0, 1);
		LinkedList<Symbol> crom = genotype.get(0).parseGrammar(grammar);
		
		if(crom==null) {
			phenotype = new Phenotype();
		}
		else phenotype = new Phenotype(crom);
		
		childs = new ArrayList<Entity>();

	}
	public MyIndividual(Entity father, Genotype geno) {
		super(father.getId(), father.node);
		type = "mi";
		img = new ImageIcon("resources/entities/myentity.png").getImage();
		grammar = new Grammar("s");
		genotype = new Genotype(geno.get(0));
		mutate();
		//if(id.equals("-1"))c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0, 1);
		LinkedList<Symbol> crom = genotype.get(0).parseGrammar(grammar);
		
		if(crom==null) {
			phenotype = new Phenotype();
		}
		else phenotype = new Phenotype(crom);
		
		childs = new ArrayList<Entity>();

	}
	public MyIndividual(String id, Node n) {
		super(id, n);
		type = "mi";
		img = new ImageIcon("resources/entities/myentity.png").getImage();
		grammar = new Grammar("s");
		Chromosome c = new Chromosome(50);
		
		
		if(id.equals("-1"))c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,2,3,1,0,0,3, 1,0,1,0,1,1,0,0,1, 0,2,1,1,0,0,1,0, 4);
		LinkedList<Symbol> crom = c.parseGrammar(grammar);
		
		genotype = new Genotype(c);
		
		if(crom==null) {
			vanish();
			phenotype = new Phenotype();
		}
		else phenotype = new Phenotype(crom);
		
		childs = new ArrayList<Entity>();

		//if(id.equals("-1"))System.out.println(this.toJSON().toString(4));
	}
	public MyIndividual(String id, Node n, JSONObject genotype, JSONObject phenotype, float energy) {
		super(id, n);
		this.energy=energy;
		type = "mi";
		img = new ImageIcon("resources/entities/myentity.png").getImage();
		grammar = new Grammar("s");
		

		
		this.genotype = new Genotype(genotype);
		

		this.phenotype = new Phenotype(phenotype);
		
		childs = new ArrayList<Entity>();

		
		//if(id.equals("-1"))System.out.println(this.toJSON().toString(4));
	}
	public void mutate() {
		genotype.get(0).mutate(this.node.radiation);
	}
	@Override
	public void update(EvoSimulator simulator) {
		super.update(simulator);
		if((this.age/500000.0f)>RandomSingleton.nextFloat()) {
			//simulator.addEntity(new MyIndividual(this));
			//simulator.addEntity(new MyIndividual(this));
		}
		if(childs.size()!=0) {
			simulator.addEntity(childs.get(0));
			simulator.addEntity(childs.get(1));
			childs.clear();
		}
		
	}
	@Override
	public MOVE getTheMove(HashMap<String,String>observations) {
		MOVE move = phenotype.getNext(observations);
		if(move==null)return MOVE.NEUTRAL;		
		
		return move;
	}
	@Override
	public void recieveActiveEntityInteraction(Entity e) {
		if(this.reproductionRestTime<=0) {
			if(this.energy>=100.0f) {
				Pair<Genotype,Genotype> p = new BasicOnePointOperator().crossover(this.genotype, ((MyIndividual)e).getGenotype());
				childs.add(new MyIndividual(this,p.first));
				childs.add(new MyIndividual(this,p.second));
				reproductionRestTime=300;
			}
		}
	}
	@Override
	public JSONObject toJSON() {
		JSONObject o = super.toJSON();
		o.getJSONObject("data").put("phenotype", phenotype.toJSON())
		   .put("genotype", genotype.toJSON())
				   .put("energy", energy);
		return o;
	}
	
	public static void main(String args[]) {
		MyIndividual m = new MyIndividual("-1",new Node(0,0,255,255));
		System.out.println(m.phenotype.getVisualCode());
		
		HashMap<String,String>r = new HashMap<String,String>();
		
		r.put("u_f_d", "0");
		r.put("d_f_d", "1");
		r.put("l_f_d", "0");
		r.put("r_f_d", "0");
	
		System.out.println(m.getTheMove(r));
		System.out.println(m.getTheMove(r));
		System.out.println(m.getTheMove(r));
		System.out.println(m.getTheMove(r));
		
		System.out.println(m.toJSON().toString(4));
	}
	

}
