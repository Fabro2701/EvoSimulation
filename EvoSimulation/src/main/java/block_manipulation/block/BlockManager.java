package block_manipulation.block;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import block_manipulation.BlockInfoSingleton;
import block_manipulation.BlockInfoSingleton.BlockInfoSupplier;
import block_manipulation.Vector2D;
import block_manipulation.block.DrawElement.Shape;


public class BlockManager implements Cloneable{
	String filename;
	private List<Integer>decisions;
	private int cursor;
	
	private Block root;
	private Vector2D base;

	private List<DrawElement.Shape>shapes;

	private HashMap<String, Boolean>blockIluminations;
	BlockInfoSupplier blocksInfo;
	
	private Graphics2D g2;
	

	public BlockManager(String filename) {
		this.filename = filename;
		decisions = new ArrayList<Integer>();
		cursor = 0;
		blocksInfo = BlockInfoSingleton.fromFile(filename);
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
		Objects.requireNonNull(this.base);
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
		decisions.remove(pos);
		decisions.addAll(pos, manager2.decisions);
	}
	@Override
	public Object clone() {
		BlockManager m = new BlockManager(this.filename);
		if(this.base!=null)m.setBase(new Vector2D(this.base));
		((ArrayList)m.decisions).ensureCapacity(this.decisions.size());
		for(int i=0;i<this.decisions.size();i++) {
			m.decisions.add(this.decisions.get(i).intValue());
		}
		return m;
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
	public void setBase(Vector2D base) {
		this.base = base;
	}
	public BlockInfoSupplier getBlocksInfo() {
		return blocksInfo;
	}
}
