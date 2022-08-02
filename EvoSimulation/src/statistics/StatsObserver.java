package statistics;

import simulator.model.EvoSimulator;

public interface StatsObserver {
	default public void onRegister() {}
	
	default public void onUpdate(EvoSimulator simulator) {}
}
