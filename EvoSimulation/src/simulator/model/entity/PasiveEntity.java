package simulator.model.entity;

import java.util.HashMap;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public abstract class PasiveEntity extends Entity {

	public PasiveEntity(String id, Node n) {
		super(id, n);
		active=false;
		pheromone = new Pheromone();
		pheromone.init(this);
	}

	@Override
	public MOVE getTheMove(HashMap<String,String>observations) {
		return MOVE.NEUTRAL;
	}

	@Override
	protected void getFood(FoodEntity foodEntity) {
		// pasive entities dont eat

	}

	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
	}
	@Override
	public boolean shouldInteract() {return false;}

}
