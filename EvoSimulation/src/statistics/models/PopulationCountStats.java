package statistics.models;

import simulator.model.EvoSimulator;
import statistics.StatsData;
import statistics.StatsManager;

public class PopulationCountStats extends StatsData{
	
	private int totalPopulation;
	private int alivePopulation;
	private int updateRate=20;
	private int currentTime=0;
	
	public PopulationCountStats(StatsManager manager) {
		super(manager);
		totalPopulation=0;
		alivePopulation=0;
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
			//dataset.addValue(Integer.valueOf(totalPopulation), "totalPopulation", Integer.valueOf(time));
			dataset.addValue(Integer.valueOf(simulator.getEntities().size()), "alivePopulation", Integer.valueOf(currentTime));
		}
	}
	
}
