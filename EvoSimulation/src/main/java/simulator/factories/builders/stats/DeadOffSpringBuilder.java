package simulator.factories.builders.stats;

import static simulator.Constants.DeadOffSpringBuilder_TYPE;

import org.json.JSONObject;

import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.DeadOffSpringStats;

public class DeadOffSpringBuilder extends StatsBuilder{
	public DeadOffSpringBuilder() {
		this.type = DeadOffSpringBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new DeadOffSpringStats(o.getInt("updateRate"));
	}

}
