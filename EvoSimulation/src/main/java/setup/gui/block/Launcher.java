package setup.gui.block;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;

public class Launcher extends JFrame{
	public Launcher() {
		Dimension dim = new Dimension(800+300+50,800+50);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		
		this.setLayout(new FlowLayout());

	
		JScrollPane scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(800,800));
		BlockEditor editor = new BlockEditor(new Dimension(1500,800));
		scroll.setViewportView(editor);
		this.add(scroll);

		JScrollPane scroll2 = new JScrollPane();
		scroll2.setPreferredSize(new Dimension(300,800));
		BlockSelector selector = new BlockSelector(new Dimension(500,800), editor, "resources/skeletons/fsm1.sklt");
		scroll2.setViewportView(selector);
		this.add(scroll2);

		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->new Launcher().setVisible(true));
	}

}
