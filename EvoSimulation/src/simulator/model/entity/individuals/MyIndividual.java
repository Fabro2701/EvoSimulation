package simulator.model.entity.individuals;

import java.util.HashMap;

import grammar.Grammar;
import simulator.Constants.MOVE;
import simulator.model.map.Node;

public class MyIndividual extends GIndividual{

	public MyIndividual(String id, Node n) {
		super(id, n);
		grammar = new Grammar("s");
		Chromosome c = new Chromosome(30);

		c.setIntToCodon(0, 1);
		c.setIntToCodon(1, 1);
		c.setIntToCodon(2, 1);
		c.setIntToCodon(3, 0);
		c.setIntToCodon(4, 1);
		c.setIntToCodon(5, 1);
		c.setIntToCodon(6, 0);
		c.setIntToCodon(7, 2);
		c.setIntToCodon(8, 0);
		c.setIntToCodon(9, 2);
		c.setIntToCodon(10, 1);
		c.setIntToCodon(11, 1);
		c.setIntToCodon(12, 0);
		c.setIntToCodon(13, 3);
		c.setIntToCodon(14, 0);
		c.setIntToCodon(15, 3);

		c.setIntToCodon(16, 1);
		c.setIntToCodon(17, 1);
		c.setIntToCodon(18, 0);
		c.setIntToCodon(19, 1);
		c.setIntToCodon(20, 0);
		c.setIntToCodon(21, 1);

		c.setIntToCodon(22, 1);
		c.setIntToCodon(23, 1);
		c.setIntToCodon(24, 0);
		c.setIntToCodon(25, 0);
		c.setIntToCodon(26, 0);
		c.setIntToCodon(27, 0);
		genotype = new Genotype(c);
		phenotype = new Phenotype(c.parseGrammar(grammar));
	}

	
	
	@Override
	public MOVE getTheMove() {
		HashMap<String,Integer> m = new HashMap<String,Integer>();
		m.put("u_f_d", 1);
		m.put("d_f_d", 0);
		m.put("l_f_d", 1);
		m.put("r_f_d", 1);
		String move = phenotype.getNext(m).getRealValue();
		
		return MOVE.valueOf(move);
	}
	
	public static void main(String args[]) {
		MyIndividual m = new MyIndividual("fa",null);
		System.out.println(m.phenotype.getVisualCode());
		m.getTheMove();
		m.getTheMove();
		m.getTheMove();
		m.getTheMove();
		m.getTheMove();
	}

}
