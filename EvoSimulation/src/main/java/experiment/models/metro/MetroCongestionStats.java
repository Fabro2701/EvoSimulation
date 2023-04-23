package experiment.models.metro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

public class MetroCongestionStats extends StatsData{

	private int currentTime=0;
	
	public MetroCongestionStats(int updateRate, boolean serialize) {
		super(updateRate, serialize);
		dataset = new DefaultCategoryDataset();
		this.serialize = false;
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			List<Entity>listTotal = simulator.getEntities().stream().filter(e->e instanceof PasiveEntity).collect(Collectors.toList());
			/*for(Entity e:listTotal) {
				((DefaultCategoryDataset)dataset).addValue((Number) e.getAttribute("congestion"), (String)e.getAttribute("station"), Integer.valueOf(currentTime));
			}*/
			double sum=0d;
			for(Entity e:listTotal) {
				sum += ((Number)e.getAttribute("congestion")).doubleValue();
			}
			((DefaultCategoryDataset)dataset).addValue(sum,"avg", Integer.valueOf(currentTime));

		}
	}

}
