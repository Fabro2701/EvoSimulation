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

public class ClosestEntityPheromoneObservation extends AbstractObservation{
	private MOVE dir;
	
	public ClosestEntityPheromoneObservation(MOVE dir) {
		this.dir = dir;
	}
	@Override
	public HashMap<String, String> getObservation(List<Entity> entities, Map map) {
		HashMap<String, String> o = new HashMap<String, String>();
		
		Pheromone p = new Pheromone();
		
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
						p = e.getPheromone();
						cEntity = e;
					}
				}
			}
		}
		
		for(int i=0;i<Constants.PHEROMONE_LENGTH;i++) {
			o.put("p["+i+"]"+dir, String.valueOf(p.get(i).intValue()));
		}
		this.manager.getClosestEntity().put(this.dir, cEntity);
		
		
		return o;
	}

}
