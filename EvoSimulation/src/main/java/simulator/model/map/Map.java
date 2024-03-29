package simulator.model.map;

import static simulator.Constants.jsonView;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.Constants.MOVE;
import simulator.Constants.NODE_TYPE;
import simulator.RandomSingleton;
import simulator.model.map.creator.EntityPanel.EntityInfo;
import util.Pair;;

/**
 * Map class represents the map where all the simulation events occur
 * <p>
 * The model is given by a {@link Node} Matrix (2d java array)
 * @author Fabrizio Ortega
 *
 */
public class Map {
	private Node[][] nodes;
	private List<Node> landNodes;//nodes with NODE_TYPE.LAND type
	
	private BufferedImage attributesImg;
	private BufferedImage elevationImg;
	private BufferedImage terrainImg;
	public int HEIGHT, WIDTH;
	private String fileName;
	
	private List<EntityInfo>entitiesInfo;//entities to be added

	/**
	 * Creates a map given a directory name
	 * @param fileName name of the folder
	 * @throws IOException 
	 */
	public Map(String fileName) throws IOException, IllegalArgumentException {
		this.fileName = fileName;
		
		this.attributesImg = ImageIO.read(new File(fileName + "/attributes.png"));
		this.elevationImg = ImageIO.read(new File(fileName + "/elevation.png"));
		this.terrainImg = ImageIO.read(new File(fileName + "/terrain.png"));
		
		if (attributesImg.getHeight() != elevationImg.getHeight() || attributesImg.getWidth() != elevationImg.getWidth())
			throw new IllegalArgumentException("Images dimensions are not equal");
		

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
		
		entitiesInfo = new ArrayList<>();
		File entsFile = new File(fileName + "/entities.json");
		if(entsFile.exists()) {
			JSONArray ents = new JSONArray(new JSONTokener(new FileInputStream(entsFile)));
			for(int i=0;i<ents.length();i++) {
				JSONObject o = ents.getJSONObject(i);
				entitiesInfo.add(new EntityInfo(o.getString("type"),o.getInt("x"),o.getInt("y"),o.optString("code", "")));
			}
		}
	}
	private void applyTerrainMask() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if(nodes[y][x].type == NODE_TYPE.VOID) {
					//elevationImg.setRGB(x, y, this.elevationImg.getRGB(x, y)&0);
					attributesImg.setRGB(x, y, this.elevationImg.getRGB(x, y)&0);
				}
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
	public Node getValidNodeModulo(Node node, MOVE move) {
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
			return getValidNodeModulo(this.nodes[newPos.second][newPos.first], move);
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
				
				return getValidNodeWallRestricted(node, move);
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

	/**
	 * Returns the resulting position of the node + move 
	 * the borderlands cannot be passed
	 * @param node
	 * @param move
	 * @return
	 */
	public Node getValidNodeWallRestricted(Node node, MOVE move) {
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
		return getValidNodeWallRestricted(node,move);
		//return getValidModuloMove(node,move);
	}
	public Node getNextNode(int x, int y, Node node) {
		x+=node.x;
		y+=node.y;
		x = x<0?0:x;
		y = y<0?0:y;
		x = x>=this.WIDTH?this.WIDTH-1:x;
		y = y>=this.HEIGHT?this.HEIGHT-1:y;
		return this.nodes[y][x];
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
	public List<EntityInfo> getEntitiesInfo() {
		return entitiesInfo;
	}
}
