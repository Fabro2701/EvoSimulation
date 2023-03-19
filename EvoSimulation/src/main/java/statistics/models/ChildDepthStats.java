package statistics.models;

import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class ChildDepthStats extends StatsData{

	private int currentTime=0;
	
	public ChildDepthStats(int updateRate, boolean serialize) {
		super(updateRate, serialize);
		dataset = new DefaultCategoryDataset();
	}
	
	@Override
	public void onRegister() {
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			List<Entity>entities = simulator.getEntities();
			Class<?>cls [] = {MyIndividual.class};
			int max =0;
			double avg=0.0;
			for(Class<?>c:cls) {
				IntSummaryStatistics stats = entities.stream().filter(e->e.getClass().equals(c)).mapToInt(Entity::getGeneration).summaryStatistics();
				avg = stats.getAverage();
				max = stats.getMax();
				((DefaultCategoryDataset)dataset).addValue(avg, "avgGen"+c.getSimpleName(), Integer.valueOf(currentTime));
				((DefaultCategoryDataset)dataset).addValue(Float.valueOf(max), "maxGen"+c.getSimpleName(), Integer.valueOf(currentTime));

			}
			//List<Integer> values = simulator.getEntities().stream().map(Entity::getGeneration).collect(Collectors.toList());
			//((DefaultBoxAndWhiskerXYDataset)dataset).add(new Date(currentTime), BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values));
		}
	}

	
}
