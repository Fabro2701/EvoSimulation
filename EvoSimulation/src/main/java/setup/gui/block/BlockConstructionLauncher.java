package setup.gui.block;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;

public class BlockConstructionLauncher extends JPanel{
	BlockEditor editor;
	public BlockConstructionLauncher() {
		this("resources/skeletons/fsm1.sklt");
	}
	public BlockConstructionLauncher(String filename) {
		Dimension dim = new Dimension(800+300+50,800+50);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		
		this.setLayout(new FlowLayout());

	
		JScrollPane scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(800,800));
		editor = new BlockEditor(new Dimension(1500,800));
		scroll.setViewportView(editor);
		this.add(scroll);

		JScrollPane scroll2 = new JScrollPane();
		scroll2.setPreferredSize(new Dimension(300,800));
		BlockSelector selector = new BlockSelector(new Dimension(500,800), editor, filename);
		scroll2.setViewportView(selector);
		this.add(scroll2);


	}
	public BlockEditor getEditor() {
		return editor;
	}
	public static void main(String args[]) {
		
		SwingUtilities.invokeLater(()->{JFrame f = new JFrame();
										f.setContentPane(new BlockConstructionLauncher());
										//f.setLocationRelativeTo(null);
										f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
										f.pack();
										f.setVisible(true);
									   }
										
		);
	}

}
