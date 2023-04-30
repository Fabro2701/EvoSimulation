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

	int sumacc=0;
	public MetroCongestionStats(int updateRate, boolean serialize, int clear) {
		super(updateRate, serialize, clear);
		dataset = new DefaultCategoryDataset();
		this.serialize = false;
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		super.onStep(simulator);
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			List<Entity>listTotal = simulator.getEntities().stream().filter(e->e instanceof PasiveEntity).collect(Collectors.toList());
			/*for(Entity e:listTotal) {
				((DefaultCategoryDataset)dataset).addValue(((PassengerController)e.getAttribute("passengers")).total(), (String)e.getAttribute("station"), Integer.valueOf(currentTime));
			}*/
			int sum=0;
			for(Entity e:listTotal) {
				sum += ((PassengerController)e.getAttribute("passengers")).total();
			}
			((DefaultCategoryDataset)dataset).addValue(sum,"avg", Integer.valueOf(currentTime));
			sumacc+=sum;
			((DefaultCategoryDataset)dataset).addValue(sumacc,"acc", Integer.valueOf(currentTime));
			
		}
	}

}
