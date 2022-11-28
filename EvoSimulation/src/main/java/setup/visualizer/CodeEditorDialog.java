package setup.visualizer;

import java.awt.Frame;

import org.graphstream.graph.Node;

public class CodeEditorDialog extends CodeEditorDialogGUI{
	Node node;
	public CodeEditorDialog(Frame parent, boolean modal) {
		super(parent, modal);
		// TODO Auto-generated constructor stub
	}
	public CodeEditorDialog(Node node) {
		super((String)node.getAttribute("ui.label"));
		this.node = node;
		this.setVisible(true);
	}

}
