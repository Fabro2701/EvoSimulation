package grammar.operator.crossover;

import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Genotype;
import util.Pair;

public class EqualOffspringCrossover extends CrossoverOperation{

	@Override
	public Pair<Genotype, Genotype> crossover(Genotype g1, Genotype g2) {
		Genotype gchild1 = new Genotype();
		Genotype gchild2 = new Genotype();
		for(int i=0;i<g1.size();i++) {
			Chromosome c1 = g1.getChromosome(i);
			Chromosome c2 = g2.getChromosome(i);

			gchild1.add(new Chromosome(c1));
			gchild2.add(new Chromosome(c2));
		}
		
	
		
		return new Pair<Genotype, Genotype>(gchild1, gchild2);
	}

	

}
