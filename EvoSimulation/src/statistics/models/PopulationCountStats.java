package statistics.models;

import java.util.ArrayList;
import java.util.HashMap;

import statistics.StatsData;
import statistics.StatsObserver;
import statistics.visualizers.StatsVisualizer;

public class PopulationCountStats extends StatsData{
	
	private int totalPopulation;
	private int alivePopulation;
	private int updateRate=20;
	private int currentTime=0;
	
	public PopulationCountStats() {
		super();
		totalPopulation=0;
		alivePopulation=0;
		linearData = new HashMap<String,ArrayList<Integer>>();
		linearData.put("time", new ArrayList<Integer>());
		linearData.put("totalPopulation", new ArrayList<Integer>());
		linearData.put("alivePopulation", new ArrayList<Integer>());
	}
	
	@Override
	public void onRegister() {
		//simulator de arg para actualizar el num de entities en ese momento
		//System.out.println("registered");
	}
	@Override
	public void onEntityAdded(int time) {
		//System.out.println("entity added");convertir luego en loggs
		totalPopulation++;
		alivePopulation++;		
		linearData.get("time").add(time);
		linearData.get("totalPopulation").add(totalPopulation);
		linearData.get("alivePopulation").add(alivePopulation);
		
		dataset.addValue(Integer.valueOf(totalPopulation), "totalPopulation", Integer.valueOf(currentTime));
		dataset.addValue(Integer.valueOf(alivePopulation), "alivePopulation", Integer.valueOf(currentTime));
		for(StatsVisualizer v:visualizers) {
			v.update();
		}
	}
	@Override 
	public void onAdvance(int time) {
		currentTime=time;
		if(time%updateRate==0) {
			dataset.addValue(Integer.valueOf(totalPopulation), "totalPopulation", Integer.valueOf(time));
			dataset.addValue(Integer.valueOf(alivePopulation), "alivePopulation", Integer.valueOf(time));
		}
	}
	@Override
	public void onEntityVanished(int time) {
		alivePopulation--;
		dataset.addValue(Integer.valueOf(totalPopulation), "totalPopulation", Integer.valueOf(currentTime));
		dataset.addValue(Integer.valueOf(alivePopulation), "alivePopulation", Integer.valueOf(currentTime));
		for(StatsVisualizer v:visualizers) {
			v.update();
		}
	}
	@Override
	public HashMap<String, ArrayList<Integer>> getLinearData(){
		return linearData;
	}
}
