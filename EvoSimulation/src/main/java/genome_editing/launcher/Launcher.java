package genome_editing.launcher;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import genome_editing.model.GenomeEditor;
import genome_editing.model.GenomeVisualizer;
import simulator.Constants;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;


public class Launcher extends JFrame{
	public Launcher() {
		Dimension dim = new Dimension(805*2,850);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);

		Genotype geno = new Genotype();
		for(int i=0;i<Constants.PLOIDY;i++) {
			geno.add(new Chromosome<Chromosome.Codon>(Constants.CHROMOSOME_LENGTH, Chromosome.Codon::new));
		}
		
		this.setLayout(new GridLayout(1,2));
		
		GenomeVisualizer visualizer = new GenomeVisualizer(800,800,geno);
		this.add(visualizer);
		
		GenomeEditor editor = new GenomeEditor(geno, visualizer);
		this.add(editor);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->new Launcher().setVisible(true));
	}

}
