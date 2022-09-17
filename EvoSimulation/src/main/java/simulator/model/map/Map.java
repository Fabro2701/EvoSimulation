package simulator.model.map;

import static simulator.Constants.jsonView;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;

import simulator.Constants.MOVE;
import simulator.Constants.NODE_TYPE;
import simulator.RandomSingleton;
import util.Pair;;

public class Map {
	private Node[][] nodes;
	private List<Node> landNodes;
	private BufferedImage attributesImg;
	private BufferedImage elevationImg;
	private BufferedImage terrainImg;
	private String fileName;
	public int HEIGHT, WIDTH;

	public Map(String fileName) {
		this.fileName = fileName;
		try {
			this.attributesImg = ImageIO.read(new File("resources/maps/" + fileName + "/attributes.png"));
			this.elevationImg = ImageIO.read(new File("resources/maps/" + fileName + "/elevation.png"));
			this.terrainImg = ImageIO.read(new File("resources/maps/" + fileName + "/terrain.png"));

			if (attributesImg.getHeight() != elevationImg.getHeight() || attributesImg.getWidth() != elevationImg.getWidth())
				System.err.println("Map loading: Images dimensions are not equal");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.HEIGHT = attributesImg.getHeight();
		this.WIDTH = attributesImg.getWidth();
		this.nodes = new Node[HEIGHT][WIDTH];
		this.landNodes = new ArrayList<Node>();
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				Color color = new Color(attributesImg.getRGB(x, y), true);
				nodes[y][x] = new Node(x, y, 
									   color.getRed()/255f, 
									   color.getGreen()/255f, 
									   new Color(this.elevationImg.getRGB(x, y)).getGreen()/255f,
									   new Color(this.terrainImg.getRGB(x, y)).getRed()>0?NODE_TYPE.LAND:NODE_TYPE.VOID);
				
				if(nodes[y][x].type == NODE_TYPE.LAND)landNodes.add(nodes[y][x]);
			}
		}
		applyTerrainMask();
	}
	private void applyTerrainMask() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if(nodes[y][x].type == NODE_TYPE.VOID) {
					//elevationImg.setRGB(x, y, this.elevationImg.getRGB(x, y)&0);
					attributesImg.setRGB(x, y, this.elevationImg.getRGB(x, y)&0);
				}//8388607 2147483647
			}
		}
		
	}
	/**
	 * Returns the resulting position of the node + move %.
	 * The borderlands are connected
	 * @param node
	 * @param move
	 * @return
	 */
	public Node getValidModuloMove(Node node, MOVE move) {
		Pair<Integer, Integer> change = move.getPosChange();
		Pair<Integer, Integer> newPos = new Pair<Integer, Integer>(node.x + change.first,
				node.y + change.second);
	
		if((newPos.first<0&&move == MOVE.LEFT) || (newPos.second<0&&move == MOVE.UP) || (newPos.first>=this.WIDTH&&move == MOVE.RIGHT) || (newPos.second>=this.HEIGHT&&move == MOVE.DOWN)) {
			newPos.first = newPos.first<0?(this.WIDTH)+newPos.first:newPos.first;
			newPos.second = newPos.second<0?(this.HEIGHT)+newPos.second:newPos.second;
			newPos.first %= this.WIDTH;
			newPos.second %= this.HEIGHT;
			if(this.nodes[newPos.second][newPos.first].type == NODE_TYPE.LAND) {
				return this.nodes[newPos.second][newPos.first];
			}
			return getValidModuloMove(this.nodes[newPos.second][newPos.first], move);
		}
		else {
			if(node.type == NODE_TYPE.VOID) {
				newPos.first = newPos.first<0?(this.WIDTH)+newPos.first:newPos.first;
				newPos.second = newPos.second<0?(this.HEIGHT)+newPos.second:newPos.second;
				newPos.first %= this.WIDTH;
				newPos.second %= this.HEIGHT;
				//System.out.println(node+" "+move);
				//return getValidModuloMove(this.nodes[newPos.second][newPos.first], move);
				return searchNextNonVoid(this.nodes[newPos.second][newPos.first], move);
			}
			else {
				
				return getValidWallRestrictedMove(node, move);
			}
		}		
	}
	private Node searchNextNonVoid(Node node, MOVE move) {
		int x= node.x,y=node.y;
		int cont=0;
		while(this.nodes[y][x].type == NODE_TYPE.VOID) {
			Pair<Integer, Integer> change = move.getPosChange();
			Pair<Integer, Integer> newPos = new Pair<Integer, Integer>(node.x + change.first,
					node.y + change.second);
			newPos.first = newPos.first<0?(this.WIDTH)+newPos.first:newPos.first;
			newPos.second = newPos.second<0?(this.HEIGHT)+newPos.second:newPos.second;
			newPos.first %= this.WIDTH;
			newPos.second %= this.HEIGHT;
			
			x = newPos.first;
			y = newPos.second;
			cont++;
			if(cont>=Math.max(this.HEIGHT, this.WIDTH)) {
				System.err.println("node loop: "+node+"  "+move);
				return this.getRandomNode();
			}
		}
		return this.nodes[y][x];
	}
//	node loop: {"elevation":0.49803922,"x":1,"temperature":0.67058825,"y":562,"radiaton":0.29411766,"type":"VOID"}  RIGHT
//	node loop: {"elevation":0.49803922,"x":1,"temperature":0.67058825,"y":571,"radiaton":0.27450982,"type":"VOID"}  RIGHT
//	-------- Simulation velocity-1: 2187
//	node loop: {"elevation":0.49803922,"x":998,"temperature":0,"y":463,"radiaton":0,"type":"VOID"}  LEFT
//	-------- Simulation velocity-1: 2143
//	-------- Simulation velocity-1: 1818
//	max: 599
//	max: 4
//	node loop: {"elevation":0.49803922,"x":513,"temperature":0,"y":1,"radiaton":0,"type":"VOID"}  DOWN
//	-------- Simulation velocity-1: 2040
//	node loop: {"elevation":0.49803922,"x":511,"temperature":0,"y":1,"radiaton":0,"type":"VOID"}  DOWN
//	node loop: {"elevation":0.49803922,"x":998,"temperature":0,"y":467,"radiaton":0,"type":"VOID"}  LEFT
//	
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
		if(this.nodes[newPos.second][newPos.first].type == NODE_TYPE.VOID) {
			return node;
		}
		return this.nodes[newPos.second][newPos.first];
	}
	/**
	 * Returns the resulting position of the node + move 
	 * @param node
	 * @param move
	 * @return
	 */
	public Node getValidMove(Node node, MOVE move) {
		if(move == MOVE.NEUTRAL)return node;
		return getValidWallRestrictedMove(node,move);
		//return getValidModuloMove(node,move);
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

	/*
	 * public static void main(String args[]){ Map map = new Map("test1");
	 * System.out.println(map); }
	 */
	public Node getNodeAt(int x, int y) {
		// TODO Auto-generated method stub
		return nodes[y][x];
	}
	public Node getRandomNode() {
		//return nodes[RandomSingleton.nextInt(HEIGHT)][RandomSingleton.nextInt(WIDTH)];
		return this.landNodes.get(RandomSingleton.nextInt(landNodes.size()));
	}
	public String getFileName() {
		return fileName;
	}
	public BufferedImage getAttributesImage() {
		return attributesImg;
	}

	public BufferedImage getElevationImage() {
		return elevationImg;
	}
}
