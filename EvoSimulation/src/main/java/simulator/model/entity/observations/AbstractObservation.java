package simulator.model.entity.observations;

import java.util.HashMap;
import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.map.Map;

public abstract class AbstractObservation {
	Entity _entity;
	public AbstractObservation(Entity e) {
		this._entity=e;
	}
	public abstract HashMap<String, String>getObservation(List<Entity>entities, Map map);
}
