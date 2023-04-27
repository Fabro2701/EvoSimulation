package experiment.models.metro;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.factories.builders.stats.StatsBuilder;
import statistics.StatsData;
import statistics.models.AttributeStats;


public class MetroCongestionBuilder extends StatsBuilder{
	public MetroCongestionBuilder() {
		this.type = this.getClass().getName();
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		return new MetroCongestionStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
								  o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE),
								  o.optInt("clear", -1)
								 );
	}

}