package block_manipulation;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import block_manipulation.BlockEditor;
import block_manipulation.BlockSelector;
import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;

public class BlockConstructionLauncher extends JPanel{
	BlockEditor editor;
	public BlockConstructionLauncher(String filename) {
		Dimension dim = new Dimension(800+300+50,800+50);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		
		this.setLayout(new BorderLayout());

	
		JScrollPane scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(800,800));
		editor = new BlockEditor(new Dimension(800,800));
		scroll.setViewportView(editor);
		this.add(scroll, BorderLayout.CENTER);

		JScrollPane scroll2 = new JScrollPane();
		scroll2.setPreferredSize(new Dimension(300,800));
		BlockSelector selector = new BlockSelector(new Dimension(500,800), editor, filename);
		scroll2.setViewportView(selector);
		this.add(scroll2, BorderLayout.LINE_END);


	}
	public BlockEditor getEditor() {
		return editor;
	}
	public static void main(String args[]) {
		
		SwingUtilities.invokeLater(()->{JFrame f = new JFrame();
										f.setContentPane(new BlockConstructionLauncher("resources/skeletons/global.sklt"));
										//f.setLocationRelativeTo(null);
										f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
										f.pack();
										f.setVisible(true);
									   }
										
		);
	}

}
