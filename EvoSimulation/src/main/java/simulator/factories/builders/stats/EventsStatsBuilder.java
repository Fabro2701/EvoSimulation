package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.EventsStats;
import statistics.models.GenesStats;

import static simulator.Constants.GenesBuilder_TYPE;

public class EventsStatsBuilder extends StatsBuilder{
	public EventsStatsBuilder() {
		this.type = Constants.EventsStatsBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		return new EventsStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
							   o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE)
							  );
	}

}
