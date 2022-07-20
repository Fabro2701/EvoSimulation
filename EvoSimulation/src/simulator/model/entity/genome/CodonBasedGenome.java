package simulator.model.entity.genome;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import grammar.Grammar;
import grammar.Grammar.Production;
import grammar.Grammar.Term;
import simulator.RandomSingleton;
import util.Util;

public class CodonBasedGenome extends Genome{
	List<Codon>codons;
	int length;
	
	public CodonBasedGenome(int l) {
		length = l;
		codons = new ArrayList<Codon>();
		for(int i=0; i<length; i++) {
			codons.add(new Codon());
		}
	}
	
	public void parseGrammar(Grammar g) {
		Term t = g.getInitial();
		List<Production> ps;
		LinkedList<Term> q = new LinkedList<Term>();
		LinkedList<Term> terminals = new LinkedList<Term>();
		// = new ArrayList<Production>(); 
		// ps.addAll(0,g.productions.get(init));
		int limit=100;
		int i=0;
		int cont=0;
		int calls=0;
		while(true) {
			
			ps = g.productions.get(t);
			int m = ps.size();
			//int r = Util.toInt(codons.get(i).bits.get(0, Util.log2(m)));
			int r = codons.get(i).intValue%m;
			q.addAll(0, ps.get(r).terms);
			
			terminals.add(g.new Terminal("("));calls++;
			while(!q.isEmpty() && g.terminals.containsKey(q.getFirst().toString())) {
				if(!q.getFirst().toString().equals(")"))cont++;
				terminals.add(q.pop());
			}
			//terminals.add(g.new Terminal(")"));
			
			if(q.isEmpty())break;
			
			t = q.pop();
			q.add(0, g.new Terminal(")"));
			i++;
			i%=length;
			if(calls>=limit)return;
		}
		terminals.add(g.new Terminal(")"));
		
		for(Term a:terminals) {
			System.out.print(a);
		}System.out.println();
		System.out.println(calls);
	}
	
	class Codon{
		BitSet bits;
		int intValue;
		public Codon() {
			bits = new BitSet(8);
			intValue = RandomSingleton.nextInt(256-1);
		}
		public Codon(int n) {
			bits = new BitSet(8);
			intValue = n;
		}
	}
	
	
	public static void main(String args[]) {
		Grammar g = new Grammar("d");
		System.out.println(g);
		CodonBasedGenome geno = new CodonBasedGenome(30);

		geno.parseGrammar(g);
	}
}
