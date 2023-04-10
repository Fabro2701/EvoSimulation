package block_manipulation.block;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	private List<InputIndex>inputsIdx; 

	public BlockManager(String filename) {
		this.filename = filename;
		decisions = new ArrayList<Integer>();
		cursor = 0;
		blocksInfo = BlockInfoSingleton.fromFile(filename);
		blockIluminations = new HashMap<>();
		inputsIdx = new ArrayList<>();
	}
	public void move(Point current, Point e) {
		this.base.x += e.x-current.x;
		this.base.y += e.y-current.y;
	}
	public BlockManager detach(RecursiveBlock block) {
		
		BlockManager m = new BlockManager(this.filename);
		m.setBase(new Vector2D(block.base));
		int des = block.decisionsUsed();
		InputIndex idx = null;
		List<InputIndex>rem = new ArrayList<>();
		for(int i=block.position;i<block.position+des;i++) {
			if((idx=this.getInput(i))!=null) {
				rem.add(idx);
			}
			m.decisions.add(this.decisions.get(i));
		}
		this.inputsIdx.removeAll(rem);
		for(InputIndex ii:this.inputsIdx) {
			if(ii.pos>block.position)ii.pos -= des-1;
		}
		for(InputIndex ii:rem) {
			ii.pos -= block.position;
		}
		m.inputsIdx.addAll(rem);
		if(this.decisions.get(block.position)!=-1) {
			this.decisions.set(block.position, -1);
			for(int i=0;i<des-1;i++) {
				this.decisions.remove(block.position+1);
			}
		}
		
		//System.out.println(getDecisionsUsed(this.));
		return m;
	}
	public boolean isComplete() {
		for(Integer i:this.decisions)if(i.intValue()==-1)return false;
		return true;
	}
	public int getDecisionsUsed(RecursiveBlock block) {
		return block.decisionsUsed();
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
		/*shapes.sort(new Comparator<Shape>(){
			@Override
			public int compare(Shape o1, Shape o2) {
				return o1.priority<o2.priority?-1:o1.priority>o2.priority?1:0;
			}
		});*/
		shapes.stream().forEach(s->s.draw(g2));
		this.blockIluminations.clear();
	}
	public void buildBlocks(String symbol) {
		RecursiveBlock r = new RecursiveBlock(this, symbol);
		this.setRoot(r);
		shapes = new ArrayList<DrawElement.Shape>();
		cursor=0;
		Objects.requireNonNull(this.base);
		root.setBase(base);
		root.paint(shapes);
	}
	public void merge(BlockManager manager2, int pos) {
		//preserve pending
		for(InputIndex ii:manager2.inputsIdx) {
			ii.pos+=pos;
		}
		for(InputIndex ii:this.inputsIdx) {
			if(ii.pos>pos)ii.pos+=manager2.decisions.size()-1;
		}
		this.inputsIdx.addAll(manager2.inputsIdx);
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
	protected static class InputIndex{
		int pos;
		String text;
		public InputIndex(int pos, String text) {
			this.pos = pos;
			this.text = text;
		}
		public JSONObject toJSON() {
			return new JSONObject().put("text", text).put("pos", pos);
		}
		@Override
		public String toString() {
			return String.valueOf(pos);
		}
	}
	public InputIndex getInput(int pos) {
		for(InputIndex ii:this.inputsIdx)if(ii.pos==pos)return ii;
		return null;
	}
	public String getInputText(int pos) {
		for(InputIndex ii:this.inputsIdx)if(ii.pos==pos)return ii.text;
		return null;
	}
	public void setInputText(String text, int pos) {
		boolean found = false;
		for(InputIndex ii:this.inputsIdx) {
			if(ii.pos==pos) {
				ii.text = text;
				found = true;
				break;
			}
		}
		if(!found)this.inputsIdx.add(new InputIndex(pos, text));
	}
	public void propagateRightClick(Point point, Component c) {
		root.rightClick(point, c);
	}
	public static BlockManager fromJSON(JSONObject o) {
		BlockManager m = new BlockManager(o.getString("filename"));
		m.setBase(new Vector2D(o.getJSONObject("base")));
		Iterator<Object> it = o.getJSONArray("decisions").iterator();
		while(it.hasNext()) {
			m.decisions.add((Integer) it.next());
		}
		JSONArray inputs = o.getJSONArray("inputs");
		for(int i=0;i<inputs.length();i++) {
			JSONObject idx = inputs.getJSONObject(i);
			m.inputsIdx.add(new InputIndex(idx.getInt("pos"),idx.getString("text")));
		}
		return m;
	}
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Integer d:this.decisions)arr.put(d);
		JSONArray arr2 = new JSONArray();
		for(InputIndex ii:this.inputsIdx)arr2.put(ii.toJSON());
        return new JSONObject().put("filename", this.filename)
				   			   .put("decisions", arr)
				   			   .put("inputs", arr2)
				   			   .put("init", ((RecursiveBlock)root).getRule())
				   			   .put("root", root.toJSON())
				   			   .put("base", this.base.toJSON())
				   			   .put("complete", isComplete());
        						
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
	public Vector2D getBase() {
		return base;
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
		if(this.root!=null)this.root.base=base;
	}
	public void updateRoot() {
		if(this.root!=null)this.root.base=base;
	}
	public BlockInfoSupplier getBlocksInfo() {
		return blocksInfo;
	}
	public List<InputIndex> getInputsIdx() {
		return inputsIdx;
	}
}
