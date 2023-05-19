package simulator.events;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.model.evaluation.EvaluationException;

/**
 * MultipleTimeEvent is a {@link Event} that executes its action multiple times
 * @author fabrizioortega
 *
 */
public abstract class MultipleTimeEvent extends Event{
	protected int times;
	protected int interval;
	
	/**
	 * MultipleTimeEvent constructor
	 * @param executionTime
	 * @param times
	 * @param interval
	 */
	public MultipleTimeEvent(int executionTime, int times, int interval) {
		super(executionTime);
		this.times = times;
		this.interval = interval;
	}

	
	/**
	 * executes _execute() {@link MultipleTimeEvent#times} times
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	@Override
	public void execute(Controller ctrl) throws JSONException, EvaluationException {
		if(times>0) {
			this._execute(ctrl);
			executionTime = executionTime+interval;
			times--;
		}
		else executionTime=-1;
	}

}
