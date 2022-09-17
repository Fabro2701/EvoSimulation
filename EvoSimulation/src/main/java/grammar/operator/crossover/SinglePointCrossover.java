package grammar.operator.crossover;

import simulator.RandomSingleton;
import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Genotype;
import util.Pair;

public class SinglePointCrossover extends CrossoverOperation{

	@Override
	public Pair<Genotype, Genotype> crossover(Genotype g1, Genotype g2) {
		
		Genotype gchild1 = new Genotype();
		Genotype gchild2 = new Genotype();
		for(int i=0;i<g1.size();i++) {
			Chromosome c1 = g1.getChromosome(i);
			Chromosome c2 = g2.getChromosome(i);
			
			int crossPoint = RandomSingleton.nextInt(c1.getLength());
			
			Chromosome child1 = new Chromosome(c1);
			Chromosome child2 = new Chromosome(c2);
			for(int j=crossPoint;j<child1.getLength();j++) {
				child1.setIntToCodon(j, c2.getCodon(j).getIntValue());
				child2.setIntToCodon(j, c1.getCodon(j).getIntValue());
			}
			gchild1.add(child1);
			gchild2.add(child2);
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
	
		return new Pair<Genotype, Genotype>(gchild1, gchild2);
	}

	

}
