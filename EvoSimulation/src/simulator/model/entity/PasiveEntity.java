package simulator.model.entity;

import simulator.Constants.MOVE;
import simulator.model.map.Node;

public abstract class PasiveEntity extends Entity {

	public PasiveEntity(String id, Node n) {
		super(id, n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE getTheMove() {
		return MOVE.NEUTRAL;
	}

	@Override
	protected void getFood(FoodEntity foodEntity) {
		// pasive entities doesnt eat

	}

	@Override
	public void update() {
		// pending
	}

}
