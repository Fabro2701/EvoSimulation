package simulator.model;

import simulator.model.entity.Entity;
import simulator.model.map.Map;

@FunctionalInterface
public interface InteractionI {
	public Object perform(Entity e1, Entity e2, Map map);
}
