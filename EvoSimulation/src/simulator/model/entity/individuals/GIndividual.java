package simulator.model.entity.individuals;

import grammar.Grammar;
import simulator.Constants.MOVE;
import simulator.model.map.Node;

public abstract class GIndividual extends AbstractIndividual{
	protected Grammar grammar;
	protected Genotype genotype;
	public Grammar getGrammar() {
		return grammar;
	}

	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

	public Genotype getGenotype() {
		return genotype;
	}

	public void setGenotype(Genotype genotype) {
		this.genotype = genotype;
	}

	public Phenotype getPhenotype() {
		return phenotype;
	}

	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}

	protected Phenotype phenotype;
	
	public GIndividual(String id, Node n) {
		super(id, n);
		// TODO Auto-generated constructor stub
	}


}
