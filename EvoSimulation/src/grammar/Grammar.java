package grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grammar {
	public HashMap<NonTerminal, List<Production>> productions;
	public HashMap<String, NonTerminal> nonTerminals;
	public HashMap<String, Terminal> terminals;
	private Term initial;
	
	

	public Grammar() {
		productions = new HashMap<NonTerminal, List<Production>>();
		nonTerminals = new HashMap<String, NonTerminal>();
		terminals = new HashMap<String, Terminal>();
	}

	public class Term {
		String name;

		public Term(String name) {
			this.name = name;
		}

		@Override
		public String toString() {return name;}

		@Override
		public int hashCode() {
			return name.hashCode();
		}
	}

	class NonTerminal extends Term {
		public NonTerminal(String name) {
			super(name);
		}
	}

	public class Terminal extends Term {
		public Terminal(String name) {
			super(name);
		}

	}

	public class Production {
		public List<Term> terms;
		int numTerms;
		public Production(Term... terms) {
			this.terms = new ArrayList<Term>();
			for (int i = 0; i < terms.length; i++) {
				this.terms.add(terms[i]);
			}
			numTerms = terms.length;
		}
		public int getNumTerms() {return numTerms;}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Term t : terms) {
				sb.append(t);
				sb.append(' ');

			}
			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}
	}
	public Term getInitial() {
		return initial;
	}

	public void setInitial(Term initial) {
		this.initial = initial;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (NonTerminal nt : productions.keySet()) {
			sb.append(nt + " -> ");
			for (Production p : productions.get(nt)) {
				sb.append(p);
				sb.append('|');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append('\n');
		}
		return sb.toString();
	}
	public Grammar(String s) {
		this();
		Terminal a = new Terminal("a");
		Terminal b = new Terminal("b");
		Terminal c = new Terminal("c");

		NonTerminal A = new NonTerminal("A");
		NonTerminal B = new NonTerminal("B");
		NonTerminal C = new NonTerminal("C");

		List<Production> pA = new ArrayList<Production>();
		Production pA1 = new Production(a, C, a, B, b, B);
		Production pA2 = new Production(a, A);
		pA.add(pA1);
		pA.add(pA2);
		this.productions.put(A, pA);

		List<Production> pB = new ArrayList<Production>();

		Production pB1 = new Production(a, B);
		Production pB2 = new Production(C, b);
		Production pB3 = new Production(b,C,b);
		pB.add(pB3);
		pB.add(pB1);
		pB.add(pB2);
		this.productions.put(B, pB);

		List<Production> pC = new ArrayList<Production>();
		Production pC1 = new Production(c);
		Production pC2 = new Production(C, c);
		pC.add(pC1);
		pC.add(pC2);
		this.productions.put(C, pC);
		
		
		nonTerminals.put("A",A);
		nonTerminals.put("B",B);
		nonTerminals.put("C",C);
		terminals.put("a",a);
		terminals.put("b",b);
		terminals.put("c",c);
		terminals.put(")",new Terminal(")"));
		//terminals.put("(",new Terminal("("));
		
		this.setInitial(A);
	}
	public static void main(String args[]) {
		Grammar g = new Grammar();

		Terminal a = g.new Terminal("a");
		Terminal b = g.new Terminal("b");
		Terminal c = g.new Terminal("c");

		NonTerminal A = g.new NonTerminal("A");
		NonTerminal B = g.new NonTerminal("B");
		NonTerminal C = g.new NonTerminal("C");

		List<Production> pA = new ArrayList<Production>();
		Production pA1 = g.new Production(B, a, b, C);
		Production pA2 = g.new Production(a);
		pA.add(pA1);
		pA.add(pA2);
		g.productions.put(A, pA);

		List<Production> pB = new ArrayList<Production>();
		Production pB1 = g.new Production(A, B);
		Production pB2 = g.new Production(C, B);
		Production pB3 = g.new Production(b);
		pB.add(pB1);
		pB.add(pB2);
		pB.add(pB3);
		g.productions.put(B, pB);

		List<Production> pC = new ArrayList<Production>();
		Production pC1 = g.new Production(C, c);
		pC.add(pC1);
		g.productions.put(C, pC);

		System.out.println(new Grammar("d"));

	}
}
