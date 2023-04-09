package simulator.model.optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import simulator.model.EvoSimulator;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.model.optimizer.clustering.KMeansClustering;
import simulator.model.optimizer.clustering.KMeansClustering.Centroid;
import util.Util;

public class DensityOptimizer implements Optimizer {

	static int updateLapse = 2000;
	
	EvoSimulator simulator;
	KMeansClustering clusterAlgo;
	int k;
	
	Grid grids[];
	Grid defaultGrid;
	boolean init;
	
	public DensityOptimizer(EvoSimulator simulator, int k) {
		this.k = k;
		this.simulator = simulator;
		clusterAlgo = new KMeansClustering(k,10);
		grids = new Grid[k];
		for(int i=0;i<k;i++)grids[i] = new Grid();
		defaultGrid = new Grid();
		init = false;
	}
	
	private class Grid{
		List<Entity>inEntities;
		int minx,miny,maxx,maxy;
		public Grid() {
			inEntities = new ArrayList<Entity>();
		}
		public void assignCluster(Entry<Centroid, List<Entity>>cluster) {
			List<Entity> es = cluster.getValue();
			if(es.isEmpty()) {
				maxx = 0;
				maxy = 0;
				minx = 0;
				miny = 0;
				return;
			}
			maxx = es.stream().max((Entity e1,Entity e2)->Integer.compare(e1.node.x, e2.node.x)).get().node.x;
			maxy = es.stream().max((Entity e1,Entity e2)->Integer.compare(e1.node.y, e2.node.y)).get().node.y;
			minx = es.stream().min((Entity e1,Entity e2)->Integer.compare(e1.node.x, e2.node.x)).get().node.x;
			miny = es.stream().min((Entity e1,Entity e2)->Integer.compare(e1.node.y, e2.node.y)).get().node.y;
		}
		public boolean contains(Node node) {
			int x = node.x, y = node.y;
			return (x>=minx && x<=maxx)&&(y>=miny && y<=maxy);
		}
	}
	
	@Override
	public void update(Map map, List<Entity> entities, int time) {
		if(time%updateLapse==0 || !init) {	
			init = true;
			int i = 0;
			for(Entry<Centroid, List<Entity>>cluster:clusterAlgo.fit(entities).entrySet()) {
				grids[i].assignCluster(cluster);
				i ++;
			}
		}
		for(Grid g:grids)g.inEntities.clear();
		defaultGrid.inEntities.clear();
		_assignGrids(entities);
		/*System.out.println("---------------");
		System.out.println(entities.size());
		int c=0;
		for(Grid g:grids)c+=g.inEntities.size();
		c+=defaultGrid.inEntities.size();
		System.out.println(c);
		System.out.println("---------------");*/
		//System.out.println(defaultGrid.inEntities.size());
		/*System.out.println("---------------");
		for(Grid g:grids)System.out.println(g.inEntities.size());
		System.out.println(defaultGrid.inEntities.size());
		System.out.println("---------------");*/
		
		//Create a pool and a task 
		ExecutorService service = Executors.newCachedThreadPool();
		List<Future<?>>results = new ArrayList<Future<?>>();
		
		for(Grid g:grids) {
			results.add(service.submit(()->_update(map, g.inEntities, entities)));
		}
		results.add(service.submit(()->_update(map, defaultGrid.inEntities, entities)));

		
		//join the results
		for(Future<?> f:results) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		service.shutdown();
		
		// remove entities
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isAlive()) {
				entities.remove(i);
				i--;
			}
		}
	}
	private void _assignGrids(List<Entity> entities) {
		for(Entity e:entities) {
			boolean assigned = false;
			for(Grid g:grids) {
				if(g.contains(e.node)) {
					g.inEntities.add(e);
					assigned = true;
					break;
				}
			}
			if(!assigned)defaultGrid.inEntities.add(e);
		}
	}
	public void _update(Map map, List<Entity> inEntities, List<Entity> entities) {
		for (Entity e : inEntities) {
			e.update(simulator);
		}
		
		// entities actions
		for (Entity e : inEntities) {
			if(e.isAlive())e.perform(entities, map);
		}
	
		// entities interactions
		for (Entity e1 : inEntities) {	
			if(e1 instanceof ActiveEntity) {
				for (Entity e2 : inEntities) {
					if(e1!=e2&&e2.isAlive()) {
						if (Util.areCloseEnough(e1, e2)) {
							e1.interact(e2);
						}
					}
				}
			}
		}
	}

}
