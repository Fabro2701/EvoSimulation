package setup.gui.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockCreator;
import block_manipulation.block.BlockManager;
import block_manipulation.block.RecursiveBlock;
import block_manipulation.parsing.BlockParser;

public class BlockSelector extends JPanel{
	BlockEditor editor;
	List<BlockManager>managers;
	LinkedHashMap<String, JSONArray> blockDescs;
	String currentSymbol;
	public BlockSelector(Dimension dim, BlockEditor editor) {
		
		this.editor = editor;
		this.editor.setBlockSelector(this);
		this.setPreferredSize(dim);
		
		this.insertInitBlocks();
		
		this.managers = new ArrayList<>();
		BlockParser parser = new BlockParser();
		JSONObject program = parser.parseFile("test");

		blockDescs = BlockCreator.loadBlocks(program);
		//this.loadQueue("COND");
		this.setMouse();
		this.setButtons();
		
	}
	private void setButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ButtonGroup g = new ButtonGroup();
		Function<String,ActionListener> al = (String s)->{return (a)->loadQueue(s);};
		for(String k:blockDescs.keySet()) {
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
		
		JSONArray descs = this.blockDescs.get(symbol);
		for(int i=0;i<descs.length();i++) {
			BlockManager manager = new BlockManager(new Vector2D(50f,50f+i*70f));
			manager.getDecisions().add(i);
			managers.add(manager);
		}
		this.repaint();
	}
	private void insertInitBlocks() {
		BlockManager m1 = new BlockManager(new Vector2D(20f,80f));
		m1.getDecisions().add(0);
		BlockManager m2 = new BlockManager(new Vector2D(100f,70f));
		m2.getDecisions().add(0);
		BlockManager m3 = new BlockManager(new Vector2D(100f,130f));
		m3.getDecisions().add(0);
		
		editor.insertBlock("IF", m1);
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
		};
		this.addMouseListener(mouseA);
		this.addMouseMotionListener(mouseA);;
		this.addMouseWheelListener(mouseA);
	}
}
