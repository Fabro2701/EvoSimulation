package statistics.models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class PopulationCountStats extends StatsData{

	private int currentTime=0;
	
	public PopulationCountStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();

	}
	
	@Override
	public void onRegister() {
		//simulator de arg para actualizar el num de entities en ese momento
		//System.out.println("registered");
	}

	@Override 
	public void onUpdate(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			long cont=simulator.getEntities().stream().filter(e->e instanceof MyIndividual).count();
		
			((DefaultCategoryDataset)dataset).addValue(Integer.valueOf(simulator.getEntities().size())-cont, "foodPopulation", Integer.valueOf(currentTime));
			((DefaultCategoryDataset)dataset).addValue(cont, "miPopulation", Integer.valueOf(currentTime));
			

		}
	}
	
}
