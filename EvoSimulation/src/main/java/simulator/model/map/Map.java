package simulator.model.map;

import static simulator.Constants.jsonView;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.json.JSONArray;

import simulator.RandomSingleton;
import simulator.Constants.MOVE;
import util.Pair;;

public class Map {
	private Node[][] nodes;
	private BufferedImage attributesImg;
	private BufferedImage elevationImg;
	public int HEIGHT, WIDTH;

	public Map(String fileName) {
		try {
			this.attributesImg = ImageIO.read(new File("resources/maps/" + fileName + "/attributes.png"));
			this.elevationImg = ImageIO.read(new File("resources/maps/" + fileName + "/elevation.png"));

			if (attributesImg.getHeight() != elevationImg.getHeight())
				System.out.println("Images dimensions are not equal");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.HEIGHT = attributesImg.getHeight();
		this.WIDTH = attributesImg.getWidth();
		this.nodes = new Node[HEIGHT][WIDTH];

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Color color = new Color(attributesImg.getRGB(x, y), true);
				nodes[y][x] = new Node(x, y, color.getRed()/255, color.getGreen()/255);
			}
		}

	}
	/**
	 * Returns the resulting position of the node + move % 
	 * the borderlands are connected
	 * @param node
	 * @param move
	 * @return
	 */
	public Node getValidModuloMove(Node node, MOVE move) {
		Pair<Integer, Integer> change = move.getPosChange();
		Pair<Integer, Integer> newPos = new Pair<Integer, Integer>(node.x + change.first,
				node.y + change.second);
	
		
		newPos.first = newPos.first<0?(this.WIDTH)+newPos.first:newPos.first;
		newPos.second = newPos.second<0?(this.HEIGHT)+newPos.second:newPos.second;
		newPos.first %= this.WIDTH;
		newPos.second %= this.HEIGHT;
		return this.nodes[newPos.second][newPos.first];
	}
	/**
	 * Returns the resulting position of the node + move 
	 * the borderlands cannot be passed
	 * @param node
	 * @param move
	 * @return
	 */
	public Node getValidWallRestrictedMove(Node node, MOVE move) {
		Pair<Integer, Integer> change = move.getPosChange();
		Pair<Integer, Integer> newPos = new Pair<Integer, Integer>(node.x + change.first,
				node.y + change.second);
	
		
		newPos.first = newPos.first<0?0:newPos.first;
		newPos.second = newPos.second<0?0:newPos.second;
		newPos.first = newPos.first>=this.WIDTH?this.WIDTH-1:newPos.first;
		newPos.second = newPos.second>=this.HEIGHT?this.HEIGHT-1:newPos.second;
		return this.nodes[newPos.second][newPos.first];
	}
	public Node getValidMove(Node node, MOVE move) {
		//return getValidWallRestrictedMove(node,move);
		return getValidModuloMove(node,move);
	}

	public JSONArray toJSON() {
		JSONArray arr = new JSONArray();
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				arr.put(nodes[i][j].toJSON());
			}
		}
		return arr;
	}

	@Override
	public String toString() {
		return this.toJSON().toString(jsonView);
	}

	/*
	 * @Override public String toString() { StringBuilder builder = new
	 * StringBuilder(); for(int i=0;i<HEIGHT;i++) { for(int j=0;j<WIDTH;j++) {
	 * builder.append(nodes[i][j]); } builder.append("\n"); } return
	 * builder.toString(); }
	 */
	public BufferedImage getAttributesImage() {
		return attributesImg;
	}

	public BufferedImage getElevationImage() {
		return elevationImg;
	}

	/*
	 * public static void main(String args[]){ Map map = new Map("test1");
	 * System.out.println(map); }
	 */
	public Node getNodeAt(int x, int y) {
		// TODO Auto-generated method stub
		return nodes[y][x];
	}
	public Node getRandomNode() {
		// TODO Auto-generated method stub
		return nodes[RandomSingleton.nextInt(HEIGHT)][RandomSingleton.nextInt(WIDTH)];
	}
}
