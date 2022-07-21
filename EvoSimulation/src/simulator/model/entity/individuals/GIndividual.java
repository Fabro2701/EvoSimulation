package simulator.model.entity.individuals;

import grammar.Grammar;
import simulator.Constants.MOVE;
import simulator.model.map.Node;

public abstract class GIndividual extends AbstractIndividual{
	protected Grammar grammar;
	protected Genotype genotype;
	protected Phenotype phenotype;
	
	public GIndividual(String id, Node n) {
		super(id, n);
		// TODO Auto-generated constructor stub
	}


}
