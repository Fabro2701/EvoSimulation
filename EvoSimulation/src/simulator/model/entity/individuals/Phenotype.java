package simulator.model.entity.individuals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import grammar.Grammar.Symbol;
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
	public Phenotype() {
		super();
		valid=false;
	}
	public Phenotype(LinkedList<Symbol>l) {
		super(l);
		valid=true;
	}
	public void jumpToEndIf(){
		int cont=0;
		while(true) {
			current=it.next();
			if(current.equals("{")) {
				cont++;
			}
			else if(current.equals("}")) {
				cont--;
			}
			if(cont==0) {
				break;
			}
		}
	}
	public Symbol getNext(HashMap<String, Integer>observations) {
		int cont=0;
		int limit=50;
		if(!valid)return null;
		if(it==null||!it.hasNext())it=this.iterator();
		current=it.next();
		boolean jump=false;
		while(true) {
			cont++;
			if(current.equals("UP")||current.equals("DOWN")||current.equals("LEFT")||current.equals("RIGHT")||current.equals("NEUTRAL")) {
				return current;
			}
			else if(current.equals("if")) {
				it.next();//(
				boolean c=false;
				current =it.next();
				if(current.equals("posF ")) {//monary
					Symbol cond = it.next();
					current=cond;
					c = observations.get(cond.getRealValue())>0;
				}
				else {
					Symbol l = current;
					Symbol op = it.next();
					Symbol r = it.next();
					switch (op.getRealValue()) {
						case "<":
							c=observations.get(l.getRealValue())<observations.get(r.getRealValue());
							break;
						case ">":
							c=observations.get(l.getRealValue())>observations.get(r.getRealValue());
							break;
						case "<=":
							c=observations.get(l.getRealValue())<=observations.get(r.getRealValue());
							break;
						case ">=":
							c=observations.get(l.getRealValue())>=observations.get(r.getRealValue());
							break;
					}
					current=r;
				}
				if(!c) {
					it.next();//)
					jump=true;
					jumpToEndIf();
				}
			}
			else if(current.equals("else")) {
				if(jump) {
					it.next();
					jump=false;
				}
				else {
					jumpToEndIf();
				}
			}
			else {
				if(!it.hasNext())it=this.iterator();
				current=it.next();
			}
			if(cont>=limit)return null;
		}
		
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
}
