package simulator.events;

import simulator.control.Controller;

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
	 */
	@Override
	public void execute(Controller ctrl) {
		this._execute(ctrl);
		if(times>0) {
			executionTime = executionTime+interval;
			times--;
		}
		else executionTime=-1;
	}

}
