package simulator.model.entity;

import java.awt.Image;

import javax.swing.ImageIcon;

import simulator.Constants.MOVE;
import simulator.model.map.Node;

public class FoodEntity extends Entity{
	protected int foodAmount = 10;
	public FoodEntity(String id, Node n) {
		super(id, n);
		type = "f";
		img = new ImageIcon("resources/entities/food.png").getImage();
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE getMove() {
		// TODO Auto-generated method stub
		return MOVE.NEUTRAL;
	}
	
	
	
	@Override
	public void recieveActiveEntityInteraction(Entity e) {
		e.getFood(this);
		this.vanish();
	}
	
	public int getFoodAmount() {return this.foodAmount;}

	@Override
	protected void getFood(FoodEntity foodEntity) {
		// TODO Auto-generated method stub
		//
	}
	

}
