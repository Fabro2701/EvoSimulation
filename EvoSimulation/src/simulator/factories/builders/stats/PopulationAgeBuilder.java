package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.PopulationAgeStats;

import static simulator.Constants.PopulationAgeBuilder_TYPE;

public class PopulationAgeBuilder extends StatsBuilder{
	public PopulationAgeBuilder() {
		this.type = PopulationAgeBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new PopulationAgeStats(o.getInt("updateRate"));
	}

}
