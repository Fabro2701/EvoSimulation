package simulator.model.entity;

import java.util.HashMap;

import javax.swing.ImageIcon;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.map.Node;

public class SimpleUPEntity extends SimpleRandomEntity {

	public SimpleUPEntity(String id, Node node, Controller ctrl) {
		super(id, node, ctrl);
		type = "su";
		img = new ImageIcon("resources/entities/entity2.png").getImage();
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE getTheMove() {
		return MOVE.UP;
	}

}
