package grammar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar.Symbol;
import grammar.bnf.BNFMeritParser;
import grammar.derivations.DerivationTree;
import grammar.derivations.TreeNode;
import simulator.RandomSingleton;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.entity.individuals.Phenotype;

public class BiasedGrammar extends AbstractGrammar{
	HashMap<Symbol,ArrayList<Integer>>_rulesMerits;
	public BiasedGrammar() {
		super();
		//_rulesProductions = new HashMap<Symbol,ArrayList<Production>>();
		_rulesMerits = new HashMap<Symbol,ArrayList<Integer>>();
	}
	@Override
	public LinkedList<Symbol> mapGrammar(Chromosome<Chromosome.Codon> c){
		Symbol t = this.getInitial();
		Production ps;
		LinkedList<Symbol> q = new LinkedList<Symbol>();
		LinkedList<Symbol> terminals = new LinkedList<Symbol>();
	
		int limit=150;
		int i=0;
		int calls=0;
		while(true) {
			int pidx = _getPseudoRandomIndex(this._rulesMerits.get(t),c.getCodon(i).getIntValue());
			
			ps = this._rulesProductions.get(t).get(pidx);
			//int r = Util.toInt(codons.get(i).bits.get(0, Util.log2(m)));
			
			q.addAll(0, ps);
			
			calls++;
			while(!q.isEmpty() && q.getFirst().getType()==AbstractGrammar.SymbolType.Terminal) {
				terminals.add(q.pop());
			}
			
			if(q.isEmpty())break;
			
			t = q.pop();
			i++;
			i %= c.getLength();
			if(calls>=limit)return null;
		}
		c.setUsedCodons(calls);

		
		return terminals;
	}
	
