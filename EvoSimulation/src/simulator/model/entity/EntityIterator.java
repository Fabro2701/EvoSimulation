package simulator.model.entity;

import java.util.Iterator;
import java.util.List;

public class EntityIterator implements Iterator<Entity> {
	Iterator<Entity> current1;
	Iterator<Entity>current2;
	public EntityIterator(List<Entity>l) {
		current1 = l.iterator();
	}
	public EntityIterator(List<Entity>l1,List<Entity>l2) {
		current1 = l1.iterator();
		current2 = l2.iterator();
	}
	@Override
	public boolean hasNext() {
		if(current1.hasNext())return true;
		else {
			if(current2==null)return false;
			else return current2.hasNext();
		}
	}

	@Override
	public Entity next() {
		if(current1.hasNext())return current1.next();
		else return current2.next();
	}
	
	

}
