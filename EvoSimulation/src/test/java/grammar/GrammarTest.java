package grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import grammar.AbstractGrammar.Production;
import grammar.AbstractGrammar.Rule;
import grammar.AbstractGrammar.Symbol;
import simulator.model.entity.individuals.genome.Chromosome;

class GrammarTest {

	@Test
	void testAddRule() {
		StandardGrammar g = new StandardGrammar();
		Symbol CODE = g.new Symbol("CODE",Grammar.SymbolType.NTerminal);
		Symbol LINE = g.new Symbol("LINE",Grammar.SymbolType.NTerminal);
		Rule pCode = g.new Rule();
		pCode.set_symbol(CODE);
		Production pCode1 = g.new Production(LINE);
		Production pCode2 = g.new Production(CODE,LINE);
		pCode.add(pCode1);
		pCode.add(pCode2);
		g.addRule(CODE, pCode);
		assertEquals(g.getRule(CODE).get(0).toString(),"<LINE>");
		assertEquals(g.getRule(CODE).get(1).toString(),"<CODE> <LINE>");
	}
	StandardGrammar testGrammar() {
		StandardGrammar g = new StandardGrammar();
		
		Symbol right = g.new Symbol("\"RIGHT\"",Grammar.SymbolType.Terminal);
		Symbol left = g.new Symbol("\"LEFT\"",Grammar.SymbolType.Terminal);
		Symbol up = g.new Symbol("\"UP\"",Grammar.SymbolType.Terminal);
		Symbol down = g.new Symbol("\"DOWN\"",Grammar.SymbolType.Terminal);
		Symbol neutral = g.new Symbol("\"NEUTRAL\"",Grammar.SymbolType.Terminal);
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
		Symbol _plus = g.new Symbol("+",Grammar.SymbolType.Terminal);
		Symbol _times = g.new Symbol("*",Grammar.SymbolType.Terminal);
		Symbol _minus = g.new Symbol("-",Grammar.SymbolType.Terminal);

		Symbol CODE = g.new Symbol("CODE",Grammar.SymbolType.NTerminal);
		Symbol LINE = g.new Symbol("LINE",Grammar.SymbolType.NTerminal);
		Symbol ACTION = g.new Symbol("ACTION",Grammar.SymbolType.NTerminal);
		Symbol IF = g.new Symbol("IF",Grammar.SymbolType.NTerminal);
		Symbol COND = g.new Symbol("COND",Grammar.SymbolType.NTerminal);
		Symbol OBS = g.new Symbol("OBS",Grammar.SymbolType.NTerminal);
		Symbol OP = g.new Symbol("OP",Grammar.SymbolType.NTerminal);
		Symbol Pi = g.new Symbol("Pi",Grammar.SymbolType.NTerminal);
		Symbol V = g.new Symbol("V",Grammar.SymbolType.NTerminal);
		Symbol AR = g.new Symbol("AR",Grammar.SymbolType.NTerminal);
		Symbol AREXP = g.new Symbol("AREXP",Grammar.SymbolType.NTerminal);

		/*
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
		Production pIf1 = g.new Production(_if,COND,_brack1,LINE,_brack2,_else,_brack1,LINE,_brack2);
		Production pIf2 = g.new Production(_if,COND,_brack1,LINE,_brack2);
		pIf.add(pIf1);
		pIf.add(pIf2);
		g.rules.put(IF,pIf);
		
		List<Production> pCond = new ArrayList<Production>();
		Production pCond1 = g.new Production(_parent1,OBS,_parent2);
		Production pCond2 = g.new Production(_parent1,OBS,OP,OBS,_parent2);
		Production pCond4 = g.new Production(_parent1,V,OP,Pi,_parent2);
		Production pCond5 = g.new Production(_parent1,Pi,OP,Pi,_parent2);
		Production pCond6 = g.new Production(_parent1,_parent1,AREXP,_parent2,OP,_parent1,AREXP,_parent2,_parent2);
		pCond.add(pCond1);
		pCond.add(pCond2);
		//pCond.add(pCond4);
		//pCond.add(pCond5);
		pCond.add(pCond6);
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
		
		List<Production> pARs = new ArrayList<Production>();
		Production pARs0 = g.new Production(_plus);
		Production pARs1 = g.new Production(_times);
		Production pARs2 = g.new Production(_minus);
		pARs.add(pARs0);
		pARs.add(pARs1);
		pARs.add(pARs2);
		g.addRule(AR,pARs);
		
		List<Production> pAREXPs = new ArrayList<Production>();
		Production pAREXPs0 = g.new Production(OBS);
		Production pAREXPs1 = g.new Production(V);
		Production pAREXPs2 = g.new Production(AREXP,AR,AREXP);
		pAREXPs.add(pAREXPs0);
		pAREXPs.add(pAREXPs1);
		pAREXPs.add(pAREXPs2);
		g.addRule(AREXP,pAREXPs);
		*/
		g.setInitial(CODE);
		return g;
	}
	@Test
	void testGrammarString() {
		StandardGrammar g = this.testGrammar();
		
		assertEquals(g.getInitial().toString(),"<CODE>");
		
		String s = "<OBS> -> r_f_d | l_f_d | u_f_d | d_f_d\n"
				+ "<OP> -> < | > | <= | >=\n"
				+ "<AR> -> + | * | -\n"
				+ "<CODE> -> <LINE> | <CODE> <LINE>\n"
				+ "<ACTION> -> \"RIGHT\" | \"LEFT\" | \"UP\" | \"DOWN\" | \"NEUTRAL\"\n"
				+ "<V> -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7\n"
				+ "<AREXP> -> <OBS> | <V> | <AREXP> <AR> <AREXP>\n"
				+ "<LINE> -> <ACTION> ; | <IF>\n"
				+ "<Pi> -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7\n"
				+ "<IF> -> if <COND> { <LINE> } else { <LINE> } | if <COND> { <LINE> }\n"
				+ "<COND> -> ( <OBS> ) | ( <OBS> <OP> <OBS> ) | ( ( <AREXP> ) <OP> ( <AREXP> ) )\n"
				+ "";
		assertEquals(s,g.toString());
	}
	@Test
	public void testParse() {
		StandardGrammar g = this.testGrammar();
		Chromosome c = new Chromosome(50);
		c.setArrayIntToCodon(1,0, 1,0,1,2,1,3,0,2,1,1,0,3,0,3, 1,0,1,0,1,1,0,0,1, 0,0,1,0,1,0,  0);
		
		LinkedList<Symbol> l = g.parse(c);
		
		List<String>expected = List.of("if", "(", "u_f_d", ">", "d_f_d", ")", "{", "\"UP\"", ";", "}", "else", "{", "if", "(", "d_f_d", ")", "{", "\"DOWN\"", ";", "}", "}", "if", "(", "r_f_d", ">", "l_f_d", ")", "{", "\"RIGHT\"", ";", "}", "else", "{", "if", "(", "l_f_d", ")", "{", "\"LEFT\"", ";", "}", "else", "{", "\"RIGHT\"", ";", "}", "}");
		for(int i=0;i<l.size();i++) {
			assertEquals(expected.get(i),l.get(i).toString());
		}
	}
}
