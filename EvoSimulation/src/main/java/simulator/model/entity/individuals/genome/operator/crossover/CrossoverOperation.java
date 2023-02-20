package simulator.model.entity.individuals.genome.operator.crossover;

import simulator.model.entity.individuals.genome.Genotype;
import util.Pair;

public abstract class CrossoverOperation {
	public abstract Pair<Genotype,Genotype> crossover(Genotype g1, Genotype g2);
}
