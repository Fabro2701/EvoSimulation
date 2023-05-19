package simulator.events;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.model.evaluation.EvaluationException;

/**
 * EventManager manages a set of {@link Event} ordered by its {@link Event#executionTime}
 * @author fabrizioortega
 *
 */
public class EventManager {
	private PriorityQueue<Event>events;

	public EventManager() {
		events = new PriorityQueue<Event>(new EventComparator());
	}
	/**
	 * Add event 
	 * @param e event
	 */
	public void addEvent(Event e) {
		events.add(e);
	}
	/**
	 * Execute the events with event.executionTime <= time
	 * @param ctrl
	 * @param time
	 * @throws EvaluationException 
	 * @throws JSONException 
	 */
	public void update(Controller ctrl, int time) throws JSONException, EvaluationException {
		while(events.size()!=0 && events.peek().executionTime<=time) {
			Event e = events.poll();
			e.execute(ctrl);
			if(e.executionTime!=-1) {
				addEvent(e);
			}
		}
	}
	
	class EventComparator implements Comparator<Event>{
		@Override
		public int compare(Event e1, Event e2) {
			if (e1.executionTime > e2.executionTime)
                return 1;
            else if (e1.executionTime < e2.executionTime)
                return -1;
            return 0;
		}
	}

	public void reset() {
		events = new PriorityQueue<Event>(new EventComparator());
	}
}
