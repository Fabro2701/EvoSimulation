package simulator.model.entity.individuals;

import java.util.List;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.entity.ActiveEntity;
import simulator.model.entity.Entity;
import simulator.model.entity.observations.ObservationManager;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;

public abstract class AbstractIndividual extends ActiveEntity{

	protected ObservationManager observationManager;
	
	public AbstractIndividual(String id, Node n, Controller ctrl, String code) throws JSONException, EvaluationException {
		super(id, n, ctrl, code);

		observationManager = new ObservationManager(this);
	}
	public void updateObservations(List<Entity>entities, Map map) {
		observationManager.update(entities, map);
	}
	@Override
	public void update(EvoSimulator evoSimulator) throws IllegalArgumentException, EvaluationException {
		super.update(evoSimulator);
		observationManager.update(evoSimulator.getEntities(), evoSimulator.getMap());
	}
	public Object getObservation(String obs) {
		return this.observationManager.getVariables().getOrDefault(obs, null);
	}

}
