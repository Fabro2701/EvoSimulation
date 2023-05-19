package simulator.model.entity.observations;

import java.util.HashMap;
import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.map.Map;

public abstract class AbstractObservation {
	Entity entity;
	ObservationManager manager;
	public AbstractObservation() {
	}
	public abstract HashMap<String, Object> getObservation(List<Entity>entities, Map map);
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public void setManager(ObservationManager manager) {
		this.manager = manager;
	}
}
