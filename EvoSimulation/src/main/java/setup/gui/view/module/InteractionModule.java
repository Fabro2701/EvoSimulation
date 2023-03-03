package setup.gui.view.module;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;
import setup.gui.block.BlockEditor;
import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.view.ViewPanel;
import setup.gui.view.ViewPanel.State;

public class InteractionModule extends Module{
	JDialog dialog;
	
	
	public InteractionModule(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		super(father, viewPanel, ctrl);
		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(800,800));
		dialog.setLayout(new BorderLayout());
		BlockManager manager = new BlockManager(new Vector2D(20f,80f));
		BlockEditor editor = new BlockEditor(manager, "LINE", new Dimension(800,800));
		dialog.add(editor, BorderLayout.CENTER);
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save(e));
		dialog.add(saveButton, BorderLayout.PAGE_END);
		dialog.setLocationRelativeTo(father);
		dialog.pack();
	}

	@Override
	public void open(State...states) {
		State state1 = states[0];
		State state2 = states[1];

		
		
		dialog.setVisible(true);
	}
	public void save(ActionEvent e) {
		
	}
}
