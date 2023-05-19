package simulator.model.entity.observations;

import static simulator.Constants.DEFAULT_OBSERVATIONS_REFRESH_RATE;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import simulator.Constants;
import simulator.Constants.MOVE;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import util.Util;

public class ObservationManager {
	int refreshRate = DEFAULT_OBSERVATIONS_REFRESH_RATE, time = 0;
	List<AbstractObservation>observations;
	Entity entity;
	
	HashMap<String,Object> variables;
	List<Entity>visibleEntities;
	EnumMap<MOVE,Entity> closestEntity;
	
	
	public ObservationManager(Entity entity) {
		this.entity = entity;
		this.variables = new HashMap<String,Object>();
		this.visibleEntities = new ArrayList<Entity>();
		this.closestEntity = new EnumMap<MOVE,Entity>(MOVE.class);
		
		observations = new ArrayList<AbstractObservation>();
		observations.add(new ClosestEntityDistanceObservation(MOVE.UP));
		observations.add(new ClosestEntityDistanceObservation(MOVE.DOWN));
		observations.add(new ClosestEntityDistanceObservation(MOVE.LEFT));
		observations.add(new ClosestEntityDistanceObservation(MOVE.RIGHT));
		/*observations.add(new EntitiesCountObservation(MOVE.UP));
		observations.add(new EntitiesCountObservation(MOVE.DOWN));
		observations.add(new EntitiesCountObservation(MOVE.LEFT));
		observations.add(new EntitiesCountObservation(MOVE.RIGHT));
		observations.add(new ClosestEntityPheromoneObservation(MOVE.UP));
		observations.add(new ClosestEntityPheromoneObservation(MOVE.DOWN));
		observations.add(new ClosestEntityPheromoneObservation(MOVE.LEFT));
		observations.add(new ClosestEntityPheromoneObservation(MOVE.RIGHT));
		*/
		observations.stream().forEach(o -> {
			o.setEntity(entity);
			o.setManager(this);
		});
		
	}
	public void update(List<Entity>entities, Map map) {
		if(time%refreshRate == 0) {
			this.setCommonVariables(entities, map);
			observations.stream().forEach(
					o -> this.variables.putAll(o.getObservation(entities, map))
			);
		}
		time++;
	}
	public HashMap<String, Object> getVariables() {
		return variables;
	}
	private void setCommonVariables(List<Entity>entities, Map map) {
		visibleEntities.clear();
		
		//double viewD = ((double)150*Math.sqrt(2))/2.0;
		float viewD = Constants.ENTITY_OBSERVATION_DISTANCE;
		
		for(Entity e2:entities) {
			if(entity!=e2 && Util.nodeDistance(entity.node, e2.node) < viewD) {
				visibleEntities.add(e2);
			}
		}
	}
	public List<Entity> getVisibleEntities() {
		return visibleEntities;
	}
	public EnumMap<MOVE, Entity> getClosestEntity() {
		return closestEntity;
	}
	public void setClosestEntity(EnumMap<MOVE, Entity> closestEntity) {
		this.closestEntity = closestEntity;
	}
	
}
