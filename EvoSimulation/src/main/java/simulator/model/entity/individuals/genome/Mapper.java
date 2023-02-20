package simulator.model.entity.individuals.genome;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import grammar.AbstractGrammar.Symbol;
import simulator.model.entity.individuals.genome.PolymorphismController.VARIATION;

public interface Mapper {
	public abstract Object mapChromosome(Chromosome<?> c);
	public default LinkedList<Symbol> mapGrammar(Chromosome<Chromosome.Codon> c){return null;}
	public default HashSet<String> mapGenes(Chromosome<Boolean> c){return null;}
	public default Map<String,VARIATION> mapPolymorphisms(Chromosome<Float> c) {return null;}

}
