package simulator.events;

import simulator.control.Controller;

public abstract class OneTimeEvent extends Event{
	
	public OneTimeEvent(int executionTime) {
		super(executionTime);
	}

	public abstract void execute(Controller ctrl);
}
