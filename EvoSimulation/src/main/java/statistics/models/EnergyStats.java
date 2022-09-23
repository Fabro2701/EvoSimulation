package statistics.models;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.FoodEntity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class EnergyStats extends StatsData{

	private int currentTime=0;
	
	public EnergyStats(int updateRate) {
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
			float sum = simulator.getEntities().stream().map(e->e instanceof FoodEntity?((FoodEntity)e).getFoodAmount():3.f)
					.reduce(0.f, Float::sum);
		
			((DefaultCategoryDataset)dataset).addValue(sum, "energy", Integer.valueOf(currentTime));
		}
	}

}
