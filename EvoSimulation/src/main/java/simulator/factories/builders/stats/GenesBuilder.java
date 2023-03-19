package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.GenesStats;

import static simulator.Constants.GenesBuilder_TYPE;

public class GenesBuilder extends StatsBuilder{
	public GenesBuilder() {
		this.type = GenesBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new GenesStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
				  			  o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE),
				  			  o.optInt("op",1)
				  			 );
	}

}
