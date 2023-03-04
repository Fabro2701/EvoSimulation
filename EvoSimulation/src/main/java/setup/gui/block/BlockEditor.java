package setup.gui.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import block_manipulation.Vector2D;
import block_manipulation.block.Block;
import block_manipulation.block.BlockManager;
import block_manipulation.block.GhostBlock;
import block_manipulation.block.RecursiveBlock;

public class BlockEditor extends JPanel{
	List<BlockManager> managers;
	private List<String> initSymbols;
	private BlockSelector selector;
	
	public BlockEditor(Dimension dim) {
		managers = new ArrayList<>();
		this.initSymbols = new ArrayList<>();
		/*this.managers.add(new BlockManager(new Vector2D(20f,80f)));
		this.managers.add(new BlockManager(new Vector2D(100f, 70f)));
		this.managers.add(new BlockManager(new Vector2D(100f, 130f)));
		
		
		this.initSymbols.add("IF");
		this.initSymbols.add("COND");
		this.initSymbols.add("LINE");*/
		//this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		//this.setMaximumSize(dim);
		this.init();
	}
	/*public BlockEditor(Dimension dim) {
		manager = new BlockManager(new Vector2D(20f,80f));
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		this.init();
	}*/
	public void insertBlock(String initSymbol, BlockManager manager, Vector2D pos) {
		BlockManager m = (BlockManager)manager.clone();
		if(pos!=null)m.setBase(pos);
		this.managers.add(m);
		this.initSymbols.add(initSymbol);
		this.repaint();
	}
	public void insertBlock(String initSymbol, BlockManager manager) {
		insertBlock(initSymbol, manager, null);
	}
	private void init() {
		/*for(BlockManager manager:managers) {
			List<Integer>decisions = manager.getDecisions();
			decisions.clear();
			for(int i=0;i<100;i++) {
				//decisions.add(RandomSingleton.nextInt(256));
				//decisions.add(-1);
			}
			decisions.add(0);
			//decisions.set(0, 1);
			//decisions.set(1, RandomSingleton.nextInt(256));
			//decisions.set(2, 0);
		}*/
		
		
		MouseAdapter mouseA = new MouseAdapter() {
			boolean pressed = false;
    		Point current = null;
    		BlockManager currentManager = null;
    		@Override
			public void mouseClicked(MouseEvent e) {
    			if(SwingUtilities.isLeftMouseButton(e)) {
    				for(BlockManager manager:managers) {
    					if(manager.flip(e)) {
                			repaint();
        				}
    				}
    				
    			}
    			if(SwingUtilities.isRightMouseButton(e)) {
    				
    			}
			}
    		@Override
			public void mousePressed(MouseEvent e) {
    			pressed = true;
    			current = e.getPoint();
    			if(currentManager==null) {
    				for(BlockManager manager:managers) {
        				if(manager.getRoot().findRecursive(e.getPoint())!=null) {
        					currentManager = manager;
        				}
        			}
    			}
    			
			}
    		@Override
			public void mouseReleased(MouseEvent e) {
    			pressed = false;
    			for(BlockManager manager:managers) {
    				manager.clearIluminations();
    			}
    			BlockManager release = null;
    			Block releaseBlock = null;
    			for(BlockManager manager:managers) {
    				if(manager!=currentManager&&(releaseBlock=manager.getRoot().findRecursive(e.getPoint()))!=null) {
    					release = manager;
    					break;
    				}
    			}
    			
    			if(release != null) {
    					String rule = ((RecursiveBlock)releaseBlock).getRule();
    					int pos = ((RecursiveBlock)releaseBlock).getPosition();
    					if(rule.equals(((RecursiveBlock)currentManager.getRoot()).getRule()) && 
    					    release.getDecisions().get(pos)==-1) {
    						release.merge(currentManager, ((RecursiveBlock)releaseBlock).getPosition());
    	    				int idx = managers.lastIndexOf(currentManager);
    	    				managers.remove(idx);
    	    				initSymbols.remove(idx);
    					}
    			}
    			currentManager = null;
    			//send to the foreground the current
    			repaint();
    		}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(pressed && currentManager!=null) {
					for(BlockManager manager:managers) {
						if(currentManager!=manager) {
							manager.iluminateRecursiveBlocks(((RecursiveBlock)currentManager.getRoot()).getRule());
						}
					}
					currentManager.move(current, e);
					current = e.getPoint();
					repaint();
				}
				
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Block.changeMult(-e.getWheelRotation()/10f);
				BlockEditor.this.repaint();
			}
		};
		this.addMouseListener(mouseA);
		this.addMouseMotionListener(mouseA);;
		this.addMouseWheelListener(mouseA);

	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
		g2.fillRect(2, 0, this.getWidth(), this.getHeight());
		
		int yshift = 0;
		for(BlockManager manager:managers) {
			g2.setColor(Color.black);
			g2.drawString(manager.getDecisions().toString(), 5, 10+yshift*15);	
			
			
			RecursiveBlock block1 = new RecursiveBlock(manager, this.initSymbols.get(yshift));

			
			manager.setRoot(block1);
			manager.paint(g2);
			
			yshift++;
		}
		
		
		//ghost blocks pending

	}
	public void setBlockSelector(BlockSelector selector) {
		this.selector = selector;
	}
}
