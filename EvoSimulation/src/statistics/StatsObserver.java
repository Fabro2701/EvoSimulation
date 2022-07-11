package statistics;

public interface StatsObserver {
	default public void onRegister() {}
	
	default public void onEntityAdded(int time) {}
	
	default public void onAdvance(int time) {}
}
