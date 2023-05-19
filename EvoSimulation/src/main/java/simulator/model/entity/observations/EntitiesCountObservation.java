package simulator.model.entity.observations;

import java.util.HashMap;
import java.util.List;

import simulator.Constants;
import simulator.Constants.MOVE;
import simulator.model.entity.Entity;
import simulator.model.entity.Pheromone;
import simulator.model.map.Map;
import util.Pair;
import util.Util;

public class EntitiesCountObservation extends AbstractObservation{
	private MOVE dir;

	public EntitiesCountObservation(MOVE dir) {
		this.dir = dir;
	}
	@Override
	public HashMap<String, Object> getObservation(List<Entity> entities, Map map) {
		HashMap<String, Object> o = new HashMap<String, Object>();
		
		
		int cont = 0;
		//double dist;
		int viewAx = (int) Constants.ENTITY_OBSERVATION_DISTANCE;
		int x = entity.node.x - viewAx, y = entity.node.y - viewAx;
		for(Entity e:this.manager.getVisibleEntities()) {
			if(e != entity) {
				if(Util.isInQuadrant(dir, e.node.x - x, e.node.y - y, viewAx)) {
				    cont++;
				}
			    	
			}
		}
		
		o.put("count"+dir, String.valueOf(cont));
		
		
		return o;
	}

}
