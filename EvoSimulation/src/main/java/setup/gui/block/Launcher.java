package setup.gui.block;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;

public class Launcher extends JFrame{
	public Launcher() {
		Dimension dim = new Dimension(805,850);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);

	
		JScrollPane scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(800,800));
		BlockEditor editor = new BlockEditor(new Dimension(1500,800));
		scroll.setViewportView(editor);
		this.add(scroll);

		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->new Launcher().setVisible(true));
	}

}
