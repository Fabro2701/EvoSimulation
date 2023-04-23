package simulator.model.entity;

import java.util.HashMap;
import java.util.List;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import grammar.AbstractGrammar;
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

public class InteractiveEntity extends Entity{

	
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

	@Override
	public void perform(List<Entity> entities, Map map) {
		
	}

	@Override
	public boolean shouldInteract() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void myInteract(Entity e2) {
		System.err.println("no interaction allowed");
	}
	
	

}
