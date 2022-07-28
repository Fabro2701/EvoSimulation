package statistics;

import java.util.List;

import simulator.model.entity.Entity;

public interface StatsObserver {
	default public void onRegister() {}
	
	default public void onEntityAdded(int time) {}
	
	default public void onAdvance(int time, List<Entity>entities) {}

	default public void onEntityVanished(int time) {}
	
	default public void onEntityDead(int time, Entity e) {}
}
