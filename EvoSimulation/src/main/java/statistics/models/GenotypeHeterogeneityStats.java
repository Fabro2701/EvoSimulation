package statistics.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.FoodEntity;
import simulator.model.entity.individuals.Genotype;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;
import util.Util;

public class GenotypeHeterogeneityStats extends StatsData{

	private int currentTime=0;
	
	public GenotypeHeterogeneityStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
	}
	
	@Override
	public void onRegister() {
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			List<Genotype> genos = simulator.getEntities().stream().filter(e->!(e instanceof FoodEntity)).map(e->((MyIndividual)e).getGenotype())
					.collect(Collectors.toList());
			
			ArrayList<ArrayList<Float>> matrix = Util.genotypeSimilarityMatrix(genos);
			float avg = matrix.stream().map(l->l.stream().reduce(0.f,Float::sum)).reduce(0.f,Float::sum) / (genos.size()*(genos.size()-1));
			
		
			((DefaultCategoryDataset)dataset).addValue(avg, "Heterogeneity", Integer.valueOf(currentTime));
		}
	}

}
