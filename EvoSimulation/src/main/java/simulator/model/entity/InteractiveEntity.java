package simulator.model.entity;

import grammar.AbstractGrammar;
import simulator.control.Controller;
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
	
	public InteractiveEntity(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		
		grammars = ctrl.getGrammarController().getGrammars();
		actions = ctrl.getActionsController().getActions();
		interactions = ctrl.getInteractionsController();
		updates = ctrl.getUpdatesController();
		inits = ctrl.getInitController();
		
		init();
	}
	protected abstract void init();

}
