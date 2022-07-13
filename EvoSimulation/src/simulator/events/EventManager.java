package simulator.events;

import java.util.Comparator;
import java.util.PriorityQueue;

import simulator.control.Controller;

public class EventManager {
	private PriorityQueue<Event>events;

	public EventManager() {
		events = new PriorityQueue<Event>(new EventComparator());
	}
	public void addEvent(Event e) {
		events.add(e);
	}
	public void update(Controller ctrl, int time) {
		
		while(events.size()!=0&&events.peek().executionTime<=time) {
			events.poll().execute(ctrl);;
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
}
