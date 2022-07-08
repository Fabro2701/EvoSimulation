package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.Constants.MOVE;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

public class EvoSimulator {
	private Map map;
	private List<SimulatorObserver>observers;
	private List<Entity>entities;
	private int time;
	
	
	public EvoSimulator() {
		this.time = 0;
		this.map = new Map("test1");
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<>();
	}
	public void update() {
		for(Entity e:entities) {
			MOVE move = e.getMove();
			
		}
		for(SimulatorObserver observer:observers) {
			observer.onUpdate(entities, time);
		}
		time++;
	}
	public void addObserver(SimulatorObserver observer) {
		observers.add(observer);
		observer.onRegister(entities,map,time);
	}
}
