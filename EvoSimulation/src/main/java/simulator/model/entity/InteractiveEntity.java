package simulator.model.entity;

import java.util.HashMap;
import java.util.Map;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.control.GrammarController;
import simulator.control.InitController;
import simulator.control.InteractionsController;
import simulator.control.UpdatesController;
import simulator.model.ActionI;
import simulator.model.map.Node;

public abstract class InteractiveEntity extends Entity{

	protected java.util.Map<String, AbstractGrammar>grammars;
	protected java.util.Map<String, java.util.Map<String, ActionI>>actions;
	protected InteractionsController interactions;
	protected UpdatesController updates;
	protected InitController inits;
	
	protected GrammarController grammarController;
	protected Map<String,Boolean>exs;
	
	public InteractiveEntity(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		
		grammars = ctrl.getGrammarController().getGrammars();
		actions = ctrl.getActionsController().getActions();
		interactions = ctrl.getInteractionsController();
		updates = ctrl.getUpdatesController();
		inits = ctrl.getInitController();
		
		grammarController = ctrl.getGrammarController();
		exs = new HashMap<>();
		for(String k:grammars.keySet()) {
			exs.put(k, false);
		}
		
		init();
	}
	protected abstract void init();
	
	

}
