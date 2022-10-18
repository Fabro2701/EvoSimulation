package simulator.model.entity.individuals;

import java.util.List;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.control.GrammarController;
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
	
	public GIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		observationManager = new ObservationManager(this);
		
		grammars = ((GrammarController)ctrl.getSetupCtrl().getModule("GrammarController")).getGrammars();
		//grammars = new HashMap<String, AbstractGrammar>();
		
	}
	public void updateObservations(List<Entity>entities, Map map) {
		observationManager.update(entities, map);
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
		observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
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
