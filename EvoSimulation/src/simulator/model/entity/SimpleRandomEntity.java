package simulator.model.entity;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.control.Controller;
import simulator.model.map.Node;

public class SimpleRandomEntity extends Entity {
	
	
	public SimpleRandomEntity(String id, Node node) {
		super(id, node);
		type="sr";
		img = new ImageIcon("resources/entities/entity1.png").getImage();
		
	}
	
	@Override
	public MOVE getMove() {
		return MOVE.values()[RandomSingleton.nextInt(MOVE.values().length)];
	}
	/*@Override
	public JSONObject getJSON() {
		return super.getJSON().getJSONObject("data").put("image", "entity1");//la img ya la conoce la clase?
	}*/

	@Override
	public void myInteract(Entity e) {
		e.recieveActiveEntityInteraction(this);
	}
	
	@Override
	public void getFood(FoodEntity f) {
		f.getFoodAmount();
	}
}
