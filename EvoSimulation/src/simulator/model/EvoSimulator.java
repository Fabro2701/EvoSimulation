package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.model.entity.Entity;
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

	public EvoSimulator() {
		this.time = 0;
		this.map = new Map("test1");
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<Entity>();

	}
	private HashMap<String,Object>getObservations(Entity e){
		
		HashMap<String,Object> r = new HashMap<String,Object>();
		
		int dist=30;
		
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
		r.put("l_f_d", l_f_d);
		r.put("r_f_d", r_f_d);
		r.put("d_f_d", d_f_d);
		r.put("u_f_d", u_f_d);
		//if(e.getId().equals("4"))System.out.println(r.get("l_f_d")+" "+r.get("u_f_d")+" "+r.get("r_f_d")+" "+r.get("d_f_d"));
		/*for(int x=-dist;x<=dist;x++) {
			for(int y=-dist;y<=dist;y++) {
				if(x<0) {
				}
			}
		}*/
		
		return r;
	}
	public void update() {
		//if(time%100==0)System.out.println(time);
		for(int i=0;i<entities.size();i++) {
			entities.get(i).update(this);
		}

		// entities movements
		for (Entity ae : entities) {
			MOVE move = ae.getMove(getObservations(ae));
			Pair<Integer, Integer> change = move.getPosChange();
			Pair<Integer, Integer> newPos = new Pair<Integer, Integer>(ae.node.x + change.first,
					ae.node.y + change.second);
			newPos.first = Math.abs(newPos.first);
			newPos.second = Math.abs(newPos.second);
			newPos.first = newPos.first >= map.WIDTH ? map.WIDTH - 1 : newPos.first;
			newPos.second = newPos.second >= map.HEIGHT ? map.HEIGHT - 1 : newPos.second;
			ae.setNewNode(this.getNodeAt(newPos.first, newPos.second));
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
		StatsManager.getInstance().onAdvance(time);
		time++;

	}

	public void addObserver(SimulatorObserver observer) {
		observers.add(observer);
		observer.onRegister(entities, map, time);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
		StatsManager.getInstance().onEntityAdded(time);
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

	public JSONObject toJSON() {
		JSONArray entitiesArr = new JSONArray();
		for (Entity e : entities) {
			entitiesArr.put(e.toJSON());
		}
		return new JSONObject().put("time", time).put("entites", entitiesArr).put("map", map.toJSON())// ?too heavy
		;
	}
}
