package simulator.factories.builders.stats;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import statistics.StatsData;
import statistics.models.AttributeStats;


public class AttributeBuilder extends StatsBuilder{
	public AttributeBuilder() {
		this.type = Constants.AttributeBuilder_TYPE;
	}
	@Override
	public StatsData createTheInstance(JSONObject o, Controller ctrl) {
		return new AttributeStats(o.optInt("updateRate", Constants.DEFAULT_STATS_REFRESHRATE), 
								  o.optBoolean("serialize", Constants.DEFAULT_STATS_SERIALIZE),
								  o.getString("attribute"), 
								  o.optBoolean("group", false)
								 );
	}

}
