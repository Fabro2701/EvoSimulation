package statistics.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import simulator.model.entity.Entity;
import statistics.StatsData;
import statistics.visualizers.StatsVisualizer;

public class PopulationAgeStats extends StatsData{
	
	private float avg;
	private int max;
	private int alivePopulation;
	private int updateRate=20;
	private int currentTime=0;
	
	public PopulationAgeStats() {
		super();
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
	public void onEntityAdded(int time) {
		//System.out.println("entity added");convertir luego en loggs
		alivePopulation++;		

		
		
		for(StatsVisualizer v:visualizers) {
			v.update();
		}
	}
	@Override 
	public void onAdvance(int time, List<Entity>entities) {
		currentTime=time;
		int avg=0;
		int max=0;
		if(time%updateRate==0) {
			for(Entity e:entities) {
				max=Math.max(max, e.getAge());
				avg+=e.getAge();
			}avg/=entities.size();
			dataset.addValue(Float.valueOf(avg), "avgAge", Integer.valueOf(currentTime));
			dataset.addValue(Float.valueOf(max), "maxAge", Integer.valueOf(currentTime));
		}
		
		for(StatsVisualizer v:visualizers) {
			v.update();
		}
	}
	@Override
	public void onEntityDead(int time, Entity e) {
		
		/*alivePopulation--;
	
		if(e.getAge()>max&&e.shouldInteract()) {
			//System.out.println("max: "+e.getAge());
			//System.out.println(((GIndividual)e).getPhenotype().getVisualCode());
		}
		max = max>e.getAge()?max:e.getAge();
		
		
		//dataset.addValue(Float.valueOf(avg/alivePopulation), "avgAge", Integer.valueOf(currentTime));
		dataset.addValue(Float.valueOf(max), "maxAge", Integer.valueOf(currentTime));
		for(StatsVisualizer v:visualizers) {
			v.update();
		}*/
	}
	@Override
	public HashMap<String, ArrayList<Integer>> getLinearData(){
		return linearData;
	}
}
