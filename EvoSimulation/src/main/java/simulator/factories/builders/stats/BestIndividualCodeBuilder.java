package simulator.factories.builders.stats;

import static simulator.Constants.BestIndividualCodeBuilder_TYPE;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.BestIndividualCodeStats;

public class BestIndividualCodeBuilder extends StatsBuilder{
	public BestIndividualCodeBuilder() {
		this.type = BestIndividualCodeBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new BestIndividualCodeStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
				  						   o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE)
				 						  );
	}

}
