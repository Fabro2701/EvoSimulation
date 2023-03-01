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
import setup.gui.view.ViewPanel.State;

public class SeparatorModule extends Module{
	JDialog dialog;
	JTextPane editor;
	EntitySeparator separator;
	
	public SeparatorModule(JFrame father, SetupEditorController ctrl) {
		super(father, ctrl);
		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(400,400));
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
	public void open(State state) {
		//reading
		separator = ctrl.getSeparators().get(state.getId());
		if(separator.getAtt()!=null) {
			StringJoiner sj = new StringJoiner(",");
			for(String v:separator.getValues())sj.add(v);
			editor.setText("("+separator.getAtt()+")"+sj.toString());
		}
		else editor.setText("");
		
		dialog.setVisible(true);
	}
	public void save(ActionEvent e) {
		try {
			String text = editor.getText();
			Pattern p = Pattern.compile("^[(][^)]+[)]");
			Matcher m = p.matcher(text);
			m.find();
			String att = text.substring(0, m.end());
			separator.setAtt(att);
			
			String[] valsRead = text.substring(m.end()).split(",");
			separator.setValues(valsRead);
			father.repaint();
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this.dialog, "Failed saving separator, sintax:(attId)value1,value2,value3", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
			System.err.println();
			ex.printStackTrace();
		}
	}
}
