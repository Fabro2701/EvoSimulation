package simulator.events;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.model.evaluation.EvaluationException;

/**
 * Event class
 * @author fabrizioortega
 *
 */
public abstract class Event {
	protected String type;
	protected int executionTime;
	
	public Event(int executionTime) {
		this.executionTime = executionTime;
	}
	protected abstract void _execute(Controller ctrl) throws JSONException, EvaluationException;
	public abstract void execute(Controller ctrl) throws JSONException, EvaluationException;
	
}
