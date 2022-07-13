package simulator.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
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
	private List<SimulatorObserver>observers;
	private List<Entity>entities;
	private int time;
	
	
	public EvoSimulator() {
		this.time = 0;
		this.map = new Map("test1","elevation1test");
		this.observers = new ArrayList<>();
		this.entities = new ArrayList<>();
		
		
	}
	public void update() {
		//entities movements
		for(Entity e:entities) {
			MOVE move = e.getMove();
			Pair<Integer,Integer> change = move.getPosChange();
			Pair<Integer,Integer> newPos = new Pair<Integer,Integer>(e.node.x+change.first,e.node.y+change.second);
			newPos.first=Math.abs(newPos.first);
			newPos.second=Math.abs(newPos.second);
			newPos.first=newPos.first>=map.WIDTH?map.WIDTH-1:newPos.first;
			newPos.second=newPos.second>=map.HEIGHT?map.HEIGHT-1:newPos.second;
			e.setNewNode(this.getNodeAt(newPos.first, newPos.second));
			//System.out.println(newPos.first +" -*-  "+newPos.second);

			//System.out.println(e.x);
			//System.out.println(e.y);
		}
		
		//entities interactions
		for(Entity e1:entities) {
			for(Entity e2:entities) {
				if(Util.areCloseEnough(e1, e2)) {
					e1.interact(e2);
				}
			}
		}
		
		//remove entities
		for(int i=0;i<entities.size();i++) {
			if(!entities.get(i).isAlive()) {
				entities.remove(i);
				i--;
			}
		}
		
		
		
		
		for(SimulatorObserver observer:observers) {
			observer.onUpdate(entities, map, time);
		}
		StatsManager.getInstance().onAdvance(time);
		time++;
		
	}
	public void addObserver(SimulatorObserver observer) {
		observers.add(observer);
		observer.onRegister(entities,map,time);
	}
	public void addEntity(Entity entity) {
		entities.add(entity);
		StatsManager.getInstance().onEntityAdded(time);
	}
	public Node getNodeAt(int x, int y) {
		// TODO Auto-generated method stub
		return map.getNodeAt(x,y);
	}
	public Map getMap() {return map;}
	public int getTime() {return time;}
	
	public JSONObject toJSON() {
		JSONArray entitiesArr = new JSONArray();
		for(Entity e:entities) {
			entitiesArr.put(e.toJSON());
		}
		return new JSONObject().put("time", time)
							   .put("entites", entitiesArr)
							   .put("map", map.toJSON())//?too heavy
							   ;
	}
}
