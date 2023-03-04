package block_manipulation.block;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.block.DrawElement.StringShape;
import block_manipulation.BlockInfoSingleton;
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
		color = manager.getBlocksInfo().getColor(ruleReference);
	}

	@Override
	public Block findRecursive(Point point) {
		Block block = null;
		for(Block b:blocks) {
			if(b instanceof PredefinedBlock) {
				if((block=b.findPredefined(point))!=null)return this;
			}
		}
		for(Block b:blocks) {
			if((block=b.findRecursive(point))!=null)return block;
		}
		return block;
	}
	@Override
	public Block findPredefined(Point point) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void paint(List<Shape> shapes) {
		blocks.clear();
		position = manager.getCursor();
		if(position>=manager.getDecisions().size())manager.getDecisions().add(-1);
		int elec = manager.getNext();
		if(elec!=-1) {
			this.incomplete=false;
			JSONArray r = manager.getBlocksInfo().getDesc(rule);
			elec %= r.length();
			JSONArray production = r.getJSONArray(elec);
			float shifty = 0f;
			for(int i=0;i<production.length();i++) {
				JSONObject bo = production.getJSONObject(i);
				Block b = Block.forType(manager, bo);
				if(bo.getString("type").equals("PredefinedBlock")) {
					//System.out.println(bo.toString(4));
					((PredefinedBlock)b).setColor(color);
					if(b instanceof InputBlock)((InputBlock)b).reference = this;
				}
				
				b.setBase(new Vector2D(base.x, base.y+shifty));
				blocks.add(b);
				b.paint(shapes);
				shifty += b.getHeight();
			}
		}
		else {
			JSONArray params = new JSONArray();
			params.put(new JSONObject().put("name", "reference")
									   .put("type", "Parameter")
									   .put("value", new JSONObject().put("value", this)));
			
			JSONObject jo = new JSONObject().put("type", "PredefinedBlock")
											.put("id", "Ghost")
											.put("params", params);
			Block b = Block.forType(manager, jo);
			b.setBase(new Vector2D(base.x, base.y));
			blocks.add(b);
			((PredefinedBlock)b).setColor(color);
			b.paint(shapes);
			this.incomplete=true;
			/*shapes.add(new DrawElement.Rectangle(this.base.x, 
												 this.base.y, 
												 manager.getGraphics().getFontMetrics().stringWidth(rule)*mult, 
												 stringHeight, 
												 color)
					  );
			shapes.add(new StringShape(rule, 
					   this.base.x+1f, 
					   this.base.y+stringHeight-2f, 
					   Color.black));*/
		}
	}
	@Override
	public void rightClick(Point point, Component c) {
		for(Block b:blocks) {
			b.rightClick(point, c);
		}

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
		if(incomplete)return manager.getGraphics().getFontMetrics().stringWidth(rule)*mult;
		
		float sum = 0f;
		for(Block b:blocks) {
			sum += b.getWidth();
		}
		return sum;
	}
	@Override
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Block b:this.blocks) {
			arr.put(b.toJSON());
		}
		return new JSONObject().put("type", "RecursiveBlock")
							   .put("rule", this.rule)
							   .put("blocks", arr);
	}

	public int getPosition() {
		return position;
	}

	public String getRule() {
		return rule;
	}


}
