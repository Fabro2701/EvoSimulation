package statistics;

import simulator.model.EvoSimulator;

public interface StatsObserver {
	default public void onRegister() {}
	
	default public void onStep(EvoSimulator simulator) {}
	
	default public void onMutation() {}
	default public void onReproduction() {}
	default public void onDeadOffSpring(int type) {}
	

	default public void onEvent(String type) {}

	public void clear();
}
