package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.builders.Builder;
import statistics.StatsData;

public abstract  class StatsBuilder extends Builder<StatsData>{

	@Override
	public abstract StatsData createTheInstance(JSONObject o, Controller ctrl);

}
