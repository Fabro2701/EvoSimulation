package simulator.model.entity.individuals;

import java.util.List;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.observations.ObservationManager;
import simulator.model.map.Map;
import simulator.model.map.Node;

public abstract class GIndividual extends AbstractIndividual{
	protected AbstractGrammar grammar;
	protected Genotype genotype;
	protected ObservationManager observationManager;
	public GIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		observationManager = new ObservationManager(this);
	}
	public void updateObservations(List<Entity>entities, Map map) {
		observationManager.update(entities, map);
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
	}
	public AbstractGrammar getGrammar() {
		return grammar;
	}

	public void setGrammar(AbstractGrammar grammar) {
		this.grammar = grammar;
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

	protected Phenotype phenotype;
	
	


}
