package setup.gui.view.module;

import javax.swing.JFrame;
import javax.swing.JPanel;

import setup.gui.control.SetupEditorController;
import setup.gui.view.ViewPanel;
import setup.gui.view.ViewPanel.State;;

public abstract class Module {
	JFrame father;
	SetupEditorController ctrl;
	ViewPanel viewPanel;
	public Module(JFrame father, ViewPanel viewPanel, SetupEditorController ctrl) {
		this.father = father;
		this.viewPanel = viewPanel;
		this.ctrl = ctrl;
	}
	public abstract void open(State...states); 
}
