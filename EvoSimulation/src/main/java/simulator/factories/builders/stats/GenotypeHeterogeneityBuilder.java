package simulator.factories.builders.stats;

import static simulator.Constants.GenotypeHeterogeneityBuilder_TYPE;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.GenotypeHeterogeneityStats;

public class GenotypeHeterogeneityBuilder extends StatsBuilder{
	public GenotypeHeterogeneityBuilder() {
		this.type = GenotypeHeterogeneityBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new GenotypeHeterogeneityStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
				  							  o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE)
				  							 );
	}

}
