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
			for(Entity e:listTotal) {
				((DefaultCategoryDataset)dataset).addValue((Number) e.getAttribute("congestion"), (String)e.getAttribute("station"), Integer.valueOf(currentTime));
			}
			/*if(this.groupby) {
				List<Entity>listTotal = simulator.getEntities().stream().filter(e->e instanceof MyIndividual).collect(Collectors.toList());
				Map<Object, List<Entity>>list = listTotal.stream().collect(Collectors.groupingBy(Entity.groupF));
				
				for(Object id:list.keySet()) {
					long count = list.get(id).stream().filter((Entity m)->m.hasAttribute(this.att)&&(Boolean)m.getAttribute(this.att)).count();
					((DefaultCategoryDataset)dataset).addValue(count, (String)id, Integer.valueOf(currentTime));
				}
			}
			else {
				long count = simulator.getEntities().stream().filter(e->e instanceof MyIndividual&&e.hasAttribute(this.att)&&(Boolean)e.getAttribute(this.att)).count();				
				((DefaultCategoryDataset)dataset).addValue(count, MyIndividual.class.getSimpleName(), Integer.valueOf(currentTime));
				
			}*/

		}
	}

}
