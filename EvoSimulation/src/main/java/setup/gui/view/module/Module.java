package setup.gui.view.module;

import javax.swing.JFrame;
import javax.swing.JPanel;

import setup.gui.control.SetupEditorController;
import setup.gui.view.ViewPanel.State;;

public abstract class Module {
	JFrame father;
	SetupEditorController ctrl;
	public Module(JFrame father, SetupEditorController ctrl) {
		this.father = father;
		this.ctrl = ctrl;
	}
	public abstract void open(State state); 
}
