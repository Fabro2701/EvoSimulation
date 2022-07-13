package simulator.events.models;

import org.json.JSONArray;

import simulator.control.Controller;
import simulator.events.OneTimeEvent;

public class AddEntitiesEvent extends OneTimeEvent{
	private JSONArray entitiesToAdd;
	public AddEntitiesEvent(int executionTime, JSONArray o) {
		super(executionTime);
		entitiesToAdd = o;
		type="ae";
	}

	@Override
	public void execute(Controller ctrl) {
		ctrl.loadEntities(entitiesToAdd);
	}

}
