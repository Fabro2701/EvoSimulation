package statistics;

import simulator.model.EvoSimulator;

public interface StatsObserver {
	default public void onRegister() {}
	
	default public void onStep(EvoSimulator simulator) {}
	
	default public void onEvent(String type) {}

	public void clear();
}
