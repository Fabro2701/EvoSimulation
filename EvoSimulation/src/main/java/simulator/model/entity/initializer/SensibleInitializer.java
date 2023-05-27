package simulator.model.entity.initializer;

import static simulator.Constants.CHROMOSOME_LENGTH;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import grammar.AbstractGrammar;
import grammar.AbstractGrammar.Production;
import grammar.AbstractGrammar.Rule;
import grammar.AbstractGrammar.Symbol;
import grammar.AbstractGrammar.SymbolType;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.map.Node;

public class SensibleInitializer extends AbstractInitializer{

	@Override
	public List<Entity> createPopulation(AbstractGrammar grammar, Controller ctrl, int num, JSONObject properties, String clazz) {
		List<Entity> population = new ArrayList<Entity>();
		
		int maxDepth = properties.getInt("maxDepth");
		
		try {
			for(int i=0;i<num/2;i++) {
				population.add((Entity)Class.forName(clazz).getConstructor(String.class,Node.class,Chromosome.class,Controller.class)
														   .newInstance(ctrl.getNextId(), ctrl.randomNode(), this._generateFull(grammar, maxDepth), ctrl));
			}
			for(int i=0;i<num/2;i++) {
				population.add((Entity)Class.forName(clazz).getConstructor(String.class,Node.class,Chromosome.class,Controller.class)
														   .newInstance(ctrl.getNextId(), ctrl.randomNode(), this._generateGrow(grammar, maxDepth), ctrl));
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return population;
	}
	private Chromosome _generateGrow(AbstractGrammar grammar, int maxDepth) {
		Chromosome<Chromosome.Codon> c = new Chromosome<Chromosome.Codon>(CHROMOSOME_LENGTH, Chromosome.Codon::new);
		List<Integer>choices = new ArrayList<Integer>();
		Production p=null;
		Rule rule = null;
		Symbol current = null;
		int choice;
		
		int i=0;
		int currentDepth=0;
		LinkedList<Symbol>pending = new LinkedList<Symbol>();
		pending.add(grammar.getInitial());
		while(!pending.isEmpty()&&i<c.getLength()) {
			current = pending.pollFirst();
			choices.clear();
			rule = grammar.getRule(current);
			
			for(int pi = 0; pi<rule.size(); pi++) {
				p = rule.get(pi);
				if(currentDepth+p.get_minimumDepth()<=maxDepth) {
					choices.add(pi);
				}
			}
		
			if(choices.size()==0) {
				
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					choices.add(pi);
				}
				
			}
			
			choice = this._randomChoice(choices);
			//c.setIntToCodon(i, choice);
			pending.addAll(rule.get(choice).stream().filter(e->e.getType()==SymbolType.NTerminal).collect(Collectors.toList()));
			currentDepth += rule.get(choice).get_minimumDepth();
			i++;
		}
		//System.out.println("created depth2: "+currentDepth);
		return c;
	}
	private Chromosome _generateFull(AbstractGrammar grammar, int maxDepth) {
		Chromosome<Chromosome.Codon> c = new Chromosome<Chromosome.Codon>(CHROMOSOME_LENGTH, Chromosome.Codon::new);
		List<Integer>choices = new ArrayList<Integer>();
		Production p=null;
		Rule rule = null;
		Symbol current = null;
		int choice;
		
		int i=0;
		int currentDepth=0;
		LinkedList<Symbol>pending = new LinkedList<Symbol>();
		pending.add(grammar.getInitial());
		while(!pending.isEmpty()&&i<c.getLength()) {
			current = pending.pollFirst();
			choices.clear();
			rule = grammar.getRule(current);
			if(rule.is_recursive()) {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(p.is_recursive() && currentDepth+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
			}
			else {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(currentDepth+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
			}
			if(choices.size()==0) {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(!p.is_recursive() && currentDepth+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
				if(choices.size()==0) {
					for(int pi = 0; pi<rule.size(); pi++) {
						p = rule.get(pi);
						choices.add(pi);
					}
				}
			}
			
			choice = this._randomChoice(choices);
			//c.setIntToCodon(i, choice);
			pending.addAll(rule.get(choice).stream().filter(e->e.getType()==SymbolType.NTerminal).collect(Collectors.toList()));
			currentDepth += rule.get(choice).get_minimumDepth();
			i++;
		}
		//System.out.println("created depth: "+currentDepth);
		return c;
	}
	private int _randomChoice(List<Integer> choices) {
		return choices.get(RandomSingleton.nextInt(choices.size()));
	}
	
}
