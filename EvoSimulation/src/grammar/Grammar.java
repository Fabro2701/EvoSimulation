package grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Phenotype;

public class Grammar {
	public HashMap<Symbol, List<Production>> rules;

	private Symbol initial;
	
	

	public Grammar() {
		rules = new HashMap<Symbol, List<Production>>();
	}
	public static enum SymbolType{NTerminal,Terminal}
	public class Symbol {
		String name;
		String realValue;
		SymbolType type;
		public Symbol(String name, SymbolType type) {
			this.type = type;
			this.name = name;
			realValue = name;
		}
		public String getRealValue() {return name;}
		public SymbolType getType() {return type;}
		@Override
		public String toString() {return "<"+name+">";}

		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		public boolean equals(String s) {
			return this.name.equals(s);
		}
		public boolean equals(Symbol s) {
			return this.name.equals(s.name)&&this.type==s.type;
		}
	}

	

	public class Production {
		public List<Symbol> terms;
		int numTerms;
		public Production(Symbol... terms) {
			this.terms = new ArrayList<Symbol>();
			for (int i = 0; i < terms.length; i++) {
				this.terms.add(terms[i]);
			}
			numTerms = terms.length;
		}
		public int getNumTerms() {return numTerms;}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Symbol t : terms) {
				sb.append(t);
				sb.append(' ');

			}
			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}
	}
	public Symbol getInitial() {
		return initial;
	}

	public void setInitial(Symbol initial) {
		if(initial.getType()==SymbolType.Terminal)throw new IllegalArgumentException("The initial symbol has to be non-Terminal");
		this.initial = initial;
	}
	public void addRule(Symbol s, List<Production>l) {
		rules.put(s, l);
	}
	public List<Production> getRule(Symbol s){
		return rules.get(s);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Symbol nt : rules.keySet()) {
			sb.append(nt + " -> ");
			for (Production p : rules.get(nt)) {
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
		Grammar g=this;
		
		Symbol right = g.new Symbol("RIGHT",Grammar.SymbolType.Terminal);
		Symbol left = g.new Symbol("LEFT",Grammar.SymbolType.Terminal);
		Symbol up = g.new Symbol("UP",Grammar.SymbolType.Terminal);
		Symbol down = g.new Symbol("DOWN",Grammar.SymbolType.Terminal);
		Symbol neutral = g.new Symbol("NEUTRAL",Grammar.SymbolType.Terminal);
		Symbol _if = g.new Symbol("if",Grammar.SymbolType.Terminal);
		Symbol _then = g.new Symbol("_then_",Grammar.SymbolType.Terminal);
		Symbol _else = g.new Symbol("else",Grammar.SymbolType.Terminal);
		Symbol _endif = g.new Symbol("endif",Grammar.SymbolType.Terminal);
		Symbol r_f_d = g.new Symbol("r_f_d",Grammar.SymbolType.Terminal);
		Symbol l_f_d = g.new Symbol("l_f_d",Grammar.SymbolType.Terminal);
		Symbol u_f_d = g.new Symbol("u_f_d",Grammar.SymbolType.Terminal);
		Symbol d_f_d = g.new Symbol("d_f_d",Grammar.SymbolType.Terminal);
		Symbol _parent1 = g.new Symbol("(",Grammar.SymbolType.Terminal);
		Symbol _parent2 = g.new Symbol(")",Grammar.SymbolType.Terminal);
		Symbol _semicolon = g.new Symbol(";",Grammar.SymbolType.Terminal);
		Symbol _brack1 = g.new Symbol("{",Grammar.SymbolType.Terminal);
		Symbol _brack2 = g.new Symbol("}",Grammar.SymbolType.Terminal);
		Symbol _posF = g.new Symbol("posF",Grammar.SymbolType.Terminal);

		Symbol CODE = g.new Symbol("CODE",Grammar.SymbolType.NTerminal);
		Symbol LINE = g.new Symbol("LINE",Grammar.SymbolType.NTerminal);
		Symbol ACTION = g.new Symbol("ACTION",Grammar.SymbolType.NTerminal);
		Symbol IF = g.new Symbol("IF",Grammar.SymbolType.NTerminal);
		Symbol COND = g.new Symbol("COND",Grammar.SymbolType.NTerminal);
		Symbol OBS = g.new Symbol("OBS",Grammar.SymbolType.NTerminal);

		
		List<Production> pCode = new ArrayList<Production>();
		Production pCode1 = g.new Production(LINE);
		Production pCode2 = g.new Production(CODE,LINE);
		pCode.add(pCode1);
		pCode.add(pCode2);
		g.addRule(CODE, pCode);
		
		
		List<Production> pLine = new ArrayList<Production>();
		Production pLine1 = g.new Production(ACTION,_semicolon);
		Production pLine2 = g.new Production(IF);
		pLine.add(pLine1);
		pLine.add(pLine2);
		g.addRule(LINE,pLine);
		
		List<Production> pAction = new ArrayList<Production>();
		Production pA1 = g.new Production(right);
		Production pA2 = g.new Production(left);
		Production pA3 = g.new Production(up);
		Production pA4 = g.new Production(down);
		Production pA5 = g.new Production(neutral);
		pAction.add(pA1);
		pAction.add(pA2);
		pAction.add(pA3);
		pAction.add(pA4);
		pAction.add(pA5);
		g.addRule(ACTION, pAction);
		
		List<Production> pIf = new ArrayList<Production>();
		Production pIf1 = g.new Production(_if,COND,_brack1,LINE,_brack2,_else,_brack1,LINE,_brack2,_endif);
		Production pIf2 = g.new Production(_if,COND,_brack1,LINE,_brack2,_endif);
		pIf.add(pIf1);
		pIf.add(pIf2);
		g.rules.put(IF,pIf);
		
		List<Production> pCond = new ArrayList<Production>();
		Production pCond1 = g.new Production(_parent1,OBS,_parent2);
		pCond.add(pCond1);
		g.addRule(COND,pCond);

		
		List<Production> pObs = new ArrayList<Production>();
		Production pObs1 = g.new Production(r_f_d);
		Production pObs2 = g.new Production(l_f_d);
		Production pObs3 = g.new Production(u_f_d);
		Production pObs4 = g.new Production(d_f_d);
		pObs.add(pObs1);
		pObs.add(pObs2);
		pObs.add(pObs3);
		pObs.add(pObs4);
		g.addRule(OBS,pObs);
		
		g.setInitial(CODE);
	}
	public static void main(String args[]) {
		Grammar g = new Grammar("s");
		System.out.println(g);
		Chromosome c = new Chromosome(30);
		c.setIntToCodon(0, 1);
		c.setIntToCodon(1, 1);
		c.setIntToCodon(2, 1);
		c.setIntToCodon(3, 0);
		c.setIntToCodon(4, 1);
		c.setIntToCodon(5, 1);
		c.setIntToCodon(6, 0);
		c.setIntToCodon(7, 2);
		c.setIntToCodon(8, 0);
		c.setIntToCodon(9, 2);
		c.setIntToCodon(10, 1);
		c.setIntToCodon(11, 1);
		c.setIntToCodon(12, 0);
		c.setIntToCodon(13, 3);
		c.setIntToCodon(14, 0);
		c.setIntToCodon(15, 3);

		c.setIntToCodon(16, 1);
		c.setIntToCodon(17, 1);
		c.setIntToCodon(18, 0);
		c.setIntToCodon(19, 1);
		c.setIntToCodon(20, 0);
		c.setIntToCodon(21, 1);

		c.setIntToCodon(22, 1);
		c.setIntToCodon(23, 1);
		c.setIntToCodon(24, 0);
		c.setIntToCodon(25, 0);
		c.setIntToCodon(26, 0);
		c.setIntToCodon(27, 0);
		Phenotype pt = new Phenotype(c.parseGrammar(g));
		
		System.out.println(pt.getVisualCode());
		
		
	}
}
