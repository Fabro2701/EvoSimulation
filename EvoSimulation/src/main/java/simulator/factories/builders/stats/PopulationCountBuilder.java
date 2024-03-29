package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.PopulationCountStats;

import static simulator.Constants.PopulationCountBuilder_TYPE;

public class PopulationCountBuilder extends StatsBuilder{
	public PopulationCountBuilder() {
		this.type = PopulationCountBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new PopulationCountStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
				  						o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE)
				  					   );
	}

}