	private int _getPseudoRandomIndex(ArrayList<Integer> merits, int codonValue) {
		float sum = merits.stream().mapToInt(e->e).sum();
		int r = codonValue%(int)sum;
		int acc = -1;
		for(int i=0;i<merits.size();i++) {
			acc += merits.get(i);
			if(acc >= r) {
				return i;
			}
		}
		return 0;
		/*float sum = merits.stream().mapToInt(e->e).sum();
		r *= sum;
		float acc = 0.f;
		for(int i=0;i<merits.size();i++) {
			acc += (float)merits.get(i);
			if(acc >= r) {
				return i;
			}
		}
		return 0;*/
	}
	public Production getProduction(int codonValue, Symbol symbol) {
		
		int pidx = _getPseudoRandomIndex(this._rulesMerits.get(symbol),codonValue);		
		return this._rulesProductions.get(symbol).get(pidx);
	}
	@Override
	public void parseBNF(String filename) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/loads/grammars/"+filename+".bnf")));
			String aux = reader.readLine();
			while(aux!=null) {
				sb.append(aux);
				aux = reader.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String string = sb.toString();
		
		BNFMeritParser parser = new BNFMeritParser();
		JSONObject o = parser.parse(string);
		
		JSONArray rules = o.getJSONArray("rules");
		for(int i=0;i<rules.length();i++) {
			JSONObject rule = rules.getJSONObject(i);
			Rule ruleO = new Rule();
			
			String name = rule.getJSONObject("name").getString("id");
			Symbol nameS = new Symbol(name,AbstractGrammar.SymbolType.NTerminal);
			
			if(i==0)this.setInitial(nameS);
			ruleO.set_symbol(nameS);
			
			
			JSONArray productions = rule.getJSONArray("productions");
			ArrayList<Integer> ms = new ArrayList<Integer>();
			
			for(int j=0;j<productions.length();j++) {
				JSONObject p = productions.getJSONObject(j);
				int merit = p.getInt("merit");
				ms.add(merit);
				Production production = new Production();
				JSONArray symbols = p.getJSONArray("symbols");
				for(int k=0; k<symbols.length();k++) {
					JSONObject s = symbols.getJSONObject(k);
					if(s.getString("type").equals("Terminal")) {
						Symbol inS = new Symbol(s.getString("id"),AbstractGrammar.SymbolType.Terminal);
						production.add(inS);
					}
					else {
						Symbol inS = new Symbol(s.getString("id"),AbstractGrammar.SymbolType.NTerminal);
						production.add(inS);
					}
				}
				ruleO.add(production);
			}
			_addRule(nameS,ruleO,ms);
		}
		
		
	}
	
	
	private void _addRule(Symbol nameS, Rule ruleO, ArrayList<Integer> ms) {
		this._rulesProductions.put(nameS, ruleO);
		this._rulesMerits.put(nameS, ms);
		
	}
	public void globalUpdate(List<Entity>entities) {
		boolean debug=true;
		Entity e = entities.stream().max(Comparator.comparing(Entity::getFoodEaten)).get();
		
		if(!(RandomSingleton.nextFloat()<=((float)e.getFoodEaten()/30.f))) {
			return;
		}
		if(debug)System.out.println("-------UPDATING GRAMMAR----------");
		
		if(debug)System.out.println("age: "+e.getAge());
		if(debug)System.out.println("maxfoodeaten: "+e.getFoodEaten());
		DerivationTree t = new DerivationTree(this);
		
		boolean b = t.buildFromChromosome(((GIndividual)e).getGenotype().getChromosome(0));//pending
		if(debug)System.out.println("initial tree: ");
		if(debug)System.out.println(t);
		
		if(!b) {
			System.err.println("Error building derivation tree from chromosome");
			return;
		}
		
		DerivationTree newT = getDeepestPropagated(t);
		
		Production newP = new Production();
		
		for(TreeNode n:newT.getRoot().get_children()) {
			newP.add(n.getData());
		}
		if(debug) {
			System.out.println("new tree");
			System.out.println(newT);
			System.out.println("new prod");
			System.out.println(newT.getRoot().getData()+" -> " + newP);
		}
		
		_modifyGrammar(newT.getRoot().getData(), newP);
		if(debug)System.out.println("new grammar");
		if(debug)System.out.println(this);

		
	}
	public void _modifyGrammar(Symbol symbol, Production p) {
		for(int i=0;i<this._rulesProductions.get(symbol).size();i++) {
			if(this._rulesProductions.get(symbol).get(i).equals(p)) {
				this._rulesMerits.get(symbol).set(i, this._rulesMerits.get(symbol).get(i)+1);
				return;
			}
		}
		this._rulesProductions.get(symbol).add(p);
		this._rulesMerits.get(symbol).add(1);
		
		this.calculateAttributes();
	}
	public DerivationTree getDeepestPropagated(DerivationTree t) {
		TreeNode n = t.getDeepest();
	
		while(n != t.getRoot() && n.getParent().getNumberOfChildren()==1) {
			n = n.getParent();
		}
		n.setData(t.getDeepest().getData());
		n.clearChildren();
		if(n == t.getRoot())return new DerivationTree(n);
		else return new DerivationTree(n.getParent());
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Symbol nt : this._rulesProductions.keySet()) {
			sb.append(nt + " -> ");
			List<Production> ps = this._rulesProductions.get(nt);
			for(int i=0;i<ps.size();i++) {
				
				sb.append("<"+this._rulesMerits.get(nt).get(i)+">  ");
				sb.append(ps.get(i));
				sb.append(" | ");
				
				
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
			sb.append('.');
			sb.append('\n');
			
		}
		return sb.toString();
	}
	public static void main(String args[]) {
		BiasedGrammar g = new BiasedGrammar();
		g.parseBNF("defaultBias");
		System.out.println(g);
		//g.setInitialProductionsAttributes();
		
		/*Symbol code = g.new Symbol("CODE",AbstractGrammar.SymbolType.NTerminal);
		Symbol line = g.new Symbol("LINE",AbstractGrammar.SymbolType.NTerminal);
		g._modifyGrammar(code, g.new Production(code,line));
		System.out.println(g);
		System.out.println(g.getProduction(86, code));
		*/
		//Grammar g = new Grammar("s");
		//System.out.println(g);
		for(int i=0;i<0;i++) {
			Chromosome c = new Chromosome(50);
			

			//c.setArrayIntToCodon(200,200,0,  200,0,0,0,0,0,0, 200,200,0,0,70,0,70,  200,0,0,0,150,0,150, 200,200,0,0,200,0,200,  0,0);
			//                           //if             //else           
			
			
			LinkedList<Symbol> l = g.parse(c);
			if(l==null) {
				System.out.println("vvvvad");
			}
			else {
				l.forEach(e->System.out.print(e));
				Phenotype pt = new Phenotype();
				System.out.println("\nvisual: ");
				System.out.println(pt.getVisualCode());
				System.out.println("-----------------");
			}
			
		}
		
		
		
	}

}
