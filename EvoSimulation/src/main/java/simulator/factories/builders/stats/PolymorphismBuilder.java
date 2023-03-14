package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.PolymorphismStats;

public class PolymorphismBuilder extends StatsBuilder{
	public PolymorphismBuilder() {
		this.type = Constants.PolyBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new PolymorphismStats(o.getInt("updateRate"),o.getString("poly"));
	}

}
