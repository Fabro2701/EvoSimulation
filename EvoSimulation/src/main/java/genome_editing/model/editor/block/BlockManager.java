package genome_editing.model.editor.block;

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

import genome_editing.model.editor.block.DrawElement.Shape;
import genome_editing.model.editor.parsing.BlockParser;
import genome_editing.model.elements.Vector2D;
import simulator.Constants;
import simulator.RandomSingleton;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Chromosome.Codon;

public class BlockManager{
	Chromosome<Chromosome.Codon>chChoice;
	Block root;
	Vector2D base;
	List<DrawElement.Shape>shapes;
	static List<Integer>decisions;
	static int cursor=0;
	static HashMap<String, JSONArray>blockDescs;
	static Graphics2D g2;
	static HashMap<String, Color>blockColors;
	
	static {
		decisions = new ArrayList<Integer>(Constants.CHROMOSOME_LENGTH);

		//decisions.stream().forEach(e->System.out.println(e));
		
		blockColors = new HashMap<String, Color>();
		blockColors.put("CODE", new Color(255,0,0,100));
		blockColors.put("LINE", new Color(0,255,0,100));
		blockColors.put("IF", new Color(0,0,255,100));
		blockColors.put("COND", new Color(255,255,0,100));
		blockColors.put("OBS", new Color(255,0,255,100));
		blockColors.put("ACTION", new Color(0,255,255,100));
		blockColors.put("OP", new Color(150,150,150,100));
	}
	
	public BlockManager(Vector2D base) {
		this.base = base;
		BlockParser parser = new BlockParser();
		JSONObject program = parser.parseFile("test");

		blockDescs = BlockCreator.loadBlocks(program);
		
	}
	public void move(Point current, MouseEvent e) {
		((RecursiveBlock)root).move(current, e.getPoint()); 
	}
	public void flip(MouseEvent e) {
		Block block = root.find(e.getPoint());
		if(block != null && block instanceof RecursiveBlock) {
			int position = ((RecursiveBlock)block).getPosition();
			decisions.set(position, decisions.get(position)+1);
			this.chChoice.getCodon(position).setInt(decisions.get(position));
		}
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
	}
	public static int getNext() {
		return decisions.get(cursor++);
	}
	public void setRoot(Block root) {
		this.root = root;
	}
	public static int getCursor() {
		return cursor;
	}
	public void setChChoice(Chromosome<Codon> chChoice) {
		this.chChoice = chChoice;
		decisions.clear();
		for(int i=0;i<Constants.CHROMOSOME_LENGTH;i++) {
			decisions.add(this.chChoice.getCodon(i).getIntValue());
		}

	}
}
