package simulator.events;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.model.evaluation.EvaluationException;

/**
 * OneTimeEvent is a {@link Event} that executes its action once
 * @author fabrizioortega
 *
 */
public abstract class OneTimeEvent extends Event{
	
	/**
	 * OneTimeEvent constructor
	 * @param executionTime
	 */
	public OneTimeEvent(int executionTime) {
		super(executionTime);
	}

	/**
	 * executes _execute() once
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	@Override
	public void execute(Controller ctrl) throws JSONException, EvaluationException {
		this._execute(ctrl);
		executionTime = -1;
	}
}
