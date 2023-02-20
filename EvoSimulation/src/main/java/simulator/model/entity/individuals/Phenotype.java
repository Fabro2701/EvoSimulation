package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import setup.OOPParser;
import simulator.model.entity.individuals.PolymorphismController.VARIATION;
import simulator.model.evaluation.ActionEvaluator;
import grammar.Evaluator;
import grammar.Parser;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Phenotype{
	String visualization;
	boolean valid;
	Map<String,Evaluator> evaluators;
	Map<String,String> codes;
	Map<String,JSONArray> grammarASTs;
	List<LinkedList<Symbol>> symbols;
	HashSet<String>genes;
	private Map<String, VARIATION> polys;
	
	OOPParser parser = new OOPParser() {
		@Override
		protected JSONObject Program() {
			return new JSONObject().put("list", this.Especification());
		}
	};
	
	public Phenotype() {
		super();
		symbols = new ArrayList<LinkedList<Symbol>>();
		//for(int i=0;i<Constants.PLOIDY;i++)symbols.add(null);
		valid = true;
		evaluators = new LinkedHashMap<String, Evaluator>();
		codes = new LinkedHashMap<>();
		grammarASTs = new LinkedHashMap<>();
		/*parser = new OOPParser() {
			@Override
			protected JSONObject Program() {
				return new JSONObject().put("body", this.Especification());
			}
		};*/
	}
	public Phenotype(JSONObject phenotype) {
 		System.err.println("pending");
	}
	public void evaluate(String key, HashMap<String,String>observations, MyIndividual ind) {
		ActionEvaluator eval = new ActionEvaluator(this.grammarASTs.get(key));
		java.util.Map<String, Object>vars = new HashMap<String, Object>();
		vars.put("this", ind);
		eval.evaluate(vars);
	}
	public Object getNext(String key, HashMap<String,String>observations) {
		Evaluator eval = evaluators.get(key);
		eval.addObservations(observations);
		return eval.getNextAction();
	}

	public void setSymbol(String key, LinkedList<Symbol> crom) {
		this.symbols.add(crom);
		if(crom==null) {
			this.valid=false;
			return;
		}
		String code = symbolRepresentation(crom);
		this.codes.put(key, code);
		
		
		this.grammarASTs.put(key, parser.parse(code).getJSONArray("list"));
		//this.evaluators.put(key, new Evaluator(parser.parse(symbolRepresentation(crom))));
	}
	public void setGenes(HashSet<String>genes) {
		this.genes = genes;
	}
	public boolean hasGene(String gene) {
		return this.genes.contains(gene);
	}

	public void setPolymorphims(Map<String, VARIATION> polys) {
		this.polys = polys;
	}
	public VARIATION getVariation(String gen) {
		return this.polys.get(gen);
	}
	
	
	private void setVisualization() {
		StringBuilder s = new StringBuilder();
		
		Iterator<Symbol>it = this.symbols.get(0).iterator();//pending
		StringBuilder tabs = new StringBuilder();
		while(it.hasNext()) {
			Symbol current = it.next();
			
			if(current.equals(";")) {
				s.append(current);
				s.append('\n');
			}
			else if(current.equals("{")) {
				s.append(current);
				s.append('\n');
				tabs.append("    ");
			}
			else if(current.equals("}")) {
				
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				s.append(tabs);
				s.append(current);
				s.append('\n');
			}
			else if(current.equals("endif")) {
				s.append(tabs);
				s.append(current);
				s.append('\n');
			}
			else if(current.equals("if")||current.equals("else")||current.equals("\"RIGHT\"")||current.equals("\"LEFT\"")||
					current.equals("\"UP\"")||current.equals("\"DOWN\"")||current.equals("\"NEUTRAL\"")){
				s.append(tabs);
				s.append(current);
			}
			else {
				s.append(current);
			}
		}
		visualization = s.toString();
	}
	public String getVisualCode() {
		if(visualization==null) {
			setVisualization();
		}
		return visualization;
	}
	private String symbolRepresentation(LinkedList<Symbol> ss) {
		StringBuilder st = new StringBuilder();
		ss.stream().forEach(s -> st.append(s));
		return st.toString();
	}
	public JSONObject toJSON() {
		JSONArray genes = new JSONArray();
		for(String gen:this.genes)genes.put(gen);
		
		return new JSONObject().put("genes", genes);
	}
}
