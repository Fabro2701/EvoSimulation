package simulator.factories.builders.stats;

import static simulator.Constants.EnergyBuilder_TYPE;

import org.json.JSONObject;

import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.EnergyStats;

public class EnergyBuilder extends StatsBuilder{
	public EnergyBuilder() {
		this.type = EnergyBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new EnergyStats(o.getInt("updateRate"));
	}

}
