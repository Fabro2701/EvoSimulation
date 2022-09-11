package simulator.events;

import simulator.control.Controller;

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
	protected abstract void _execute(Controller ctrl);
	public abstract void execute(Controller ctrl);
	
}
