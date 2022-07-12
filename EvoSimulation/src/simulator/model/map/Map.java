package simulator.model.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

public class Map {
	private Node[][] nodes;
	private BufferedImage attributesImg;
	private BufferedImage elevationImg;
	public int HEIGHT,WIDTH;
	
	public Map(String attributesFileName, String elevationFileName) {
		try {
			this.attributesImg = ImageIO.read(new File("resources/maps/"+attributesFileName+".jpg"));
			this.elevationImg = ImageIO.read(new File("resources/maps/"+elevationFileName+".jpg"));
			
			if(attributesImg.getHeight()!=elevationImg.getHeight())System.out.println("Images dimensions are not equal");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.HEIGHT = attributesImg.getHeight();
		this.WIDTH = attributesImg.getWidth();
		this.nodes = new Node[HEIGHT][WIDTH];
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
	            Color color = new Color(attributesImg.getRGB(x,y), false);
				nodes[x][y] = new Node(x,y,color.getRed(),color.getGreen(),color.getBlue());
			}
        }
		
	}
	public JSONArray toJSON() {
		JSONArray arr = new JSONArray();
		for(int i=0;i<nodes.length;i++) {
			for(int j=0;j<nodes[i].length;j++) {
				arr.put(nodes[i][j].toJSON());
			}
		}
		return arr;
	}
	
	@Override
	public String toString() {
		return this.toJSON().toString();
	}
	/*@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<HEIGHT;i++) {
			for(int j=0;j<WIDTH;j++) {
				builder.append(nodes[i][j]);
			}
			builder.append("\n");
		}
		return builder.toString();
	}*/
	public BufferedImage getAttributesImage() {return attributesImg;}
	public BufferedImage getElevationImage() {return elevationImg;}
	
	/*public static void main(String args[]){
		Map map = new Map("test1");
		System.out.println(map);
	}*/
	public Node getNodeAt(int x, int y) {
		// TODO Auto-generated method stub
		return nodes[x][y];
	}
}
