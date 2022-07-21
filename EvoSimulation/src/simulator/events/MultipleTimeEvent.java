package simulator.events;

import simulator.control.Controller;

public abstract class MultipleTimeEvent extends Event{
	protected int times;
	protected int interval;
	public MultipleTimeEvent(int executionTime, int times, int interval) {
		super(executionTime);
		this.times = times;
		this.interval = interval;
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void execute(Controller ctrl);

}
