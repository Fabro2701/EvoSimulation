package simulator.model;

import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.view.viewer.ViewElement;

public interface SimulatorObserver {
	public void onRegister(List<Entity> entities, Map map, int time);
	public void onUpdate(List<Entity> entities, Map map, int time, java.util.Map<Object, ViewElement> viewElements);
	//onevent (List<Entity> entities, Map map, double time);
}
