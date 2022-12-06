package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import grammar.Evaluator;
import grammar.Parser;
import simulator.Constants;
import simulator.Constants.ACTION;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Phenotype{
	String visualization, code;
	boolean valid;
	Map<String,Evaluator> evaluators;
	List<LinkedList<Symbol>> symbols;
	HashSet<String>genes;
	Parser parser = new Parser();
	public Phenotype() {
		super();
		symbols = new ArrayList<LinkedList<Symbol>>();
		//for(int i=0;i<Constants.PLOIDY;i++)symbols.add(null);
		valid=true;
		evaluators = new LinkedHashMap<String, Evaluator>();
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
		this.evaluators.put(key, new Evaluator(parser.parse(symbolRepresentation(crom))));
	}
	public void setGenes(HashSet<String>genes) {
		this.genes = genes;
	}
	public boolean hasGene(String gene) {
		return this.genes.contains(gene);
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
	public String getCode() {
		if(code==null) {
			StringBuilder st = new StringBuilder();
			//this.stream().forEach(s -> st.append(s));pending
			code = st.toString();
		}
		return code;
	}
	public JSONObject toJSON() {
		return new JSONObject().put("code", this.getVisualCode());
	}
}
