package statistics.models;

import java.util.IntSummaryStatistics;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class PopulationAgeStats extends StatsData{
	
	private float avg;
	private int max;
	private int alivePopulation;
	private int currentTime=0;
	
	public PopulationAgeStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
		//dataset = new DefaultBoxAndWhiskerXYDataset("depth");

		avg=0.0f;
		max=0;
		alivePopulation=0;

	}
	
	@Override
	public void onRegister() {
	
	}

	@Override 
	public void onUpdate(EvoSimulator simulator) {
		List<Entity>entities = simulator.getEntities();
		currentTime=simulator.getTime();
		double avg=0;
		int max=0;
		if(currentTime%updateRate==0) {
			Class<?>cls [] = {MyIndividual.class};
			
			for(Class<?>c:cls) {
				IntSummaryStatistics stats = entities.stream().filter(e->e.getClass().equals(c)).mapToInt(Entity::getAge).summaryStatistics();
				avg = stats.getAverage();
				max = stats.getMax();
				
				((DefaultCategoryDataset)dataset).addValue(avg, "avgAge"+c.getSimpleName(), Integer.valueOf(currentTime));
				System.out.println("max: "+max);
				((DefaultCategoryDataset)dataset).addValue(Float.valueOf(max), "maxAge"+c.getSimpleName(), Integer.valueOf(currentTime));

			}
			
			
			
			/*List<Integer> values = simulator.getEntities().stream().map(Entity::getAge).collect(Collectors.toList());

			
			((DefaultBoxAndWhiskerXYDataset)dataset).add(new Date(currentTime), BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values));*/
		}
		
	}
}
