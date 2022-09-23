package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.RefreshStatsEvent;

public class RefreshStatsEventBuilder extends EventBuilder{
	public RefreshStatsEventBuilder() {
		type = simulator.Constants.RefreshStatsBuilder_TYPE;
	}
	@Override
	public Event createTheInstance(JSONObject o, Controller ctrl) {
		return new RefreshStatsEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"));
	}

}
