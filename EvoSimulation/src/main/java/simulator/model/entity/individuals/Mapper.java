package simulator.model.entity.individuals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import grammar.AbstractGrammar.Symbol;
import simulator.model.entity.individuals.PolymorphismController.VARIATION;

public interface Mapper {
	public abstract Object mapChromosome(Chromosome<?> c);
	public default LinkedList<Symbol> mapGrammar(Chromosome<Chromosome.Codon> c){return null;}
	public default HashSet<String> mapGenes(Chromosome<Boolean> c){return null;}
	public default Map<String,VARIATION> mapPolymorphisms(Chromosome<Integer> c) {return null;}

}
