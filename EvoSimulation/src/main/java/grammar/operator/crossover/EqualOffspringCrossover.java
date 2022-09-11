package grammar.operator.crossover;

import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Genotype;
import util.Pair;

public class EqualOffspringCrossover extends CrossoverOperation{

	@Override
	public Pair<Genotype, Genotype> crossover(Genotype g1, Genotype g2) {
		Chromosome c1 = g1.getChromosome();
		Chromosome c2 = g2.getChromosome();

		Chromosome child1 = new Chromosome(c1);
		Chromosome child2 = new Chromosome(c2);
	
	
		return new Pair<Genotype, Genotype>(new Genotype(child1),new Genotype(child2));
	}

	

}
