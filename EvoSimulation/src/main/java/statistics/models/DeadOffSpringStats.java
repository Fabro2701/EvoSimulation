package statistics.models;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class DeadOffSpringStats extends StatsData{

	private int currentTime=0;
	private int contGeneration,contCrossover;
	public DeadOffSpringStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
		contGeneration=contCrossover=0;
	}
	
	@Override
	public void onRegister() {
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {			
			((DefaultCategoryDataset)dataset).addValue((float)contGeneration/updateRate, "contGeneration", Integer.valueOf(currentTime));
			((DefaultCategoryDataset)dataset).addValue((float)contCrossover/updateRate, "contCrossover", Integer.valueOf(currentTime));
			contGeneration=contCrossover=0;
		}
	}
	@Override
	public void onDeadOffSpring(int type) {
		if(type==0)contGeneration++;
		else contCrossover++;
	}

}
