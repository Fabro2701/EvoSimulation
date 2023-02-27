package genome_editing.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import genome_editing.model.editor.block.Block;
import genome_editing.model.editor.block.BlockCreator;
import genome_editing.model.editor.block.BlockManager;
import genome_editing.model.editor.block.DrawElement;
import genome_editing.model.editor.block.DrawElement.Shape;
import genome_editing.model.editor.block.RecursiveBlock;
import genome_editing.model.editor.parsing.BlockParser;
import genome_editing.model.elements.Vector2D;
import genome_editing.model.elements.Vector3D;
import simulator.Constants;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;

public class GenomeEditor extends Editor{
	Genotype geno;
	GenomeVisualizer visualizer;
	BlockManager manager;
	Chromosome<Chromosome.Codon>chChoice;
	public GenomeEditor(Genotype geno, GenomeVisualizer visualizer) {
		super();
		this.geno = geno;
		this.chChoice = geno.get(0);
		this.visualizer = visualizer;
		this.init();
	} 

	private void init() {
		manager = new BlockManager(new Vector2D(20f,80f));
		MouseAdapter mouseA = new MouseAdapter() {
			boolean pressed = false;
    		Point current = null;
    		@Override
			public void mouseClicked(MouseEvent e) {
    			GenomeEditor.this.manager.flip(e);
    			repaint();
			}
    		@Override
			public void mousePressed(MouseEvent e) {
    			pressed = true;
    			current = e.getPoint();
			}
    		@Override
			public void mouseReleased(MouseEvent e) {pressed = false;}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(pressed) {
					GenomeEditor.this.manager.move(current, e);
					current = e.getPoint();
					repaint();
				}
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
		
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
		
		g2.setColor(Color.black);
		for(int i=0;i<Constants.PLOIDY;i++) {
			g2.drawString(geno.toString(i), 5, 10+13*i);		
		}
		
		
		RecursiveBlock block1 = new RecursiveBlock("CODE");

		manager.setChChoice(this.chChoice);
		manager.setRoot(block1);
		manager.paint(g2);
		
		//ghost blocks pending

	}
	
	
}
