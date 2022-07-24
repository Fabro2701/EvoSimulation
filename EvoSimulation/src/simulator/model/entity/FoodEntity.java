package simulator.model.entity;

import javax.swing.ImageIcon;

import simulator.Constants;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public class FoodEntity extends PasiveEntity {
	protected int foodAmount = 10;

	public FoodEntity(String id, Node n) {
		super(id, n);
		type = "f";
		img = new ImageIcon("resources/entities/food.png").getImage();
	}

	@Override
	public void recieveActiveEntityInteraction(Entity e) {
		e.getFood(this);
		this.vanish();
	}

	public int getFoodAmount() {
		return this.foodAmount;
	}
	@Override
	public void update(EvoSimulator simu) {
		super.update(simu);
		if(age>=Constants.FOOD_LIVE_TIME)vanish();
	}

}
