package simulator.model.entity.individuals;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import simulator.control.Controller;
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
				if(this.isAlive())updates_l.get(id).accept(this, evoSimulator);
			}
		}
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
			if(interactions.match(id, this.getClass(), e2.getClass())) {
				interactions_l.get(id).perform(this, e2, ctrl.getMap());
			}
		}
	}
	
	public static class Genes implements Mapper{
		static java.util.Map<Integer, String>rules;
		static {
			rules = new LinkedHashMap<>();
			rules.put(0, "gen1");;
			rules.put(3, "gen2");;
			rules.put(7, "gen3");;
			rules.put(23, "gen4");
		}
		@Override
		public Object mapChromosome(Chromosome<?> c) {
			return mapGenes((Chromosome<Boolean>) c);
		}
		@Override
		public HashSet<String> mapGenes(Chromosome<Boolean> c) {
			HashSet<String>r = new HashSet<>();
//			for(int i=0;i<c.getLength();i++) {
//				if(c.getCodon(i))r.add(rules.get(i));
//			}
			IntStream.range(0, c.getLength())
					 .forEach(i->{if(c.getCodon(i)&&rules.containsKey(i))r.add(rules.get(i));});
			return r;
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
