package simulator.model.entity.individuals;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.entity.ActiveEntity;
import simulator.model.map.Node;

public abstract class AbstractIndividual extends ActiveEntity{

	public AbstractIndividual(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		
	}

}
