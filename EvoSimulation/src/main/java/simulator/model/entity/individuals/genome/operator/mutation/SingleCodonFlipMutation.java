package simulator.model.entity.individuals.genome.operator.mutation;

import simulator.RandomSingleton;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;

public class SingleCodonFlipMutation extends MutationOperation{

	@Override
	public void mutate(Genotype g, float p) {
		for(Chromosome<Chromosome.Codon> c:g) {
			if(RandomSingleton.nextFloat()<=p)
				c.getCodon(RandomSingleton.nextInt(c.getLength())).setInt(RandomSingleton.nextInt(256));
		}
	}

}
