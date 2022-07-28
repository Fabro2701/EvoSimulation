package grammar.operator;

import simulator.model.entity.individuals.Genotype;
import util.Pair;

public abstract class GrammarOperator {
	public abstract Pair<Genotype,Genotype> crossover(Genotype g1, Genotype g2);
}
