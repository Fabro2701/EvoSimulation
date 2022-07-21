package simulator.model.entity.individuals;

import java.util.HashMap;

import javax.swing.ImageIcon;

import grammar.Grammar;
import simulator.Constants.MOVE;
import simulator.model.map.Node;

public class MyIndividual extends GIndividual{

	public MyIndividual(String id, Node n) {
		super(id, n);
		type = "mi";
		img = new ImageIcon("resources/entities/myentity.png").getImage();
		grammar = new Grammar("s");
		Chromosome c = new Chromosome(100);

		c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0, 1);
		
		genotype = new Genotype(c);
		phenotype = new Phenotype(c.parseGrammar(grammar));
	}

	
	
	@Override
	public MOVE getTheMove(HashMap<String,Integer>observations) {
		
		String move = phenotype.getNext(observations).getRealValue();
		
		return MOVE.valueOf(move);
	}
	
	public static void main(String args[]) {
		MyIndividual m = new MyIndividual("fa",null);
		System.out.println(m.phenotype.getVisualCode());
		
		HashMap<String,Integer>r = new HashMap<String,Integer>();
		
		r.put("u_f_d", 0);
		r.put("d_f_d", 0);
		r.put("l_f_d", 0);
		r.put("r_f_d", 0);
	
		System.out.println(m.getTheMove(r));
		System.out.println(m.getTheMove(r));
		System.out.println(m.getTheMove(r));
		System.out.println(m.getTheMove(r));
	}

}
