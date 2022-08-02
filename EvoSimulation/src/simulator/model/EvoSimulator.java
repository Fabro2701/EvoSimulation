package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.map.Map;
import simulator.model.map.Node;
import statistics.StatsManager;
import util.Pair;
import util.Util;

public class EvoSimulator {
	private Map map;
	private List<SimulatorObserver> observers;
	private List<Entity> entities;
	private int time;
	long startTime;

	public EvoSimulator() {
		this("test1000");
	}
	public EvoSimulator(String map) {
		this.time = 0;
		this.map = new Map(map);
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<Entity>();
		startTime=System.currentTimeMillis();
	}
	private HashMap<String,String>getObservations(Entity e){
		
		HashMap<String,String> r = new HashMap<String,String>();
		
		int dist=200;
		
		int u_f_d=0;
		int d_f_d=0;
		int r_f_d=0;
		int l_f_d=0;
		
		
	
		if(!e.active)return r;
		
		for(Entity e2:entities) {
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
		r.put("l_f_d", String.valueOf(l_f_d));
		r.put("r_f_d", String.valueOf(r_f_d));
		r.put("d_f_d", String.valueOf(d_f_d));
		r.put("u_f_d", String.valueOf(u_f_d));
		//if(e.getId().equals("-1"))System.out.println(r.get("l_f_d")+" "+r.get("u_f_d")+" "+r.get("r_f_d")+" "+r.get("d_f_d"));
		/*for(int x=-dist;x<=dist;x++) {
			for(int y=-dist;y<=dist;y++) {
				if(x<0) {
				}
			}
		}*/
		
		return r;
	}
	int max=500;
	public void update() {
		if(time%100==0) {
			long currentTime = System.currentTimeMillis();
			System.out.println("-------- Simulation velocity-1: "+(currentTime-this.startTime));
			startTime = currentTime;
		}
		for(int i=0;i<entities.size();i++) {
			entities.get(i).update(this);
		}
		if(time%1000==0) {
			System.out.println(time);
			for(int i=0;i<entities.size();i++) {
				if(entities.get(i).getAge()>max) {
					max=entities.get(i).getAge();
					System.out.println("new max "+max);
					System.out.println(((GIndividual)entities.get(i)).getPhenotype().getVisualCode());
				}
			}
		}
		
		

		// entities movements
		for (Entity e : entities) {
			MOVE move = e.getMove(getObservations(e));
			e.setNewNode(map.getValidMove(e.node, move));
		}

		// entities interactions
		for (Entity e1 : entities) {	
			if(e1.shouldInteract()) {
				for (Entity e2 : entities) {
					if(e1!=e2) {
						if (Util.areCloseEnough(e1, e2)) {
							e1.interact(e2);
						}
					}
				}
			}
		}

		// remove entities
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isAlive()) {
				entities.remove(i);
				i--;
			}
		}

		for (SimulatorObserver observer : observers) {
			observer.onUpdate(entities, map, time);
		}
		time++;

	}

	public void addObserver(SimulatorObserver observer) {
		observers.add(observer);
		observer.onRegister(entities, map, time);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public Node getNodeAt(int x, int y) {
		return map.getNodeAt(x, y);
	}

	public Map getMap() {
		return map;
	}

	public int getTime() {
		return time;
	}
	public Entity getEntity(String id) {
		for(Entity e:entities) {
			if(e.getId().equals(id))return e;
		}
		return null;
	}
	public List<Entity> getEntities(){
		return this.entities;
	}
	public JSONObject toJSON() {
		JSONArray entitiesArr = new JSONArray();
		for (Entity e : entities) {
			if(e.getEnergy()>0.0f)entitiesArr.put(e.toJSON());
		}
		return new JSONObject().put("time", time).put("entities", entitiesArr);//.put("map", map.toJSON())// ?too heavy
	}
}
