package simulator.factories.builders.stats;

import static simulator.Constants.ChildDepthStatsBuilder_TYPE;

import org.json.JSONObject;

import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.ChildDepthStats;

public class ChildDepthBuilder extends StatsBuilder{
	public ChildDepthBuilder() {
		this.type = ChildDepthStatsBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new ChildDepthStats(o.getInt("updateRate"));
	}

}
