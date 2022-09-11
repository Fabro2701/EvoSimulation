package grammar.operator.crossover;

import simulator.RandomSingleton;
import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Genotype;
import util.Pair;

public class SinglePointCrossover extends CrossoverOperation{

	@Override
	public Pair<Genotype, Genotype> crossover(Genotype g1, Genotype g2) {
		
		
		Chromosome c1 = g1.getChromosome();
		Chromosome c2 = g2.getChromosome();
		
		int crossPoint = RandomSingleton.nextInt(c1.getLength());
		
		Chromosome child1 = new Chromosome(c1);
		Chromosome child2 = new Chromosome(c2);
		for(int i=crossPoint;i<child1.getLength();i++) {
			child1.setIntToCodon(i, c2.getCodon(i).getIntValue());
			child2.setIntToCodon(i, c1.getCodon(i).getIntValue());
		}
		
		/*int crossPoint=0;
		Chromosome c1 = g1.getChromosome();
		Chromosome c2 = g2.getChromosome();
		for(int i = 0;i<c1.getLength();i++) {
			if(c1.getCodon(i).getModValue()!=c2.getCodon(i).getModValue())break;
			else {
				crossPoint=i;
			}
		}
		Chromosome child1 = new Chromosome(c1);
		Chromosome child2 = new Chromosome(c2);
		for(int i=crossPoint+1;i<child1.getLength();i++) {
			child1.setIntToCodon(i, c2.getCodon(i).getIntValue());
			child2.setIntToCodon(i, c1.getCodon(i).getIntValue());
		}*/
	
		return new Pair<Genotype, Genotype>(new Genotype(child1),new Genotype(child2));
	}

	

}
