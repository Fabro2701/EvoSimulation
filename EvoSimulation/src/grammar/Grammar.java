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
		public String toString() {return type==SymbolType.NTerminal?"<"+name+">":name;}

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
		Symbol _posF = g.new Symbol("posF ",Grammar.SymbolType.Terminal);
		Symbol _less = g.new Symbol("<",Grammar.SymbolType.Terminal);
		Symbol _greater = g.new Symbol(">",Grammar.SymbolType.Terminal);
		Symbol _lessE = g.new Symbol("<=",Grammar.SymbolType.Terminal);
		Symbol _greaterE = g.new Symbol(">=",Grammar.SymbolType.Terminal);
		Symbol _0 = g.new Symbol("0",Grammar.SymbolType.Terminal);
		Symbol _1 = g.new Symbol("1",Grammar.SymbolType.Terminal);
		Symbol _2 = g.new Symbol("2",Grammar.SymbolType.Terminal);
		Symbol _3 = g.new Symbol("3",Grammar.SymbolType.Terminal);
		Symbol _4 = g.new Symbol("4",Grammar.SymbolType.Terminal);
		Symbol _5 = g.new Symbol("5",Grammar.SymbolType.Terminal);
		Symbol _6 = g.new Symbol("6",Grammar.SymbolType.Terminal);
		Symbol _7 = g.new Symbol("7",Grammar.SymbolType.Terminal);

		Symbol CODE = g.new Symbol("CODE",Grammar.SymbolType.NTerminal);
		Symbol LINE = g.new Symbol("LINE",Grammar.SymbolType.NTerminal);
		Symbol ACTION = g.new Symbol("ACTION",Grammar.SymbolType.NTerminal);
		Symbol IF = g.new Symbol("IF",Grammar.SymbolType.NTerminal);
		Symbol COND = g.new Symbol("COND",Grammar.SymbolType.NTerminal);
		Symbol OBS = g.new Symbol("OBS",Grammar.SymbolType.NTerminal);
		Symbol OP = g.new Symbol("OP",Grammar.SymbolType.NTerminal);
		Symbol Pi = g.new Symbol("Pi",Grammar.SymbolType.NTerminal);
		Symbol V = g.new Symbol("V",Grammar.SymbolType.NTerminal);

		
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
		Production pCond1 = g.new Production(_parent1,_posF,OBS,_parent2);
		Production pCond2 = g.new Production(_parent1,OBS,OP,OBS,_parent2);
		Production pCond3 = g.new Production(_parent1,V,OP,Pi,_parent2);
		Production pCond4 = g.new Production(_parent1,Pi,OP,Pi,_parent2);
		pCond.add(pCond1);
		pCond.add(pCond2);
		pCond.add(pCond3);
		pCond.add(pCond4);
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
		
		List<Production> pOps = new ArrayList<Production>();
		Production pOps1 = g.new Production(_less);
		Production pOps2 = g.new Production(_greater);
		Production pOps3 = g.new Production(_lessE);
		Production pOps4 = g.new Production(_greaterE);
		pOps.add(pOps1);
		pOps.add(pOps2);
		pOps.add(pOps3);
		pOps.add(pOps4);
		g.addRule(OP,pOps);
		
		List<Production> pPis = new ArrayList<Production>();
		Production pPis0 = g.new Production(_0);
		Production pPis1 = g.new Production(_1);
		Production pPis2 = g.new Production(_2);
		Production pPis3 = g.new Production(_3);
		Production pPis4 = g.new Production(_4);
		Production pPis5 = g.new Production(_5);
		Production pPis6 = g.new Production(_6);
		Production pPis7 = g.new Production(_7);
		pPis.add(pPis0);
		pPis.add(pPis1);
		pPis.add(pPis2);
		pPis.add(pPis3);
		pPis.add(pPis4);
		pPis.add(pPis5);
		pPis.add(pPis6);
		pPis.add(pPis7);
		g.addRule(Pi,pPis);
		
		List<Production> pVs = new ArrayList<Production>();
		Production pVs0 = g.new Production(_0);
		Production pVs1 = g.new Production(_1);
		Production pVs2 = g.new Production(_2);
		Production pVs3 = g.new Production(_3);
		Production pVs4 = g.new Production(_4);
		Production pVs5 = g.new Production(_5);
		Production pVs6 = g.new Production(_6);
		Production pVs7 = g.new Production(_7);
		pVs.add(pVs0);
		pVs.add(pVs1);
		pVs.add(pVs2);
		pVs.add(pVs3);
		pVs.add(pVs4);
		pVs.add(pVs5);
		pVs.add(pVs6);
		pVs.add(pVs7);
		g.addRule(V,pVs);
		
		g.setInitial(CODE);
	}
	public static void main(String args[]) {
		Grammar g = new Grammar("s");
		System.out.println(g);
		Chromosome c = new Chromosome(100);
		//c.setArrayIntToCodon(1,0, 1,0,1,2,3,3,0,2,1,1,0,3,0,3, 1,0,1,0,3,1,0,0,1, 0,0,1,0,1,0,  2);
		
		Phenotype pt = new Phenotype(c.parseGrammar(g));
		
		System.out.println(pt.getVisualCode());
		
		
	}
}
