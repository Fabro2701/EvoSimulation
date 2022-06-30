package map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map {
	private Node[][] nodes;
	private BufferedImage img;
	private int HEIGHT,WIDTH;
	
	public Map(String fileName) {
		try {
			this.img = ImageIO.read(new File("resources/maps/"+fileName+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.HEIGHT = img.getHeight();
		this.WIDTH = img.getWidth();
		this.nodes = new Node[HEIGHT][WIDTH];
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
	            Color color = new Color(img.getRGB(x,y), true);
				nodes[x][y] = new Node(color.getRed(),color.getGreen(),color.getBlue());
			}
        }
		
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<HEIGHT;i++) {
			for(int j=0;j<WIDTH;j++) {
				builder.append(nodes[i][j]);
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	public static void main(String args[]){
		Map map = new Map("test1");
		System.out.println(map);
	}
}
