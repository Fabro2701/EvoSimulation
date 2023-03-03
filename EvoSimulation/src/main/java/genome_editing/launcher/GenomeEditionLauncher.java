package genome_editing.launcher;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import genome_editing.model.GenomeEditor;
import genome_editing.model.GenomeVisualizer;
import simulator.Constants;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;


public class GenomeEditionLauncher extends JFrame{
	public GenomeEditionLauncher() {
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

		JScrollPane scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(800,800));
		GenomeEditor editor = new GenomeEditor(new Dimension(1500,800),geno);
		scroll.setViewportView(editor);
		this.add(scroll);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->new GenomeEditionLauncher().setVisible(true));
	}

}
