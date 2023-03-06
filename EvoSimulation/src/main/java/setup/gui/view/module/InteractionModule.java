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

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;
import setup.gui.block.BlockConstructionLauncher;
import setup.gui.block.BlockEditor;
import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.view.ViewPanel;
import setup.gui.view.ViewPanel.State;

public class InteractionModule extends Module{
	JDialog dialog;
	BlockConstructionLauncher launcher;
	
	public InteractionModule(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		super(father, viewPanel, ctrl);
		
	}
	private JSONObject load(Object o1, Object o2) {
		return ctrl.pullInteraction(o1, o2);
	}
	@Override
	public void open(State...states) {
		State state1 = states[0];
		State state2 = states[1];

		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(1300,800));
		dialog.setResizable(true);
		dialog.setLayout(new BorderLayout());
		
		launcher = new BlockConstructionLauncher("resources/skeletons/oop.sklt");
		Object o1 = state1.getClazz().equals("class")?state1.getId():state1.getName();
		Object o2 = state2.getClazz().equals("class")?state2.getId():state2.getName();
		JSONObject jo = load(o1, o2);
		if(jo==null) {
			jo = new JSONObject().put("base", new JSONObject().put("x", 250).put("y", 250))
								 .put("decisions", new JSONArray(List.of(0,0,0)))
								 .put("init", "INTERACTION_DEF")
								 .put("filename", "resources/skeletons/oop.sklt")
								 .put("inputs", new JSONArray(List.of(new JSONObject().put("pos", 1).put("text", o1.toString()),new JSONObject().put("pos", 2).put("text", o2.toString()))));
		}
		launcher.getEditor().loadBlocks(new JSONArray().put(jo));
		dialog.add(launcher, BorderLayout.CENTER);
		
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save(state1, state2));
		dialog.add(saveButton, BorderLayout.PAGE_END);
		//dialog.setLocationRelativeTo(father);
		dialog.pack();
		dialog.setVisible(true);
		
	}
	private void save(State state, State state2) {
		// TODO Auto-generated method stub
	}
}
