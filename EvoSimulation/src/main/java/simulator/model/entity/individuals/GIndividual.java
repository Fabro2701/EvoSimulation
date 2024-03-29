package simulator.model.entity.individuals;

import static simulator.Constants.CHROMOSOME_LENGTH;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.JSONException;

import grammar.AbstractGrammar.Symbol;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.model.ActionI;
import simulator.model.EvoSimulator;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;
import simulator.model.entity.individuals.genome.Mapper;
import simulator.model.entity.individuals.genome.Phenotype;
import simulator.model.entity.individuals.genome.PolymorphismController;
import simulator.model.entity.individuals.genome.PolymorphismController.VARIATION;
import simulator.model.entity.observations.ObservationManager;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;
import statistics.StatsManager;

public abstract class GIndividual extends AbstractIndividual{
	protected Genotype genotype;
	protected Phenotype phenotype;
	
	private static Supplier<Chromosome.Codon>suppCodon = Chromosome.Codon::new;
	private static Supplier<Boolean>suppBool = ()->RandomSingleton.nextBoolean();
	private static Supplier<Float>suppFloat = ()->RandomSingleton.nextFloat();
	
	private int count = 0;
	
	
	public GIndividual(String id, Node node, Controller ctrl, String code) throws JSONException, EvaluationException {
		super(id, node, ctrl, code);

		genotype = new Genotype();
		phenotype = new Phenotype();
		
		//grammars chroms
		for(String key:grammars.keySet()) {
			Chromosome<Chromosome.Codon> c = new Chromosome<Chromosome.Codon>(CHROMOSOME_LENGTH, suppCodon);
			genotype.addChromosome(c);
			LinkedList<Symbol> crom = (LinkedList<Symbol>) grammars.get(key).mapChromosome(c);
			phenotype.setSymbol(key, crom);
			if(phenotype.isValid()==false) {
				for(StatsManager sm:ctrl.getStatsManagers())sm.onEvent("offspringdeath");
				dispose();
				return;
			}
			//System.out.println(phenotype.getVisualCode());
		}
		
		//genes chrom
		Chromosome<Boolean> c = new Chromosome<Boolean>(CHROMOSOME_LENGTH, suppBool);
		GIndividual.Genes genesMapper = new GIndividual.Genes();
		HashSet<String> genes = genesMapper.mapGenes(c);
		phenotype.setGenes(genes);
		genotype.addChromosome(c);
		
		//polymorphims chrom
		Chromosome<Float> c2 = new Chromosome<Float>(CHROMOSOME_LENGTH, suppFloat);
		java.util.Map<String, VARIATION> polys = new PolymorphismController().mapPolymorphisms(c2);
		phenotype.setPolymorphims(polys);
		genotype.addChromosome(c2);
		
		

		init();
	}
	
	@Override
	public void perform(List<Entity>entities, Map map) throws EvaluationException {
		for(String actionid:grammars.keySet()) {
			int t = this.grammarController.getTime(actionid);
			if(t==-1) {//execute only once
				if(!this.exs.get(actionid)) {
					this.exs.put(actionid, true);
					this.phenotype.evaluate(actionid, this.observationManager.getVariables(), (MyIndividual) this);
					Object r = this.getAttribute("result"+actionid);
					if(r!=null) {
						ActionI act = actions.get(actionid).get(r);
						act.perform(this, entities, map);
					}
				}
				else {
					continue;
				}
			}
			else if(count%t==0) {
				this.phenotype.evaluate(actionid, this.observationManager.getVariables(), (MyIndividual) this);
				Object r = this.getAttribute("result"+actionid);
				if(r!=null) {
					ActionI act = actions.get(actionid).get(r);
					act.perform(this, entities, map);
				}
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
