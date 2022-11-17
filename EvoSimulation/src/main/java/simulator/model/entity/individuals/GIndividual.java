package simulator.model.entity.individuals;

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
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.entity.observations.ObservationManager;
import simulator.model.map.Map;
import simulator.model.map.Node;

public abstract class GIndividual extends AbstractIndividual{
	protected Genotype genotype;
	protected Phenotype phenotype;
	
	protected ObservationManager observationManager;
	
	public GIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		observationManager = new ObservationManager(this);
	}
	@Override
	protected void init() {
		java.util.Map<String, Consumer<Entity>>inits_l = inits.getStatements(); 
		for(String id:inits_l.keySet()) {
			if(inits.match(id, this.getClass())) {
				inits_l.get(id).accept(this);
			}
		}
		this.alive = true;
	}
	public void updateObservations(List<Entity>entities, Map map) {
		observationManager.update(entities, map);
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
		
		java.util.Map<String, BiConsumer<Entity, EvoSimulator>> updates_l = updates.getUpdates();
		for(String id:updates_l.keySet()) {
			if(updates.match(id, this.getClass())) {
				updates_l.get(id).accept(this, evoSimulator);;
			}
		}
		//System.out.println(this.getAttribute("imc"));
	}

	@Override
	public void perform(List<Entity>entities, Map map) {
		for(String actionid:grammars.keySet()) {
			Object election = this.phenotype.getNext(actionid, this.observationManager.getVariables());
			ActionI a = actions.get(actionid).get(election);
			if(a!=null)a.perform(this, entities, map);
			//else System.err.println("Action "+election+" not declared");
		}
	}
	@Override
	public void myInteract(Entity e2) {
		java.util.Map<String, InteractionI> interactions_l = interactions.getInteractions();
		for(String id:interactions_l.keySet()) {
			if(interactions.match(id, this.getClass())) {
				interactions_l.get(id).perform(this, e2, ctrl.getMap());
			}
		}
	}

	public Genotype getGenotype() {
		return genotype;
	}

	public void setGenotype(Genotype genotype) {
		this.genotype = genotype;
	}

	public Phenotype getPhenotype() {
		return phenotype;
	}
	
	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}
}
