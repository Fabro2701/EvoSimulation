package simulator.model.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import simulator.Constants.MOVE;
import simulator.model.EvoSimulator;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import util.Util;

/**
 * UniformGridOptimizer creates (xDivision * yDivision) uniform grids and updates each of them internally
 * For the moment an entity can appear in only one grid
 * @author fabrizioortega
 *
 */
public class UniformGridOptimizer implements Optimizer{
	EvoSimulator simulator;
	Grid grids[][];
	int xDivision;
	int yDivision;
	float unitsPerGridx;
	float unitsPerGridy;
	
	public UniformGridOptimizer(EvoSimulator simulator, int xDivision, int yDivision) {
		this.simulator = simulator;
		this.xDivision = xDivision;
		this.yDivision = yDivision;
		unitsPerGridx = (float)simulator.getMap().WIDTH/xDivision;
		unitsPerGridy = (float)simulator.getMap().HEIGHT/yDivision;
		grids = new Grid[yDivision][xDivision];
		for(int i=0;i<yDivision;i++) {
			for(int j=0;j<xDivision;j++) {
				grids[i][j] = new Grid();
			}
		}
		
	}
	protected class Grid{
		List<Entity>inEntities;
		public Grid() {
			inEntities = new ArrayList<Entity>();
		}
		public void addEntity(Entity e) {
			this.inEntities.add(e);
		}
		public void update(Map map, List<Entity> entities) {
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
	/**
	 * Assign each entity to a grid
	 * @param entities
	 */
	private void _assignGrids(List<Entity> entities) {
		for(Entity e:entities) {
			int x = e.node.x;
			int y = e.node.y;
			
			//we calculated the grid this way because they are uniform
			this.grids[(int)(y/unitsPerGridy)][(int)(x/unitsPerGridx)].addEntity(e);
			
			//actually each entity can be in 4 grids 
			//because they have a radio of Constants.INTERACTION_DISTANCE 
			
		}
	}
	
	@Override
	public void update(Map map, List<Entity> entities, int time) {
		//clear the grids
		for(int i=0;i<yDivision;i++) {
			for(int j=0;j<xDivision;j++) {
				grids[i][j].inEntities.clear();
			}
		}
		
		//Assign the entities to the grids
		_assignGrids(entities);
		
		
		//Create a pool and a task per each grid
		ExecutorService service = Executors.newCachedThreadPool();
		List<Future<?>>results = new ArrayList<Future<?>>();
		
		class Task implements Runnable{
			int i,j;
			public Task(int i, int j) {
				this.i=i;
				this.j=j;
			}
			@Override
			public void run() {
				grids[i][j].update(map, entities);
			}
		}
		for(int i=0;i<yDivision;i++) {
			for(int j=0;j<xDivision;j++) {
				results.add(service.submit(new Task(i,j)));
			}
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
	
}
