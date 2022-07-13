package util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import simulator.model.entity.Entity;
import static simulator.Constants.INTERACTION_DISTANCE;

public class Util {
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	public static boolean areCloseEnough(Entity e1, Entity e2) {
		return Math.abs(e1.node.x-e2.node.x)+(Math.abs(e1.node.y-e2.node.y))<=INTERACTION_DISTANCE;
	}
}
