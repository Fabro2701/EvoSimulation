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
		int viewAx = 150;
		//double viewD = ((double)viewAx*Math.sqrt(2))/2.0;
		int x = -entity.node.x + viewAx/2, y = -entity.node.y + viewAx/2;
		for(Entity e:this.manager.getVisibleEntities()) {
			if(e != entity) {
				if(Util.isInQuadrant(dir, e.node.x - x, e.node.y - y, viewAx)) {
					dist = Util.nodeDistance(entity.node, e.node);
			    	if(dist  < minDist) {
						minDist = dist;
					}
				    
				}
			    	
			}
		}
		
		o.put("dist"+dir, String.valueOf((int)minDist));
		
		
		return o;
	}

}
