package simulator.model.optimizer.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import simulator.model.entity.Entity;

public class KMeansClustering {
	int k, maxIt;
	Random rnd = new Random();
	public KMeansClustering(int k, int maxIt) {
		this.k = k;
		this.maxIt = maxIt;
	}
	public class Centroid{
		int x,y;

		public Centroid(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		public String toString() {
			return x+" - "+y;
		}
	}
	public Map<Centroid, List<Entity>>fit(List<Entity>es){
		
		//init clusters
		List<Centroid>centroids = new ArrayList<>();
		int maxx = es.stream().max((Entity e1,Entity e2)->Integer.compare(e1.node.x, e2.node.x)).get().node.x;
		int maxy = es.stream().max((Entity e1,Entity e2)->Integer.compare(e1.node.y, e2.node.y)).get().node.y;
		for(int i=0;i<k;i++) {
			centroids.add(new Centroid(rnd.nextInt(maxx+1), rnd.nextInt(maxy+1)));
		}

		Map<Centroid, List<Entity>>clusters = new HashMap<>();
		for(int i=0;i<maxIt;i++) {
			clusters.clear();
			for(Centroid c:centroids) {
				clusters.put(c, new ArrayList<>());
			}
			
			//assign ents to clusters
			for(Entity e:es) {
				Centroid nc = centroids.get(0);
				double nd = dist(e,nc);
				for(Centroid c:centroids) {
					double d = dist(e,c);
					if(d<nd) {
						nc = c;
						nd = d;
					}
				}
				clusters.get(nc).add(e);
			}
			
			//updates centroids
			for(Centroid c:centroids) {
				if(clusters.get(c).isEmpty())continue;
				double ax = clusters.get(c).stream().mapToInt(e->e.node.x).average().orElse(0);
				double ay = clusters.get(c).stream().mapToInt(e->e.node.y).average().orElse(0);
				c.x = (int) ax;
				c.y = (int) ay;
			}
		}
		
		
		return clusters;
	}
	

	private double dist(Entity e, Centroid c) {
		return Math.sqrt(Math.pow(e.node.x-c.x, 2)+Math.pow(e.node.y-c.y, 2));
	}


}
