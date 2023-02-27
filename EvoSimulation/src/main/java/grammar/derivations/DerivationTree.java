package grammar.derivations;

import java.util.LinkedList;

import grammar.AbstractGrammar;
import grammar.AbstractGrammar.Production;
import grammar.AbstractGrammar.Symbol;
import simulator.model.entity.individuals.genome.Chromosome;
import grammar.BiasedGrammar;

public class DerivationTree {
	private TreeNode _current, _root , _deepest;
	private int _nodeCount;
	private BiasedGrammar _grammar;
	public DerivationTree(BiasedGrammar grammar) {
		this._nodeCount = 0;
		this._grammar = grammar;
	}
	public DerivationTree(TreeNode copy) {
		_root = new TreeNode(copy);
		_current = _root;
	}
	protected void _addNode(TreeNode node) {
		if(_root == null) {
			_root = node;
			_deepest = _root;
			_current = _root;
		}
		else {
			_current.addChild(node);
			if(node._depth>_deepest._depth) _deepest = node;
		}
		_nodeCount++;
	}
	public boolean buildFromChromosome(Chromosome c) {
		_addNode(new TreeNode(_grammar.getInitial()));
		LinkedList<TreeNode>pending = new LinkedList<TreeNode>();
		pending.add(_current);
		int i = 0;
		int limit = 400;
		while(!pending.isEmpty()) {
			_current = pending.pollFirst();
			
			if(_current._data.getType().equals(AbstractGrammar.SymbolType.NTerminal)) {
				_expandAndPushNode(c.getCodon(i%c.getLength()).getIntValue(),pending);
				i++;
			}
			if(i>=limit) return false;
		}
		return true;
	}
	private void _expandAndPushNode(int codonValue, LinkedList<TreeNode> pending) {
		Production ps = _grammar.getProduction(codonValue, _current._data);
		for(Symbol s:ps) {
			TreeNode n = new TreeNode(s);
			_addNode(n);
			pending.add(n);
		}
	}
	public TreeNode getDeepest() {
		return this._deepest;
	}
	public TreeNode getRoot() {
		return this._root;
	}
	@Override
	public String toString() {
		/*StringBuilder sb = new StringBuilder();
		
		return sb.toString();*/
		return this._root.toString();
	}
	public static void main(String args[]) {
		/*BiasedGrammar g = new BiasedGrammar();
		g.parseBNF("defaultBias");
		DerivationTree t = new DerivationTree(g);
		
		
		for(int i=0;i<10;i++) {
			Chromosome c = new Chromosome(50);
			//c.setArrayIntToCodon(1,0,0,0,0,0,0,0);
			
			boolean b = t.buildFromChromosome(c);
			if(b) {
				System.out.println(t.toString());
				System.out.println(t._deepest._data.toString()+" "+t._deepest._depth);
				System.out.println(t._nodeCount);
				System.out.println(g.getDeepestPropagated(t));
			}
			else {
				System.out.println("bad");
			}
		}*/
		
	}
}
