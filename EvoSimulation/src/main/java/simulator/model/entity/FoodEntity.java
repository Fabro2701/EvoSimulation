package simulator.model.entity;

import javax.swing.ImageIcon;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;

public class FoodEntity extends PasiveEntity {
	protected float foodAmount;
	int lifeTime;

	public FoodEntity(String id, Node n, Controller ctrl) {
		this(id, n, 10.f, Constants.FOOD_LIVE_TIME, ctrl);
	}
	public FoodEntity(String id, Node n, float foodAmount, int lifeTime, Controller ctrl) {
		super(id, n, ctrl);
		type = "f";
		img = new ImageIcon("resources/entities/food.png").getImage();
		this.foodAmount = foodAmount;
		this.lifeTime = lifeTime;
	}

	@Override
	public void recieveActiveEntityInteraction(Entity e) {
		e.getFood(this);
		this.vanish();
	}

	public float getFoodAmount() {
		return this.foodAmount;
	}
	@Override
	public void update(EvoSimulator simu) {
		super.update(simu);
		if(age>=lifeTime)vanish();
	}

}
