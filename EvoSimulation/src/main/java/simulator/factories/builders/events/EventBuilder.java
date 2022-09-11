package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.factories.builders.Builder;

public abstract class EventBuilder extends Builder<Event>{

	@Override
	public abstract Event createTheInstance(JSONObject o, Controller ctrl);

}
