package statistics.models;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.FoodEntity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class MutationReproductionStats extends StatsData{

	private int currentTime=0;
	private int mutations;
	private int reproductions;
	public MutationReproductionStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
		mutations=0;
		reproductions=0;
	}
	
	@Override
	public void onRegister() {
	}

	@Override 
	public void onUpdate(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			
		
			((DefaultCategoryDataset)dataset).addValue((float)mutations/updateRate, "mutations", Integer.valueOf(currentTime));
			((DefaultCategoryDataset)dataset).addValue((float)reproductions/updateRate, "reproductions", Integer.valueOf(currentTime));
			mutations=reproductions=0;
		}
	}
	@Override
	public void onMutation() {
		mutations++;
	}
	@Override
	public void onReproduction() {
		reproductions++;
	}
	
}
