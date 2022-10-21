package simulator.model;

import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.map.Map;

@FunctionalInterface
public interface ActionI {
	public Object perform(Entity e, List<Entity>entities, Map map);
}
