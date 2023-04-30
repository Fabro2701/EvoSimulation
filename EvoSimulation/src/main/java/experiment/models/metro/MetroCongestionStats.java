package experiment.models.metro;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import statistics.StatsData;

public class MetroCongestionStats extends StatsData{

	int sumacc=0;
	Map<String,Integer> accMap;
	public MetroCongestionStats(int updateRate, boolean serialize, int clear) {
		super(updateRate, serialize, clear);
		dataset = new DefaultCategoryDataset();
		this.serialize = false;
		this.accMap = new TreeMap<>();
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
				String station = (String) e.getAttribute("station");
				if(accMap.containsKey(station)) {
					accMap.put(station, accMap.get(station)+((PassengerController)e.getAttribute("passengers")).total());
				}
				else {
					accMap.put(station, ((PassengerController)e.getAttribute("passengers")).total());
				}
				((DefaultCategoryDataset)dataset).addValue(accMap.get(station),station, Integer.valueOf(currentTime));
			}
			//((DefaultCategoryDataset)dataset).addValue(sum,"avg", Integer.valueOf(currentTime));
			//sumacc+=sum;
			
			
		}
	}

}
