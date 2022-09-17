package grammar.operator.mutation;

import simulator.RandomSingleton;
import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Genotype;

public class SingleCodonFlipMutation extends MutationOperation{

	@Override
	public void mutate(Genotype g) {
		for(Chromosome c:g) {
			c.getCodon(RandomSingleton.nextInt(c.getLength())).setInt(RandomSingleton.nextInt(256));
		}
	}

}
