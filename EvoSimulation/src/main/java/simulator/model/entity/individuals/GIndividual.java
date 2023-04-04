package simulator.model.entity.individuals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import simulator.control.Controller;
import simulator.model.ActionI;
import simulator.model.EvoSimulator;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;
import simulator.model.entity.individuals.genome.Mapper;
import simulator.model.entity.individuals.genome.Phenotype;
import simulator.model.entity.observations.ObservationManager;
import simulator.model.map.Map;
import simulator.model.map.Node;

public abstract class GIndividual extends AbstractIndividual{
	protected Genotype genotype;
	protected Phenotype phenotype;
	
	protected ObservationManager observationManager;
	
	private int count = 0;
	
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
	public void perform(List<Entity>entities, Map map) {
		for(String actionid:grammars.keySet()) {
			int t = this.grammarController.getTime(actionid);
			if(t==-1) {//execute only once
				if(!this.exs.get(actionid)) {
					this.exs.put(actionid, true);
					this.phenotype.evaluate(actionid, this.observationManager.getVariables(), (MyIndividual) this);
				}
				else {
					continue;
				}
			}
			else if(count%t==0) {
				this.phenotype.evaluate(actionid, this.observationManager.getVariables(), (MyIndividual) this);
			}
		}
		count++;
		/*for(String actionid:grammars.keySet()) {
			Object election = this.phenotype.getNext(actionid, this.observationManager.getVariables());
			ActionI a = actions.get(actionid).get(election);
			if(a!=null)a.perform(this, entities, map);
			//else System.err.println("Action "+election+" not declared");
		}*/
	}
	@Override
	public void myInteract(Entity e2) {
		java.util.Map<String, InteractionI> interactions_l = interactions.getInteractions();
		for(String id:interactions_l.keySet()) {
			if(interactions.match(id, this.getClass(), e2.getClass())) {
				interactions_l.get(id).perform(this, e2, ctrl.getMap(), ctrl.getSimulator().getTime());
			}
		}
	}
	
	public static class Genes implements Mapper{
		static java.util.Map<String, List<Integer>>rules = new LinkedHashMap<>();
//		static {
//			rules = new LinkedHashMap<>();
//			rules.put("gen1", List.of(0));
//			rules.put("gen2", List.of(1,2,3));
//			rules.put("gen3", List.of(7,2,3));
//			rules.put("gen4", List.of(34));
//		}
		/**
		 * Loads genes specification from file (.genes)
		 * @param path
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
		public static void loadFromFile(String path) throws FileNotFoundException, IOException {
			rules = new LinkedHashMap<>();
			Properties properties = new Properties();
			properties.load(new FileInputStream(path));
			
			try {
				for(Object key:properties.keySet()) {
					String s[] = properties.getProperty((String)key).split(",");
					rules.put((String)key, Arrays.stream(s).map(e->Integer.valueOf(e)).collect(Collectors.toList()));
				}
			}catch(NumberFormatException ex) {
				System.err.println("Syntax error");
				ex.printStackTrace();
			}
			
			
		}
		@Override
		public Object mapChromosome(Chromosome<?> c) {
			return mapGenes((Chromosome<Boolean>) c);
		}
		@Override
		public HashSet<String> mapGenes(Chromosome<Boolean> c) {
			HashSet<String>r = new HashSet<>();
			for(String key:rules.keySet()) {
				if(mask(c.getCodons(), rules.get(key)))r.add(key);
			}
			return r;
		}
		private static boolean mask(List<Boolean>list, List<Integer>mask) {
			for(Integer m:mask) if(!list.get(m))return false;
			return true;
		}
		public static java.util.Map<String, List<Integer>> getRules(){
			return rules;
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
