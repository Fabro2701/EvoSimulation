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
		x=0;
		y=0;
		img = new ImageIcon("resources/entities/entity1.png").getImage();
		//System.out.println(Builder.type);
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
	public Image getImage() {
		return img;
		/*BufferedImage img = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = img.createGraphics();
		gr.setColor(Color.BLUE);
		gr.fillOval(0, 0, 20, 20);*/
		//Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 20, 20);
		//return circle;
	}
	public static class Builder extends Entity.Builder{
		public String type="sr";
		@Override
		public Entity createTheInstance(JSONObject o, Controller ctrl) {
			return new SimpleRandomEntity(o.getString("id"),
										  ctrl.getNodeAt(o.getInt("x"),o.getInt("y"))
										  );
		}
		
	}
}
