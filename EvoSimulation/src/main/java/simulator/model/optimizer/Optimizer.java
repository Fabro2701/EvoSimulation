package simulator.model.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.entity.observations.AbstractObservation;
import simulator.model.map.Map;
import util.Util;

public abstract class Optimizer {
	public abstract void update(Map map, List<Entity>entities, int time);
	protected HashMap<String,String>getObservations(Entity e, List<Entity>entities){
		if(e instanceof PasiveEntity)return null;
		HashMap<String,String> r = new HashMap<String,String>();
		
		double viewD = ((double)150*Math.sqrt(2))/2.0;
		List<Entity>e2s = new ArrayList<Entity>();
		for(Entity e2:entities) {
			if(e!=e2 && Util.nodeDistance(e.node, e2.node)<viewD) {
				e2s.add(e2);
			}
		}
		for(AbstractObservation o:((GIndividual)e).getObservations()) {
			r.putAll(o.getObservation(e2s, null));
		}
		return r;
		/*
		
		int dist=100;
		
		int u_f_d=0;
		int d_f_d=0;
		int r_f_d=0;
		int l_f_d=0;
		
		
	
		if(!e.active)return r;
		
		for(Entity e2:entities) {
			if(e2 instanceof FoodEntity) {
				if(Math.sqrt(Math.pow(e.node.x-e2.node.x,2)+Math.pow(e.node.y-e2.node.y,2))<=dist) {
					if(e.node.x>e2.node.x) {
						l_f_d++;
					}
					if(e.node.x<e2.node.x) {
						r_f_d++;
					}
					if(e.node.y<e2.node.y) {
						d_f_d++;
					}
					if(e.node.y>e2.node.y) {
						u_f_d++;
					}
				}
			}
		}
		r.put("l_f_d", String.valueOf(l_f_d));
		r.put("r_f_d", String.valueOf(r_f_d));
		r.put("d_f_d", String.valueOf(d_f_d));
		r.put("u_f_d", String.valueOf(u_f_d));
		//if(e.getId().equals("-2"))System.out.println(r.get("l_f_d")+" "+r.get("u_f_d")+" "+r.get("r_f_d")+" "+r.get("d_f_d"));
		 
		
		*/
	}
}
