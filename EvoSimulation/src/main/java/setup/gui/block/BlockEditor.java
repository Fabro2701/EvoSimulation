package setup.gui.block;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.Vector2D;
import block_manipulation.block.Block;
import block_manipulation.block.BlockManager;
import block_manipulation.block.RecursiveBlock;

public class BlockEditor extends JPanel{
	List<BlockManager> managers;
	private List<String> initSymbols;
	private BlockSelector selector;
	BlockManager bufferBlock;
	private String bufferSymbol;
	
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

	public BlockManager insertBlock(String initSymbol, BlockManager manager, Vector2D pos) {
		BlockManager m = (BlockManager)manager.clone();
		if(pos!=null)m.setBase(pos);
		this.managers.add(m);
		this.initSymbols.add(initSymbol);
		this.repaint();
		return m;
	}
	public void insertBlock(String initSymbol, BlockManager manager) {
		insertBlock(initSymbol, manager, null);
	}
	private void init() {
		MouseAdapter mouseA = new MouseAdapter() {
			boolean pressed = false;
    		Point current = null;
    		BlockManager currentManager = null;
			Block currentBlock = null;
			int currentPos = 0;
    		@Override
			public void mouseClicked(MouseEvent e) {
    			
    			if(SwingUtilities.isLeftMouseButton(e)) {
    				for(BlockManager manager:managers) {
        				if((currentBlock=manager.getRoot().findRecursive(e.getPoint()))!=null) {
        					currentManager = manager;
        					currentPos = ((RecursiveBlock)currentBlock).getPosition();
        					
        					//System.out.println("des "+manager.getDecisionsUsed((RecursiveBlock) currentBlock));
        					if(((RecursiveBlock) currentBlock).getPosition()!=0) {
        						initSymbols.add(0, ((RecursiveBlock) currentBlock).getRule());
            					BlockManager m = currentManager.detach((RecursiveBlock) currentBlock);
            					managers.add(0, m);
            					repaint();
        					}
        					
        					break;
        				}
        			}
    				//if(managers.size()>0)System.out.println(managers.get(0).getRoot().toJSON().toString(4));
    				/*for(BlockManager manager:managers) {
    					if(manager.flip(e)) {
                			repaint();
        				}
    				}*/
    				
    			}
    			if(SwingUtilities.isRightMouseButton(e)) {
    				for(BlockManager manager:managers) {
    					manager.propagateRightClick(e.getPoint(), BlockEditor.this);
    				}
    			}
			}
    		@Override
			public void mousePressed(MouseEvent e) {
    			pressed = true;
    			current = e.getPoint();
    			if(pressed && bufferBlock != null) {
    				currentManager = insertBlock(bufferSymbol, bufferBlock, new Vector2D(e.getPoint().x, e.getPoint().y));
    				bufferBlock = null;
					repaint();
    			}
    			if(currentManager==null) {
    				for(BlockManager manager:managers) {
        				if((currentBlock=manager.getRoot().findRecursive(e.getPoint()))!=null) {
        					currentManager = manager;
        					currentPos = ((RecursiveBlock)currentBlock).getPosition();
        					break;
        				}
        			}
    				if(currentBlock!=null) {
    					
    					//System.out.println(currentPos);
    					//System.out.println(currentManager.getRoot()+"  "+ currentBlock);
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
    			//send to the foreground the current pending
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
					//System.out.println(currentPos);
					if(currentPos==0 || (currentBlock !=null && ((RecursiveBlock)currentBlock).isIncomplete())) {
						currentManager.move(current, e.getPoint());
					}
					else if(currentPos>0) {
						
					}
					
					current = e.getPoint();
					repaint();
				}
				
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Block.changeMult(-e.getWheelRotation()/10f);
				BlockEditor.this.repaint();
			}
			@Override
		    public void mouseEntered(MouseEvent e) {
	            currentPos=0;
				Point mousePoint = new Point(e.getPoint());
				SwingUtilities.convertPointToScreen(mousePoint, BlockEditor.this);
		        try {
		            Robot robot = new Robot();
		            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		            robot.mouseMove(mousePoint.x -1, mousePoint.y);
		            currentPos=0;
		            //robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		        } catch (AWTException ex) {
		            ex.printStackTrace();
		        }
				//BlockEditor.this.repaint();
		    }
		};
		this.addMouseListener(mouseA);
		this.addMouseMotionListener(mouseA);;
		this.addMouseWheelListener(mouseA);
		this.setFocusable(true);

	}
	public void loadBlocks(JSONArray load) {
		for(int i=0;i<load.length();i++) {
			JSONObject o = load.getJSONObject(i);
			this.managers.add(BlockManager.fromJSON(o));
			this.initSymbols.add(o.getString("init"));
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
		g2.fillRect(2, 0, this.getWidth(), this.getHeight());
		
		int yshift = 0;
		for(BlockManager manager:managers) {
			
			
			RecursiveBlock block1 = new RecursiveBlock(manager, this.initSymbols.get(yshift));

			
			manager.setRoot(block1);
			manager.paint(g2);
			

			g2.setColor(Color.black);
			g2.drawString(manager.getDecisions().toString()+" "+manager.getInputsIdx().toString(), 5, 10+yshift*15);	
			
			yshift++;
		}
	}
	public void setBlockSelector(BlockSelector selector) {
		this.selector = selector;
	}
	public void setBufferBlock(BlockManager bufferBlock) {
		this.setBufferBlock(bufferBlock, ((RecursiveBlock)bufferBlock.getRoot()).getRule());
	}
	public void setBufferBlock(BlockManager bufferBlock, String symbol) {
		this.bufferBlock = bufferBlock;
		this.bufferSymbol = symbol;
	}

	public List<BlockManager> getManagers() {
		return managers;
	}

	public List<String> getInitSymbols() {
		return initSymbols;
	}

}
