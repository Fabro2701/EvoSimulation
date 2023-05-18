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
	protected static int splitThreshold = 100;
	
	EvoSimulator simulator;
	KMeansClustering clusterAlgo;
	int k;
	
	ClusterGrid grids[];
	ClusterGrid defaultGrid;
	boolean init;
	
	public DensityOptimizer(EvoSimulator simulator, int k) {
		this.k = k;
		this.simulator = simulator;
		clusterAlgo = new KMeansClustering(k,10);
		grids = new ClusterGrid[k];
		for(int i=0;i<k;i++)grids[i] = new ClusterGrid();
		defaultGrid = new ClusterGrid();
		init = false;
	}
	
	private class ClusterGrid{
		List<Entity>inEntities;
		int minx,miny,maxx,maxy;
		Entry<Centroid, List<Entity>>cluster;
		Grid grids[][];
		Grid defaultGrid;
		public ClusterGrid() {
			inEntities = new ArrayList<Entity>();
		}
		public void assignCluster(Entry<Centroid, List<Entity>>cluster) {
			this.cluster = cluster;
			
		}
		public void reset() {
			if(this.grids!=null) {
				for(int i=0;i<splits;i++) {
					for(int j=0;j<splits;j++) {
						grids[i][j].reset();;
					}
				}
			}
			this.grids = null;
		}
		private void updateRange() {
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
		public void update(Map map, List<Entity> entities) {
			if(grids==null) {
				_update(map,this.inEntities,entities);
			}
			else {
				ExecutorService service = Executors.newCachedThreadPool();
				List<Future<?>>results = new ArrayList<Future<?>>();
				
				try {
					for(int i=0;i<splits;i++) {
						for(int j=0;j<splits;j++) {
							Grid g = grids[i][j];
							results.add(service.submit(()->g.update(map,entities)));
						}results.add(service.submit(()->defaultGrid.update(map,entities)));
					}
	
					//join the results
					for(Future<?> f:results) {
						
							f.get();
						
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					return;
				}
				service.shutdown();
				
			}
		
		}
		public void propagate() {
			if(this.inEntities.size()<=splitThreshold) {
				grids = null;
			}
			else {
				//System.out.println(this.inEntities.size());
				if(grids==null)initGrids(inEntities.size());
				for(int i=0;i<splits;i++) {
					for(int j=0;j<splits;j++) {
						grids[i][j].reset();
					}
				}defaultGrid.reset();
				
				for(Entity e:this.inEntities) {
					int x = (int)((e.node.x-minx)/unitsPerGridx);
					int y = (int)((e.node.y-miny)/unitsPerGridy);
					if(x<0||y<0||x>=splits||y>=splits) {
						defaultGrid.internalEntities.add(e);
					}
					else this.grids[y][x].internalEntities.add(e);
				}
			}
		}
		float unitsPerGridx;
		float unitsPerGridy;
		int splits;
		public void initGrids(int amount) {
			updateRange();
			this.splits = 2+amount/100;
			this.unitsPerGridx = (float)(maxx-minx)/splits;
			this.unitsPerGridy = (float)(maxy-miny)/splits;
			this.grids = new Grid[splits][splits];
			for(int i=0;i<splits;i++) {
				for(int j=0;j<splits;j++) {
					grids[i][j]=new Grid();
				}
			}
			defaultGrid=new Grid();
			
		}
		private class Grid{
			List<Entity>internalEntities;
			public Grid() {
				this.internalEntities = new ArrayList<>();
				
			}
			public void reset() {
				if(this.internalEntities!=null)this.internalEntities.clear();
			}
			public void update(Map map, List<Entity> entities) {
				_update(map,this.internalEntities,entities);
			}
		}
	}
	
	@Override
	public void update(Map map, List<Entity> entities, int time) {
		if(time%updateLapse==0 || !init) {	
			init = true;
			int i = 0;
			for(Entry<Centroid, List<Entity>>cluster:clusterAlgo.fit(entities).entrySet()) {
				grids[i].reset();
				grids[i].assignCluster(cluster);
				i ++;
			}
		}
		for(ClusterGrid g:grids)g.inEntities.clear();
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
		
	
		for(ClusterGrid g:grids) {
			results.add(service.submit(()->g.update(map,entities)));
		}

		//join the results
		for(Future<?> f:results) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return;
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
		/*for(Entity e:entities) {
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
		*/
		for(Entity e:entities) {
			double md = 10000d;
			ClusterGrid mg = null;
			for(ClusterGrid g:grids) {
				Entry<Centroid, List<Entity>> c = g.cluster;
				double d = KMeansClustering.dist(e,c.getKey());
				if(d<md) {
					md = d;
					mg = g;
				}
			}
			mg.inEntities.add(e);
		}
		for(ClusterGrid g:grids) {
			g.propagate();
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
