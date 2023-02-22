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
		// TODO Auto-generated method stub
		return new AttributeStats(o.getInt("updateRate"), o.getString("attribute"), o.getBoolean("group"));
	}

}
