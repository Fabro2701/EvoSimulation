package simulator.model.entity;

import javax.swing.ImageIcon;

import simulator.RandomSingleton;
import simulator.Constants.MOVE;
import simulator.model.map.Node;

public class SimpleUPEntity extends SimpleRandomEntity{

	public SimpleUPEntity(String id, Node node) {
		super(id, node);
		type="su";
		img = new ImageIcon("resources/entities/entity2.png").getImage();
		// TODO Auto-generated constructor stub
	}
	@Override
	public MOVE getMove() {
		return MOVE.UP;
	}

}
