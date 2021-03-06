package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.Grammar;
import grammar.Grammar.Production;
import grammar.Grammar.Symbol;
import simulator.RandomSingleton;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Chromosome {
	List<Codon>codons;
	int length;
	

	public Chromosome(int l) {
		length = l;
		codons = new ArrayList<Codon>(length);
		for(int i=0; i<length; i++) {
			codons.add(new Codon());
		}
	}
	public Chromosome(Chromosome copy) {
		length = copy.length;
		codons = new ArrayList<Codon>(length);
		for(int i=0; i<length; i++) {
			codons.add(new Codon(copy.codons.get(i)));
		}
	}
	public Chromosome(JSONObject o) {
		length = o.getInt("length");
		codons = new ArrayList<Codon>(length);
		JSONArray arr = o.getJSONArray("codons");
		for(int i=0; i<length; i++) {
			codons.add(new Codon(arr.getJSONObject(i).getInt("intValue")));
		}
	}
	public void setIntToCodon(int i, int v) {
		codons.set(i, new Codon(v));
	}
	public void setModToCodon(int i, int v) {
		codons.get(i).modValue=v;
	}
	public void setArrayIntToCodon(int ...v) {
		for(int i=0;i<v.length;i++) {
			codons.set(i, new Codon(v[i]));
		}
	}
	public LinkedList<Symbol> parseGrammar(Grammar g) {
		Symbol t = g.getInitial();
		List<Production> ps;
		LinkedList<Symbol> q = new LinkedList<Symbol>();
		LinkedList<Symbol> terminals = new LinkedList<Symbol>();
		// = new ArrayList<Production>(); 
		// ps.addAll(0,g.productions.get(init));
		int limit=100;
		int i=0;
		int cont=0;
		int calls=0;
		while(true) {
			ps = g.getRule(t);
			int m = ps.size();
			//int r = Util.toInt(codons.get(i).bits.get(0, Util.log2(m)));
			int r = codons.get(i).intValue%m;
			this.setModToCodon(i, r);
			q.addAll(0, ps.get(r).terms);
			
			//terminals.add(g.new Terminal("("));
			calls++;
			while(!q.isEmpty() && q.getFirst().getType()==Grammar.SymbolType.Terminal) {
				if(!q.getFirst().toString().equals(")"))cont++;
				terminals.add(q.pop());
			}
			
			if(q.isEmpty())break;
			
			t = q.pop();
			//q.add(0, g.new Terminal(")"));
			i++;
			i%=length;
			if(calls>=limit)return null;
		}
		//terminals.add(g.new Symbol(")",Grammar.SymbolType.Terminal));
		
		/*for(Symbol a:terminals) {
			System.out.print(a.getRealValue()+" ");
		}System.out.println();*/
		
		return terminals;
	}
	public void mutate(float mutationProb) {
		float r = (0.005f+mutationProb)/255.0f;
		if(RandomSingleton.nextFloat()<r) {
			codons.get(RandomSingleton.nextInt(codons.size())).setInt(RandomSingleton.nextInt(256-1));;
		}
	}
	public class Codon{
		BitSet bits;
		int intValue;
		int modValue;
		public Codon() {
			bits = new BitSet(8);
			intValue = RandomSingleton.nextInt(256-1);
		}
		public Codon(int n) {
			bits = new BitSet(8);
			intValue = n;
		}
		public Codon(JSONObject o) {
			bits = new BitSet(8);
			intValue = o.getInt("intValue");
		}
		public Codon(Codon copy) {
			bits = new BitSet(8);
			intValue = copy.intValue;
		}
		public void setInt(int v) {
			intValue=v;
		}
		public int getIntValue() {return this.intValue;}
		public int getModValue() {return this.modValue;}
		public JSONObject toJSON() {
			return new JSONObject().put("intValue", intValue);
		}
	}
	public int getLength() {return this.length;}
	public Codon getCodon(int i) {return this.codons.get(i);}
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Codon c:codons) {
			arr.put(c.toJSON());
		}
		return new JSONObject().put("length", this.length)
							   .put("codons", arr);
	}
}
