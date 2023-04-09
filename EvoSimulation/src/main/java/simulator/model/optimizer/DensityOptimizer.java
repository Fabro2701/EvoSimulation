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
import simulator.model.optimizer.clustering.KMeansClustering;
import simulator.model.optimizer.clustering.KMeansClustering.Centroid;
import util.Util;

public class DensityOptimizer implements Optimizer {

	static int updateLapse = 2000;
	
	EvoSimulator simulator;
	KMeansClustering clusterAlgo;
	
	
	java.util.Map<Centroid, List<Entity>> clusters;
	
	public DensityOptimizer(EvoSimulator simulator) {
		this.simulator = simulator;
		clusterAlgo = new KMeansClustering(4,5);
		
	}
	@Override
	public void update(Map map, List<Entity> entities, int time) {
		if(time%updateLapse==0 || clusters == null) {
			clusters = clusterAlgo.fit(entities);
		}
		
		//Create a pool and a task 
		ExecutorService service = Executors.newCachedThreadPool();
		List<Future<?>>results = new ArrayList<Future<?>>();
		
		for(Entry<Centroid,List<Entity>> entry:clusters.entrySet()) {
			results.add(service.submit(()->_update(map, entry.getValue(), entities)));
		}
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
