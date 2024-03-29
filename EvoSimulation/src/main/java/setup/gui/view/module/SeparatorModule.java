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
	
	public SeparatorModule(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		super(father, viewPanel, ctrl);
		
	}


	@Override
	public void open(State...states) {
		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(700,200));
		dialog.setLayout(new BorderLayout());
		editor = new JTextPane();
		dialog.add(editor, BorderLayout.CENTER);
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save(states[0]));
		dialog.add(saveButton, BorderLayout.PAGE_END);
		dialog.setLocationRelativeTo(father);
		dialog.pack();
		//reading
		EntitySeparator separator = ctrl.pullSeparators().get(states[0].getId());
		if(separator.getAtt()!=null) {
			StringJoiner sj = new StringJoiner(",");
			for(String v:separator.getValues())sj.add(v);
			editor.setText("("+separator.getAtt()+")"+sj.toString());
		}
		else editor.setText("");
		
		dialog.setVisible(true);
	}
	public void save(State state) {
		try {
			String text = editor.getText();
			Pattern p = Pattern.compile("^[(][^)]+[)]");
			Matcher m = p.matcher(text);
			m.find();
			String att = text.substring(1, m.end()-1);
			
			String[] valsRead = text.substring(m.end()).split(",");
			this.ctrl.pushSeparator((Class<?>) state.getId(), att, valsRead);
			this.viewPanel.recalculate();
			father.repaint();
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this.dialog, "Failed saving separator, sintax:(attId)value1,value2,value3", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
			System.err.println();
			ex.printStackTrace();
		}
	}

}
