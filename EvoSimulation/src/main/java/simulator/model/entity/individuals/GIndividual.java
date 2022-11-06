package simulator.model.entity.individuals;

import java.util.List;
import java.util.function.Consumer;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.control.GrammarController;
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
	
	protected java.util.Map<String, AbstractGrammar>grammars;
	protected java.util.Map<String, java.util.Map<String, ActionI>>actions;
	protected java.util.Map<String, InteractionI>interactions;
	protected java.util.Map<String, Consumer<Entity>>updates;
	protected java.util.Map<String, Consumer<Entity>>inits;
	public GIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		observationManager = new ObservationManager(this);
		
		grammars = ctrl.getGrammarController().getGrammars();
		actions = ctrl.getActionsController().getActions();
		interactions = ctrl.getInteractionsController().getInteractions();
		updates = ctrl.getUpdatesController().getUpdates();
		inits = ctrl.getInitController().getStatements();
		
		init();
	}
	private void init() {
		for(String id:inits.keySet()) {
			inits.get(id).accept(this);
		}
	}
	public void updateObservations(List<Entity>entities, Map map) {
		observationManager.update(entities, map);
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
		
		for(String id:updates.keySet()) {
			updates.get(id).accept(this);
		}
		System.out.println(this.getAttribute("imc"));
	}

	@Override
	public void perform(List<Entity>entities, Map map) {
		for(String actionid:grammars.keySet()) {
			Object election = this.phenotype.getNext(actionid, this.observationManager.getVariables());
			ActionI a = actions.get(actionid).get(election);
			if(a!=null)a.perform(this, entities, map);
			else System.err.println("Action "+election+" not declared");
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
