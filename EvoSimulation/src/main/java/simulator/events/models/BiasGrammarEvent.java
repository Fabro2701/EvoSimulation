package simulator.events.models;

import java.util.List;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import statistics.StatsManager;

public class BiasGrammarEvent extends MultipleTimeEvent{
	public BiasGrammarEvent(int executionTime, int times, int interval) {
		super(executionTime, times, interval);
	}

	@Override
	protected void _execute(Controller ctrl) {
		for(var entry:ctrl.getGrammarController().getGrammars().entrySet()) {
			var g = entry.getValue();
			if(g instanceof grammar.BiasedGrammar) {
				((grammar.BiasedGrammar)g).globalUpdate(ctrl.getSimulator().getEntities());
			}
		}
		
		
	}
}
