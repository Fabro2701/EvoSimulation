package setup.gui.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.BlockInfoSingleton;
import block_manipulation.Vector2D;
import block_manipulation.BlockInfoSingleton.BlockInfoSupplier;
import block_manipulation.block.BlockManager;
import block_manipulation.block.RecursiveBlock;
import block_manipulation.parsing.BlockParser;

public class BlockSelector extends JPanel{
	BlockEditor editor;
	List<BlockManager>managers;
	String currentSymbol;
	String filename;
	BlockInfoSupplier blocksInfo;
	public BlockSelector(Dimension dim, BlockEditor editor, String filename) {
		this.filename = filename;
		this.editor = editor;
		this.editor.setBlockSelector(this);
		this.setPreferredSize(dim);
		this.blocksInfo = BlockInfoSingleton.fromFile(filename);
		
		//this.insertInitBlocks();
		
		this.managers = new ArrayList<>();

		//this.loadQueue("COND");
		this.setMouse();
		this.setButtons();
		
	}
	private void setButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ButtonGroup g = new ButtonGroup();
		Function<String,ActionListener> al = (String s)->{return (a)->loadQueue(s);};
		for(String k:blocksInfo.getKeys()) {
			JRadioButton b = new JRadioButton(k);
			b.addActionListener(al.apply(k));
			g.add(b);
			panel.add(b);
		}
		this.add(panel);
	}
	public void loadQueue(String symbol) {
		this.currentSymbol = symbol;
		this.managers.clear();
		
		JSONArray descs = blocksInfo.getDesc(symbol);
		for(int i=0;i<descs.length();i++) {
			BlockManager manager = new BlockManager(filename);
			manager.setBase(new Vector2D(50f, 50f+i*70f));
			manager.getDecisions().add(i);
			managers.add(manager);
		}
		this.repaint();
	}
	private void insertInitBlocks() {
		BlockManager m1 = new BlockManager(filename);
		m1.setBase(new Vector2D(20f,80f));
		m1.getDecisions().add(0);
		BlockManager m2 = new BlockManager(filename);
		m2.setBase(new Vector2D(100f,70f));
		m2.getDecisions().add(0);
		BlockManager m3 = new BlockManager(filename);
		m3.setBase(new Vector2D(100f,130f));
		m3.getDecisions().add(0);
		
		editor.insertBlock("TRANSITION", m1);
		editor.insertBlock("COND", m2);
		editor.insertBlock("LINE", m3);
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int i = 0;
		for(BlockManager manager:managers) {
			g2.setColor(Color.black);
			RecursiveBlock block1 = new RecursiveBlock(manager, this.currentSymbol);

			manager.setRoot(block1);
			manager.paint(g2);
			
			i++;
		}
	}
	private void setMouse() {
		MouseAdapter mouseA = new MouseAdapter() {
			boolean pressed = false;
    		Point current = null;
    		BlockManager currentManager = null;
    		@Override
			public void mouseClicked(MouseEvent e) {
    			if(SwingUtilities.isLeftMouseButton(e)) {
    				for(BlockManager manager:managers) {
        				if(manager.getRoot().findRecursive(e.getPoint())!=null) {
        					BlockSelector.this.editor.insertBlock(currentSymbol, manager, new Vector2D(400,400));
        					BlockSelector.this.repaint();
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
				for(BlockManager manager:managers) {
    				if(manager.getRoot().findRecursive(e.getPoint())!=null) {
    					currentManager = manager;
    				}
    			}
				
			}
    		@Override
			public void mouseReleased(MouseEvent e) {
    			pressed = false;
    			currentManager = null;
			}
    		@Override
			public void mouseDragged(MouseEvent e) {
				if(pressed && currentManager != null) {
					currentManager.move(current, e.getPoint());
					current = e.getPoint();
					if(current.x<=0) {
						editor.setBufferBlock(currentManager);
						//BlockSelector.this.transferFocusBackward();;
						editor.requestFocusInWindow();
						currentManager = null;
					}
					repaint();
				}
				
			}
		};
		this.addMouseListener(mouseA);
		this.addMouseMotionListener(mouseA);;
		this.addMouseWheelListener(mouseA);
		
	}
}
