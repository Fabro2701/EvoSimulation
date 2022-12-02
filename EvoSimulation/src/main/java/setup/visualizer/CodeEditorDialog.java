package setup.visualizer;

import java.awt.Frame;

import org.graphstream.graph.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.ModuleController;

public class CodeEditorDialog extends CodeEditorDialogGUI{
	Node node;
	public CodeEditorDialog(Frame parent, boolean modal) {
		super(parent, modal);
		// TODO Auto-generated constructor stub
	}
	public CodeEditorDialog(Node node, ModuleController module) {
		super((String)node.getAttribute("ui.label"));
		this.node = node;
		this.jEditorPane.setText(module.getCode(node.getAttribute("ui.label")));
		this.setVisible(true);
	}
}
