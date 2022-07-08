package simulator.model.entity;

import java.awt.image.BufferedImage;

import simulator.Constants.MOVE;
import simulator.model.map.Node;

public interface Entity {
	Node node=null;

	public MOVE getMove();
	public BufferedImage getImage();
}
