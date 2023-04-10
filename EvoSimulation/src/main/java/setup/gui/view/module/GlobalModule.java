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

import block_manipulation.BlockEditor;
import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;
import block_manipulation.block.RecursiveBlock;
import block_manipulation.BlockConstructionLauncher;
import setup.gui.control.SetupEditorController;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.view.ViewPanel;
import setup.gui.view.ViewPanel.State;

public class GlobalModule extends Module{
	JDialog dialog;
	BlockConstructionLauncher launcher;
	
	public GlobalModule(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		super(father, viewPanel, ctrl);
		
	}
	private JSONObject load() {
		return ctrl.pullGlobal();
	}
	@Override
	public void open(State...states) {

		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(1300,800));
		dialog.setResizable(true);
		dialog.setLayout(new BorderLayout());
		
		launcher = new BlockConstructionLauncher("resources/skeletons/global.sklt");
		JSONObject jo = load();
		if(jo==null) {
			jo = new JSONObject().put("base", new JSONObject().put("x", 250).put("y", 250))
								 .put("decisions", new JSONArray(List.of(0,0,0)))
								 .put("init", "GLOBAL_DEF")
								 .put("filename", "resources/skeletons/global.sklt")
								 .put("inputs", new JSONArray(List.of()));
		}
		launcher.getEditor().loadBlocks(new JSONArray().put(jo));
		dialog.add(launcher, BorderLayout.CENTER);
		
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save());
		dialog.add(saveButton, BorderLayout.PAGE_END);
		//dialog.setLocationRelativeTo(father);
		dialog.pack();
		dialog.setVisible(true);
		
	}
	private void save() {
		List<BlockManager> managers = launcher.getEditor().getManagers();
		for(BlockManager manager:managers) {
			if(((RecursiveBlock)manager.getRoot()).getRule().equals("GLOBAL_DEF")) {
				JSONObject jo = manager.toJSON();
				ctrl.pushGlobal(jo);
			}
		}

	}
}
