package simulator.model.entity.individuals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import grammar.Grammar.Symbol;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Phenotype extends LinkedList<Symbol>{
	String visualization;
	Iterator<Symbol>it;
	Symbol current;
	public Phenotype() {
		super();
	}
	public Phenotype(LinkedList<Symbol>l) {
		super(l);
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
		if(it==null||!it.hasNext())it=this.iterator();
		current=it.next();
		boolean jump=false;
		while(true) {
			if(current.equals("UP")||current.equals("DOWN")||current.equals("LEFT")||current.equals("RIGHT")||current.equals("NEUTRAL")) {
				return current;
			}
			else if(current.equals("if")) {
				it.next();//(
				Symbol cond = it.next();
				current=cond;
				boolean c = observations.get(cond.getRealValue())>0;
				if(!c) {
					it.next();//)
					jump=true;
					jumpToEndIf();
				}
			}
			else if(current.equals("else")&&jump) {
				it.next();
				jump=false;
				
			}
			else {
				if(!it.hasNext())it=this.iterator();
				current=it.next();
			}
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
