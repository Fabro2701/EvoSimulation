package simulator.events;

import simulator.control.Controller;

/**
 * TimeLapseEvent is a {@link Event} that executes its action in a time lapse
 * @author fabrizioortega
 *
 */
public abstract class TimeLapseEvent extends Event{
	protected int times;
	protected int interval;
	protected int timelapse;
	protected float innerInterval;
	protected int cont;
	protected int auxTime;
	
	/**
	 * TimeLapseEvent constructor
	 * @param executionTime
	 * @param times
	 * @param interval
	 * @param timelapse
	 */
	public TimeLapseEvent(int executionTime, int times, int interval, int timelapse) {
		super(executionTime);
		this.times = times;
		this.interval = interval;
		this.timelapse = timelapse;
		this.innerInterval = interval/timelapse;
		this.cont = timelapse;
		this.auxTime = executionTime;
	}

	
	/**
	 * executes _execute() {@link TimeLapseEvent#times} times
	 */
	@Override
	public void execute(Controller ctrl) {
		
		if(times>0) {
			this._execute(ctrl);
			
			cont--;
			if(cont==0) {
				cont = timelapse;
				times--;
				auxTime = auxTime+interval;
				executionTime = auxTime;
			}
			else {
				executionTime = (int) (executionTime+innerInterval);
			}
		}
		else executionTime=-1;
	}

}
