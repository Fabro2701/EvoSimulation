package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.AttributeStats;
import statistics.models.NumericAttributeStats;


public class NumericAttributeBuilder extends StatsBuilder{
	public NumericAttributeBuilder() {
		this.type = Constants.NumericAttributeBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		return new NumericAttributeStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
								  o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE),
								  o.getString("attribute")
								 );
	}

}
