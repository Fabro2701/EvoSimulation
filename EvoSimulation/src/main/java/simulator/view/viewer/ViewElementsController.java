package simulator.view.viewer;

import java.awt.Graphics2D;
import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.map.Map;

public abstract class ViewElementsController {
	protected java.util.Map<Object,ViewElement>viewElements;
	public ViewElementsController(java.util.Map<Object,ViewElement>viewElements) {
		this.viewElements = viewElements;
	}
	public abstract void produce(List<Entity>entities, Map map);
}
