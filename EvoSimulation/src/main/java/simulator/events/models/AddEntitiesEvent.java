package simulator.events.models;

import org.json.JSONArray;

import simulator.control.Controller;
import simulator.events.OneTimeEvent;
import simulator.factories.builders.events.EventBuilder;

/**
 * AddEntitiesEvent delegates the creation of {@link AddEntitiesEvent#entitiesToAdd} to {@link Controller} 
 * @author fabrizioortega
 *
 */
public class AddEntitiesEvent extends OneTimeEvent{
	private JSONArray entitiesToAdd;
	
	/**
	 * AddEntitiesEvent's constructor
	 * @param executionTime
	 * @param entities
	 */
	public AddEntitiesEvent(int executionTime, JSONArray entities) {
		super(executionTime);
		entitiesToAdd = entities;
	}

	/**
	 * {@link Controller} loads entitiesToAdd calling its set of {@link EventBuilder}
	 */
	@Override
	protected void _execute(Controller ctrl) {
		ctrl.loadEntities(entitiesToAdd);
	}

}
