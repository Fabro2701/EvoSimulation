package simulator.model.entity.individuals;

import java.util.List;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.control.GrammarController;
import simulator.model.ActionI;
import simulator.model.EvoSimulator;
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
	public GIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		observationManager = new ObservationManager(this);
		
		grammars = ctrl.getGrammarController().getGrammars();
		actions = ctrl.getActionsController().getActions();
		
	}
	public void updateObservations(List<Entity>entities, Map map) {
		observationManager.update(entities, map);
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
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
