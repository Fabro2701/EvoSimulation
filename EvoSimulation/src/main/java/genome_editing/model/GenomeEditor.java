package genome_editing.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import block_manipulation.Vector2D;
import block_manipulation.block.Block;
import block_manipulation.block.BlockManager;
import block_manipulation.block.RecursiveBlock;
import simulator.Constants;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;

public class GenomeEditor extends Editor{
	Genotype geno;
	BlockManager manager;
	Chromosome<Chromosome.Codon>chChoice;
	public GenomeEditor(Dimension dim, Genotype geno) {
		super(dim);
		this.geno = geno;
		this.chChoice = geno.get(0);
		this.init();
	} 

	private void init() {
		manager = new BlockManager(new Vector2D(20f,80f));
		List<Integer>decisions = manager.getDecisions();
		decisions.clear();
		for(int i=0;i<Constants.CHROMOSOME_LENGTH;i++) {
			decisions.add(this.chChoice.getCodon(i).getIntValue());
		}
		decisions.set(1, -1);
		
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
				Block.changeMult(-e.getWheelRotation()/10f);
				GenomeEditor.this.repaint();
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
		/*for(int i=0;i<Constants.PLOIDY;i++) {
			g2.drawString(geno.toString(i), 5, 10+13*i);		
		}*/
		g2.drawString(manager.getDecisions().toString(), 5, 10);	
		
		
		RecursiveBlock block1 = new RecursiveBlock(manager, "IF");

		
		manager.setRoot(block1);
		manager.paint(g2);
		
		//ghost blocks pending

	}
	
	
}
