package simulator.events.models;

import java.util.List;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import statistics.StatsManager;

public class RefreshStatsEvent extends MultipleTimeEvent{
	public RefreshStatsEvent(int executionTime, int times, int interval) {
		super(executionTime, times, interval);
	}

	@Override
	protected void _execute(Controller ctrl) {
		List<StatsManager> stats = ctrl.getStatsManagers();
		for(StatsManager sm:stats) sm.getModels().stream().forEach(m->m.clear());
	}
}
