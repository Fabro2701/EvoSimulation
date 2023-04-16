package state_diagram.elements.properties;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import state_diagram.elements.Transition;
import state_diagram.elements.Transition.TRANSITION_TYPE;

public class TransitionProperties extends ElementProperties {
	Transition e;
	JTextArea  codeField;
	public TransitionProperties(JScrollPane father, Transition e) {
		super(father);
		this.e = e;

		this.setLayout(new BorderLayout());
		JPanel proppanel = new JPanel();
		proppanel.setLayout(new GridLayout(0,2));
		
		JLabel codeLabel = new JLabel("Code:");
		codeField = new JTextArea(e.getCode());
		proppanel.add(codeLabel);proppanel.add(codeField);
		
		
		JButton saveb = new JButton("save");
		saveb.addActionListener(a->{
			save();
			
			e.getDiagram().repaint();
		});

		this.add(proppanel, BorderLayout.PAGE_START);
		this.add(saveb, BorderLayout.PAGE_END);
	}
	@Override
	protected void save() {
		e.setType(codeField.getText().equals("")?TRANSITION_TYPE.TRUE:TRANSITION_TYPE.COND);
		e.setCode(codeField.getText());
	}

	@Override
	protected void _load() {
		codeField.setText(e.getCode());
	}

}
