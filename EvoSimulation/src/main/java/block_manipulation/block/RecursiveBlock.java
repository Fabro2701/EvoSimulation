package block_manipulation.block;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.block.DrawElement.StringShape;
import block_manipulation.Vector2D;

public class RecursiveBlock extends Block{
	String rule;
	List<Block>blocks;
	Color color;
	int position;//in chromosome
	boolean incomplete=false;
	
	public RecursiveBlock(BlockManager manager, String ruleReference) {
		super(manager);
		this.rule = ruleReference;
		blocks = new ArrayList<Block>();
		color = manager.getBlockColor(ruleReference);
	}

	@Override
	public Block find(Point point) {
		for(Block b:blocks) {
			if(b instanceof PredefinedBlock) {
				/*List<Shape>shapes = ((PredefinedBlock)b).getBufferShapes();
				for(Shape s:shapes) {
					if(s.contains(point)) return this;
				}*/
				List<Shape>shapes = ((PredefinedBlock)b).getSelectableShapes();
				for(Shape s:shapes) {
					if(s.contains(point)) return this;
				}
			}
		}
		Block block = null;
		for(Block b:blocks) {
			block = b.find(point);
			if(block != null)return block;
		}
		return block;
	}
	public void move(Point current, Point dest) {
		for(Block b:blocks) {
			if(b instanceof PredefinedBlock) {
				List<Shape>shapes = ((PredefinedBlock)b).getBufferShapes();
				for(Shape s:shapes) {
					if(s.contains(current)) {
						this.base.x += dest.x-current.x;
						this.base.y += dest.y-current.y;
					}
				}
			}
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		blocks.clear();
		position = manager.getCursor();
		int elec = manager.getNext();
		if(elec!=-1) {
			JSONArray r = manager.getBlockDescription(rule);
			elec %= r.length();
			JSONArray production = r.getJSONArray(elec);
			float shifty = 0f;
			for(int i=0;i<production.length();i++) {
				JSONObject bo = production.getJSONObject(i);
				Block b = Block.forType(manager, bo);
				if(bo.getString("type").equals("PredefinedBlock")) {
					((PredefinedBlock)b).setColor(color);
				}
				
				b.setBase(new Vector2D(base.x, base.y+shifty));
				blocks.add(b);
				b.paint(shapes);
				shifty += b.getHeight();
			}
		}
		else {
			this.incomplete=true;
			shapes.add(new DrawElement.Rectangle(this.base.x, 
												 this.base.y, 
												 manager.getGraphics().getFontMetrics().stringWidth(rule), 
												 stringHeight, 
												 color)
					  );
			shapes.add(new StringShape(rule, 
					   this.base.x+1f, 
					   this.base.y+stringHeight-2f, 
					   Color.black));
		}
		int a=0;
	}
	@Override
	public float getHeight() {
		if(incomplete)return stringHeight;
		
		float sum = 0f;
		for(Block b:blocks) {
			sum += b.getHeight();
		}
		return sum;
	}
	@Override
	public float getWidth() {
		if(incomplete)return manager.getGraphics().getFontMetrics().stringWidth(rule);
		
		float sum = 0f;
		for(Block b:blocks) {
			sum += b.getWidth();
		}
		return sum;
	}

	public int getPosition() {
		return position;
	}
}
