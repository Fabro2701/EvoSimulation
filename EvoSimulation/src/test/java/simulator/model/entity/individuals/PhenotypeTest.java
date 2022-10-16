package simulator.model.entity.individuals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import grammar.AbstractGrammar.Symbol;
import grammar.StandardGrammar;
import simulator.Constants.MOVE;

class PhenotypeTest {

	@Test
	void testGetVisualCode() {
		StandardGrammar g = new StandardGrammar();
		g.parseBNF("default");
		
		Chromosome c = new Chromosome(50);
		c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0,  0);
		
		
		LinkedList<Symbol> l = g.parse(c);
		Phenotype pt = new Phenotype(l);
		
		String exp = "if(u_f_d>d_f_d){\n"
				+ "    \"UP\";\n"
				+ "}\n"
				+ "else{\n"
				+ "    if(d_f_d){\n"
				+ "        \"DOWN\";\n"
				+ "    }\n"
				+ "}\n"
				+ "if(r_f_d>l_f_d){\n"
				+ "    \"RIGHT\";\n"
				+ "}\n"
				+ "else{\n"
				+ "    if(l_f_d){\n"
				+ "        \"LEFT\";\n"
				+ "    }\n"
				+ "    else{\n"
				+ "        \"RIGHT\";\n"
				+ "    }\n"
				+ "}\n"
				+ "";
		assertEquals(exp,pt.getVisualCode());
	}
	@Test
	public void testGetNext() {
		StandardGrammar g = new StandardGrammar();
		g.parseBNF("default");
		
		
		Chromosome c = new Chromosome(50);
		c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0,  0);
		
		
		LinkedList<Symbol> l = g.parse(c);
		Phenotype pt = new Phenotype(l);
		
		HashMap<String,String>r = new HashMap<String,String>();
		
		r.put("u_f_d", "0");
		r.put("d_f_d", "1");
		r.put("l_f_d", "1");
		r.put("r_f_d", "2");
		
		MOVE m1 = pt.getNext(r);
		MOVE m2 = pt.getNext(r);
		r.put("u_f_d", "3");
		MOVE m3 = pt.getNext(r);
		r.put("r_f_d", "0");
		r.put("l_f_d", "0");
		MOVE m4 = pt.getNext(r);
		
		assertEquals(MOVE.DOWN,m1);
		assertEquals(MOVE.RIGHT,m2);
		assertEquals(MOVE.UP,m3);
		assertEquals(MOVE.RIGHT,m4);
	}

}
