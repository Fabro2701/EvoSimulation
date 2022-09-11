package statistics;

import simulator.model.EvoSimulator;

public interface StatsObserver {
	default public void onRegister() {}
	
	default public void onUpdate(EvoSimulator simulator) {}
	
	default public void onMutation() {}
	default public void onReproduction() {}
	default public void onDeadOffSpring(int type) {}
}
