package simulator.model.entity;

import java.util.HashMap;
import java.util.List;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import grammar.AbstractGrammar;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.control.GrammarController;
import simulator.control.InitController;
import simulator.control.InteractionsController;
import simulator.control.UpdatesController;
import simulator.model.ActionI;
import simulator.model.EvoSimulator;
import simulator.model.entity.observations.ObservationManager;
import simulator.model.map.Map;
import simulator.model.map.Node;
import util.Util;

public abstract class InteractiveEntity extends Entity{

	
	protected java.util.Map<String, AbstractGrammar>grammars;
	protected java.util.Map<String, java.util.Map<String, ActionI>>actions;
	protected InteractionsController interactions;
	protected UpdatesController updates;
	protected InitController inits;
	
	protected GrammarController grammarController;
	protected java.util.Map<String,Boolean>exs;
	
	
	
	public InteractiveEntity(String id, Node n, Controller ctrl, String code) {
		super(id, n, ctrl);
		
		
		grammars = ctrl.getGrammarController().getGrammars();
		actions = ctrl.getActionsController().getActions();
		interactions = ctrl.getInteractionsController();
		updates = ctrl.getUpdatesController();
		inits = ctrl.getInitController();
		
		grammarController = ctrl.getGrammarController();
		exs = new HashMap<>();
		for(String k:grammars.keySet()) {
			exs.put(k, false);
		}
		
		if(code!=null&&code.length()>0)this.apply(code);
		init();
		
	}
	
	protected void init() {
		java.util.Map<String, Consumer<Entity>>inits_l = inits.getStatements(); 
		for(String id:inits_l.keySet()) {
			if(inits.match(id, this.getClass())) {
				inits_l.get(id).accept(this);
			}
		}
		this.alive = true;
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		//observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
		
		java.util.Map<String, BiConsumer<Entity, EvoSimulator>> updates_l = updates.getUpdates();
		java.util.Map<String, Integer> freqs = updates.getUpdatesFreq();
		
		for(String id:updates_l.keySet()) {
			if(evoSimulator.getTime()%freqs.get(id)!=0)continue;
			try {
				if(updates.match(id, this.getClass())) {
					if(this.isAlive())updates_l.get(id).accept(this, evoSimulator);
				}
			}catch(Exception e) {
				System.err.println("Error in update: "+id);
				e.printStackTrace();
			}
			
		}
	}

	public Node nextNodeTowards_(Map map, Node node) {
		int x = (node.x-this.node.x); if(x!=0)x=x>0?1:-1;
		int y = (node.y-this.node.y); if(y!=0)y=y>0?1:-1;
		return map.getNodeAt(this.node.x+x, this.node.y+y);
	}
	public Node nextNodeTowards(Map map, Entity entity) {
		return nextNodeTowards_(map,entity.node);
	}
	public Node nextNodeStraightTowards_(Map map, Node node, double th) {
		double dx = node.x - this.node.x;
		double dy = node.y - this.node.y;
	    
		if(dx==0&&dy==0)return this.node;
	    double proportion = Math.abs(dx / dy);
	    
	    int x=0,y=0;
	    if(dx!=0)x=dx>0?1:-1;
	    if(dy!=0)y=dy>0?1:-1;
	    
	    if(proportion>=th) {
	    	return map.getNodeAt(this.node.x+x, this.node.y);
	    }
	    else if(proportion<=1/th) {
	    	return map.getNodeAt(this.node.x, this.node.y+y);
	    }
	    else {
	    	return map.getNodeAt(this.node.x+x, this.node.y+y);
	    }
	}
	public Node nextNodeStraightTowardsEsp(Map map, Entity entity, double th) {
		return nextNodeStraightTowards_(map,entity.node,th);
	}
	public Node nextNodeStraightTowards(Map map, Entity entity) {
		return nextNodeStraightTowards_(map,entity.node,3d);
	}
	public Entity getEntityClosestAttribute(EvoSimulator simulator, String attribute) {
		List<Entity>entities = simulator.getEntities();
		Entity entity=null;
		double mindist = Double.MAX_VALUE;
		double dist;
		for(Entity e:entities) {
			if(e instanceof PasiveEntity &&e.hasAttribute(attribute)&&(boolean) e.getAttribute(attribute) && (dist=Util.nodeDistance(node, e.node))<mindist) {
				mindist = dist;
				entity = e;
			}
		}
		return entity;
	}
	public Entity getEntityRandomAttribute(EvoSimulator simulator, String attribute) {
		Entity entity=null;
		List<Entity>entities = simulator.getEntities().stream().filter(e->e instanceof PasiveEntity &&e.hasAttribute(attribute)&&(boolean) e.getAttribute(attribute)).collect(Collectors.toList());
		if(entities.size()>0) 
			entity = entities.get(RandomSingleton.nextInt(entities.size()));
		return entity;
	}
	

}
