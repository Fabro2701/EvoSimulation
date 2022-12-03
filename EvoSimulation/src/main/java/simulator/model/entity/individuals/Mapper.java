package simulator.model.entity.individuals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import grammar.AbstractGrammar.Symbol;

public interface Mapper {
	public abstract Object mapChromosome(Chromosome c);
	public default LinkedList<Symbol> mapGrammar(Chromosome c){return null;}
	public default HashSet<String> map(Chromosome c){return null;}

}
