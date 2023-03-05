package setup.gui.view.module;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.BlockManager;
import block_manipulation.block.RecursiveBlock;
import setup.gui.block.BlockConstructionLauncher;
import setup.gui.control.SetupEditorController;
import setup.gui.view.ViewPanel;
import setup.gui.view.ViewPanel.State;

public class FSMModule extends Module{
	JDialog dialog;
	BlockConstructionLauncher launcher;
	
	public FSMModule(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		super(father, viewPanel, ctrl);
		
	}


	@Override
	public void open(State...states) {
		dialog = new JDialog(this.father);
		dialog.setPreferredSize(new Dimension(1300,800));
		dialog.setResizable(true);
		dialog.setLayout(new BorderLayout());
		
		launcher = new BlockConstructionLauncher("resources/skeletons/fsm1.sklt");
		launcher.getEditor().loadBlocks(load());
		dialog.add(launcher, BorderLayout.CENTER);
		
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save(states[0]));
		dialog.add(saveButton, BorderLayout.PAGE_END);
		//dialog.setLocationRelativeTo(father);
		dialog.pack();
		dialog.setVisible(true);
	}


	private JSONArray load() {
		return ctrl.pullFSMs();
	}
	private void save(State state) {
		List<BlockManager> managers = launcher.getEditor().getManagers();
		for(BlockManager manager:managers) {
			if(((RecursiveBlock)manager.getRoot()).getRule().equals("FSM_DEF")) {
				JSONObject jo = manager.toJSON();
				ctrl.pushFSM(jo);
			}
		}
	}


}
