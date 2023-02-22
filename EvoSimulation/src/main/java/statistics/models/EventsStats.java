package statistics.models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import statistics.StatsData;

/**
 * 
 * @author Fabrizio Ortega
 *
 */
public class EventsStats extends StatsData{

	private int currentTime=0;
	Map<String, AtomicInteger> events;

	public EventsStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
		events = new HashMap<>();
		serialize = false;
	}
	
	@Override 
	public void onEvent(String type) {
		this.events.computeIfAbsent(type, i->new AtomicInteger(1)).incrementAndGet();
	}
	
	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			for(String key:events.keySet()) {
				((DefaultCategoryDataset)dataset).addValue(events.get(key), 0, key);

			}
		}
	}

}
