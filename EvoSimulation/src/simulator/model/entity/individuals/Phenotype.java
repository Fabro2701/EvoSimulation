package simulator.model.entity.individuals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.Evaluator;
import grammar.Grammar.Symbol;
import grammar.Parser;
import simulator.Constants.MOVE;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Phenotype extends LinkedList<Symbol>{
	String visualization;
	Iterator<Symbol>it;
	Symbol current;
	boolean valid;
	Evaluator evaluator;
	public Phenotype() {
		super();
		valid=false;
	}
	public Phenotype(LinkedList<Symbol>l) {
		super(l);
		valid=true;
		Parser parser = new Parser();
		evaluator = new Evaluator(parser.parse(this.getVisualCode()));
	}
	public MOVE getNext(HashMap<String,String>observations) {
		evaluator.addObservations(observations);
		return evaluator.getNext();
	}
	
	
	private void setVisualization() {
		StringBuilder s = new StringBuilder();
		
		Iterator<Symbol>it = this.iterator();
		StringBuilder tabs = new StringBuilder();
		while(it.hasNext()) {
			Symbol current = it.next();
			
			if(current.equals(";")) {
				s.append(current.getRealValue());
				s.append('\n');
			}
			else if(current.equals("{")) {
				s.append(current.getRealValue());
				s.append('\n');
				tabs.append("    ");
			}
			else if(current.equals("}")) {
				
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				s.append(tabs);
				s.append(current.getRealValue());
				s.append('\n');
			}
			else if(current.equals("endif")) {
				s.append(tabs);
				s.append(current.getRealValue());
				s.append('\n');
			}
			else if(current.equals("if")||current.equals("else")||current.equals("RIGHT")||current.equals("LEFT")||
					current.equals("UP")||current.equals("DOWN")||current.equals("NEUTRAL")){
				s.append(tabs);
				s.append(current.getRealValue());
			}
			else {
				s.append(current.getRealValue());
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
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		
		for(Symbol s:this){
			arr.put(s.getRealValue());
		}
		return new JSONObject().put("symbols", arr);
	}
}
