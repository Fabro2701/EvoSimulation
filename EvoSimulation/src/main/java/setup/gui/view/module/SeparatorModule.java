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

import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.view.ViewPanel;
import setup.gui.view.ViewPanel.State;

public class SeparatorModule extends Module{
	JDialog dialog;
	JTextPane editor;
	EntitySeparator separator;
	
	public SeparatorModule(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		super(father, viewPanel, ctrl);
		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(700,700));
		dialog.setLayout(new BorderLayout());
		editor = new JTextPane();
		dialog.add(editor, BorderLayout.CENTER);
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save(e));
		dialog.add(saveButton, BorderLayout.PAGE_END);
		dialog.setLocationRelativeTo(father);
		dialog.pack();
	}


	@Override
	public void open(State...states) {
		//reading
		
		
		dialog.setVisible(true);
	}
	private Object save(ActionEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

}
