package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.List;

import grammar.AbstractGrammar;
import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.entity.observations.AbstractObservation;
import simulator.model.entity.observations.ClosestEntityDistanceObservation;
import simulator.model.entity.observations.ClosestEntityPheromoneObservation;
import simulator.model.entity.observations.EntitiesCountObservation;
import simulator.model.map.Node;

public abstract class GIndividual extends AbstractIndividual{
	protected AbstractGrammar grammar;
	protected Genotype genotype;
	protected List<AbstractObservation>_observations;
	public GIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		this._observations = new ArrayList<AbstractObservation>();
		this._observations.add(new ClosestEntityDistanceObservation(this, MOVE.UP));
		this._observations.add(new ClosestEntityDistanceObservation(this, MOVE.DOWN));
		this._observations.add(new ClosestEntityDistanceObservation(this, MOVE.LEFT));
		this._observations.add(new ClosestEntityDistanceObservation(this, MOVE.RIGHT));
		this._observations.add(new EntitiesCountObservation(this, MOVE.UP));
		this._observations.add(new EntitiesCountObservation(this, MOVE.DOWN));
		this._observations.add(new EntitiesCountObservation(this, MOVE.LEFT));
		this._observations.add(new EntitiesCountObservation(this, MOVE.RIGHT));
		this._observations.add(new ClosestEntityPheromoneObservation(this, MOVE.UP));
		this._observations.add(new ClosestEntityPheromoneObservation(this, MOVE.DOWN));
		this._observations.add(new ClosestEntityPheromoneObservation(this, MOVE.LEFT));
		this._observations.add(new ClosestEntityPheromoneObservation(this, MOVE.RIGHT));
		// TODO Auto-generated constructor stub
	}
	public List<AbstractObservation> getObservations(){
		return this._observations;
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
