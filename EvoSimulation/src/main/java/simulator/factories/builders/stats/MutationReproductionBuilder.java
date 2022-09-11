package simulator.factories.builders.stats;

import static simulator.Constants.MutationReproductionBuilder_TYPE;

import org.json.JSONObject;

import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.MutationReproductionStats;

public class MutationReproductionBuilder extends StatsBuilder{
	public MutationReproductionBuilder() {
		this.type = MutationReproductionBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new MutationReproductionStats(o.getInt("updateRate"));
	}

}
