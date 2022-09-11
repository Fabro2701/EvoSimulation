package simulator.events;

import simulator.control.Controller;

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
	 */
	@Override
	public void execute(Controller ctrl) {
		this._execute(ctrl);
		executionTime = -1;
	}
}
