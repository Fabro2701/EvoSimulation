package simulator.model.entity.individuals.genome.operator.mutation;

import simulator.RandomSingleton;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;
import simulator.model.entity.individuals.genome.Chromosome.Codon;

public class SingleCodonFlipMutation extends MutationOperation{

	@Override
	public void mutate(Genotype g, float p) {
		for(Chromosome c:g) {
			for(int i=0;i<c.getLength();i++) {
				if(RandomSingleton.nextFloat()<=p) {
					c.flip(i);
				}
			}
		}
	}
}
