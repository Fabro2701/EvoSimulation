package simulator.model.entity;

import java.awt.Image;

import javax.swing.ImageIcon;

import simulator.Constants.MOVE;
import simulator.RandomSingleton;

public class SimpleRandomEntity extends Entity {
	
	public SimpleRandomEntity() {
		x=0;
		y=0;
		img = new ImageIcon("resources/entities/entity1.png").getImage();
	}
	
	@Override
	public MOVE getMove() {
		return MOVE.values()[RandomSingleton.nextInt(MOVE.values().length)];
	}

	@Override
	public Image getImage() {
		return img;
		/*BufferedImage img = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = img.createGraphics();
		gr.setColor(Color.BLUE);
		gr.fillOval(0, 0, 20, 20);*/
		//Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 20, 20);
		//return circle;
	}

}
