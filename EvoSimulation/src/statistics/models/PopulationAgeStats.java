package statistics.models;

import java.util.List;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import statistics.StatsData;
import statistics.StatsManager;
import statistics.visualizers.StatsVisualizer;

public class PopulationAgeStats extends StatsData{
	
	private float avg;
	private int max;
	private int alivePopulation;
	private int updateRate=20;
	private int currentTime=0;
	
	public PopulationAgeStats(StatsManager manager) {
		super(manager);
		avg=0.0f;
		max=0;
		alivePopulation=0;

	}
	
	@Override
	public void onRegister() {
		//simulator de arg para actualizar el num de entities en ese momento
		//System.out.println("registered");
	}

	@Override 
	public void onUpdate(EvoSimulator simulator) {
		List<Entity>entities = simulator.getEntities();
		currentTime=simulator.getTime();
		int avg=0;
		int max=0;
		if(currentTime%updateRate==0) {
			for(Entity e:entities) {
				max=Math.max(max, e.getAge());
				avg+=e.getAge();
			}avg/=entities.size()>0?entities.size():10000000;
			dataset.addValue(Float.valueOf(avg), "avgAge", Integer.valueOf(currentTime));
			dataset.addValue(Float.valueOf(max), "maxAge", Integer.valueOf(currentTime));
		}
		
		for(StatsVisualizer v:visualizers) {
			v.update();
		}
	}
}
