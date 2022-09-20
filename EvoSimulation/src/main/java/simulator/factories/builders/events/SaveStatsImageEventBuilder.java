package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.SaveStatsImageEvent;

public class SaveStatsImageEventBuilder extends EventBuilder{
	public SaveStatsImageEventBuilder() {
		type = simulator.Constants.SaveStatsImageBuilder_TYPE;
	}
	@Override
	public Event createTheInstance(JSONObject o, Controller ctrl) {
		return new SaveStatsImageEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("filename"),o.getBoolean("replacement"));
	}

}
