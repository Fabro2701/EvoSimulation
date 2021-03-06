package simulator.model.entity;

import java.util.HashMap;

import javax.swing.ImageIcon;

import simulator.Constants.MOVE;
import simulator.RandomSingleton;
import simulator.model.map.Node;

public class SimpleRandomEntity extends ActiveEntity {

	public SimpleRandomEntity(String id, Node node) {
		super(id, node);
		type = "sr";
		img = new ImageIcon("resources/entities/entity1.png").getImage();

	}

	@Override
	public MOVE getTheMove(HashMap<String,String>observations) {
		return MOVE.values()[RandomSingleton.nextInt(MOVE.values().length)];
	}
	/*
	 * @Override public JSONObject getJSON() { return
	 * super.getJSON().getJSONObject("data").put("image", "entity1");//la img ya la
	 * conoce la clase? }
	 */

}
