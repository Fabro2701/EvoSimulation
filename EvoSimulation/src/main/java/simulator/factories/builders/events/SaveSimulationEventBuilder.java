package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.models.SaveSimulationEvent;

public class SaveSimulationEventBuilder extends EventBuilder{
	public SaveSimulationEventBuilder() {
		type = simulator.Constants.SaveSimulatorBuilder_TYPE;
	}
	@Override
	public Event createTheInstance(JSONObject o, Controller ctrl) {
		return new SaveSimulationEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"),o.getString("filename"),o.getBoolean("replacement"));
	}

}
