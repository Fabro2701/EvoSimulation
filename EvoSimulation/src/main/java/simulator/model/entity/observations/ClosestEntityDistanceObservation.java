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

public class ClosestEntityDistanceObservation extends AbstractObservation{
	private MOVE dir;

	public ClosestEntityDistanceObservation(MOVE dir) {
		this.dir = dir;
	}
	@Override
	public HashMap<String, String> getObservation(List<Entity> entities, Map map) {
		HashMap<String, String> o = new HashMap<String, String>();
		
		
		double minDist = Double.MAX_VALUE;
		double dist;
		Entity cEntity = null;
		int viewAx = (int) Constants.ENTITY_OBSERVATION_DISTANCE;
		int x = entity.node.x - viewAx, y = entity.node.y - viewAx;
		for(Entity e:this.manager.getVisibleEntities()) {
			if(e != entity) {
				if(Util.isInQuadrant(dir, e.node.x - x, e.node.y - y, viewAx)) {
					dist = Util.nodeDistance(entity.node, e.node);
			    	if(dist  < minDist) {
						minDist = dist;
						cEntity = e;
					}
				    
				}
			    	
			}
		}
		
		o.put("dist"+dir, String.valueOf((int)minDist));
		this.manager.getClosestEntity().put(this.dir, cEntity);
		
		return o;
	}

}
