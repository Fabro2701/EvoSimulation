package simulator.events;

import simulator.control.Controller;

public abstract class Event {
	protected String type;
	protected int executionTime;
	
	public Event(int executionTime) {
		this.executionTime = executionTime;
	}
	public abstract void execute(Controller ctrl);
	
}
