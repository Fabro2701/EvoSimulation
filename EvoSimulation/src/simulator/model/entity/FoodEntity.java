package simulator.model.entity;

import javax.swing.ImageIcon;

import simulator.model.map.Node;

public class FoodEntity extends PasiveEntity {
	protected int foodAmount = 10;

	public FoodEntity(String id, Node n) {
		super(id, n);
		type = "f";
		img = new ImageIcon("resources/entities/food.png").getImage();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void recieveActiveEntityInteraction(Entity e) {
		e.getFood(this);
		this.vanish();
	}

	public int getFoodAmount() {
		return this.foodAmount;
	}

}
