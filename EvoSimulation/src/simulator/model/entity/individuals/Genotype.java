package simulator.model.entity.individuals;

import java.util.ArrayList;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Genotype extends ArrayList<Chromosome>{
	public Genotype(Chromosome c) {
		super();
		this.add(c);
	}
}
