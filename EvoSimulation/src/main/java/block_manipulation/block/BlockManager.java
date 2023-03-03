package block_manipulation.block;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.parsing.BlockParser;
import block_manipulation.Vector2D;
import simulator.Constants;
import simulator.RandomSingleton;


public class BlockManager{
	private List<Integer>decisions;
	private int cursor;
	
	private Block root;
	private Vector2D base;

	private List<DrawElement.Shape>shapes;
	private HashMap<String, JSONArray>blockDescs;
	private HashMap<String, Color>blockColors;
	private HashMap<String, Boolean>blockIluminations;
	
	private Graphics2D g2;

	public BlockManager(Vector2D base) {
		decisions = new ArrayList<Integer>(Constants.CHROMOSOME_LENGTH);
		cursor = 0;
		
		int alpha = 100;
		blockColors = new HashMap<String, Color>();
		blockColors.put("CODE", new Color(200,0,0,alpha));
		blockColors.put("LINE", new Color(0,200,0,alpha));
		blockColors.put("IF", new Color(0,0,200,alpha));
		blockColors.put("COND", new Color(200,200,0,alpha));
		blockColors.put("OBS", new Color(200,0,200,alpha));
		blockColors.put("ACTION", new Color(0,200,200,alpha));
		blockColors.put("OP", new Color(150,150,150,alpha));
		
		this.base = base;
		BlockParser parser = new BlockParser();
		JSONObject program = parser.parseFile("test");

		blockDescs = BlockCreator.loadBlocks(program);
		
		blockIluminations = new HashMap<>();
	}
	public void move(Point current, MouseEvent e) {
		root.base.x += e.getPoint().x-current.x;
		root.base.y += e.getPoint().y-current.y;
	}
	public boolean flip(MouseEvent e) {
		Block block = root.findRecursive(e.getPoint());
		if(block != null && block instanceof RecursiveBlock) {
			int position = ((RecursiveBlock)block).getPosition();
			decisions.set(position, decisions.get(position)+1);
			return true;
		}
		return false;
	}

	public void iluminateRecursiveBlocks(String rule) {
		blockIluminations.put(rule, true);
	}
	public void paint(Graphics2D g) {
		g2 = g;
		shapes = new ArrayList<DrawElement.Shape>();
		cursor=0;
		root.setBase(base);
		root.paint(shapes);
		shapes.sort(new Comparator<Shape>(){
			@Override
			public int compare(Shape o1, Shape o2) {
				return o1.priority<o2.priority?-1:o1.priority>o2.priority?1:0;
			}
		});
		shapes.stream().forEach(s->s.draw(g2));
		this.blockIluminations.clear();
	}
	public void merge(BlockManager manager2, int pos) {
		//preserve pending

		decisions.addAll(pos, manager2.decisions);
	}
	public int getNext() {
		return decisions.get(cursor++);
	}

	public void setRoot(Block root) {
		this.root = root;
	}
	public int getCursor() {
		return cursor;
	}
	public List<Integer> getDecisions(){
		return this.decisions;
	}
	public Color getBlockColor(String id) {
		return this.blockColors.get(id);
	}
	public JSONArray getBlockDescription(String id) {
		return this.blockDescs.get(id);
	}
	public Graphics2D getGraphics() {
		return this.g2;
	}
	public Block getRoot() {
		return root;
	}
	public boolean shouldBlockBeIluminated(String id) {
		return this.blockIluminations.containsKey(id)&&this.blockIluminations.get(id);
	}
	public void clearIluminations() {
		this.blockIluminations.clear();
	}
}
