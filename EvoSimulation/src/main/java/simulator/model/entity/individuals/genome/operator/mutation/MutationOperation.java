package simulator.model.entity.individuals.genome.operator.mutation;

import simulator.model.entity.individuals.genome.Genotype;

public abstract class MutationOperation {
	public abstract void mutate(Genotype g, float p);

}
