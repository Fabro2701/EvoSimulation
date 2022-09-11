package simulator.model;

import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.map.Map;

public interface SimulatorObserver {
	public void onRegister(List<Entity> entities, Map map, int time);
	public void onUpdate(List<Entity> entities, Map map, int time);
	//onevent (List<Entity> entities, Map map, double time);
}
