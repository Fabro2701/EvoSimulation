package statistics;

import java.util.ArrayList;
import java.util.List;

import simulator.model.entity.Entity;

public class StatsManager implements StatsObserver{
    private static StatsManager single_instance = null;
 
    private List<StatsObserver> observers;

    private StatsManager(){
    	observers = new ArrayList<StatsObserver>();
    }
 

    public static StatsManager getInstance(){
        if (single_instance == null)
            single_instance = new StatsManager();
 
        return single_instance;
    }


	public void addObserver(StatsObserver observer) {
		observers.add(observer);
		observer.onRegister();
	}
	
	@Override
	public void onRegister() {}
	@Override
	public void onEntityAdded(int time) {
		for(StatsObserver observer:observers) {
			observer.onEntityAdded(time);
		}
	}
	@Override
	public void onAdvance(int time) {
		for(StatsObserver observer:observers) {
			observer.onAdvance(time);
		}
	}
	@Override
	public void onEntityVanished(int time) {
		for(StatsObserver observer:observers) {
			observer.onEntityVanished(time);
		}
	}
	@Override
	public void onEntityDead(int time, Entity e) {
		for(StatsObserver observer:observers) {
			observer.onEntityDead(time, e);
		}
	}
}