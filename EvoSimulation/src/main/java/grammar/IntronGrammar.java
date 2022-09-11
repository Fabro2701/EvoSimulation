package grammar;

import java.util.LinkedList;
import java.util.List;

import simulator.model.entity.individuals.Chromosome;
import simulator.model.entity.individuals.Phenotype;

public class IntronGrammar extends Grammar{
	public LinkedList<Symbol> parse(Chromosome c){
		Symbol t = this.getInitial();
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
			ps = this.getRule(t);
			int m = ps.size();
			//int r = Util.toInt(codons.get(i).bits.get(0, Util.log2(m)));
			
			int r = c.getCodon(i).getIntValue() % m;
			c.setModToCodon(i, r);
			List<Symbol>l = ps.get(r);
			if(l.size()==1&&l.get(0).equals("intron")) {
				i++;
				i %= c.getLength();
				continue;
			}
			q.addAll(0, ps.get(r));
			
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
			i %= c.getLength();
			if(calls>=limit)return null;
		}
		c.setUsedCodons(calls);
		//terminals.add(g.new Symbol(")",Grammar.SymbolType.Terminal));

		
		return terminals;
	}
	
	public static void main(String args[]) {
		IntronGrammar g = new IntronGrammar();
		g.parseBNF("defaultIntron");
		System.out.println(g);
		
		
		//Grammar g = new Grammar("s");
		//System.out.println(g);
		for(int i=0;i<1;i++) {
			Chromosome c = new Chromosome(50);
			c.setArrayIntToCodon(1,0, 1,1,1,1,0,0, 1,1,1,1,1,0,0);
			
			
			LinkedList<Symbol> l = g.parse(c);
			l.forEach(e->System.out.print(e+" "));
			Phenotype pt = new Phenotype(l);
			System.out.println("\nvisual: ");
			System.out.println(pt.getVisualCode());
			System.out.println("-----------------");
		}
		
		
		
	}
}
