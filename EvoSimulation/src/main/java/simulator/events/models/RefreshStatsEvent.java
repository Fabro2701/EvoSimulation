package simulator.events.models;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import statistics.StatsManager;

public class RefreshStatsEvent extends MultipleTimeEvent{
	public RefreshStatsEvent(int executionTime, int times, int interval) {
		super(executionTime, times, interval);
	}

	@Override
	protected void _execute(Controller ctrl) {
		StatsManager stats = ctrl.getStatsManager();
		stats.getModels().stream().forEach(m->m.clear());
	}
}
