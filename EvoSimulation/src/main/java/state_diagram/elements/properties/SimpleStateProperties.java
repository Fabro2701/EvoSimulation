package state_diagram.elements.properties;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import state_diagram.elements.SimpleState;

public class SimpleStateProperties extends ElementProperties {
	SimpleState e;
	JTextField idField,restField,continuousField;
	JTextArea actionField;
	public SimpleStateProperties(JScrollPane father, SimpleState e) {
		super(father);
		this.e = e;

		this.setLayout(new BorderLayout());
		JPanel proppanel = new JPanel();
		proppanel.setLayout(new GridLayout(0,2));
		
		JLabel idLabel = new JLabel("ID:");
		idField = new JTextField(e.getId());
		proppanel.add(idLabel);proppanel.add(idField);
		
		JLabel restLabel = new JLabel("Rest:");
		restField = new JTextField(String.valueOf(e.getRest()));
		proppanel.add(restLabel);proppanel.add(restField);
		
		JLabel continuousLabel = new JLabel("Continuous:");
		continuousField = new JTextField(String.valueOf(e.isContinuous()));
		proppanel.add(continuousLabel);proppanel.add(continuousField);
		
		JLabel actionLabel = new JLabel("Action:");
		actionField = new JTextArea(e.getAction());
		proppanel.add(actionLabel);proppanel.add(actionField);
		
		
		JButton saveb = new JButton("save");
		saveb.addActionListener(a->{
			save();
		});

		this.add(proppanel, BorderLayout.PAGE_START);
		this.add(saveb, BorderLayout.PAGE_END);
	}

	@Override
	protected void _load() {
		idField.setText(e.getId());
		restField.setText(String.valueOf(e.getRest()));
		continuousField.setText(String.valueOf(e.isContinuous()));
		actionField.setText(e.getAction());
	}

	@Override
	protected void save() {
		e.updateId(idField.getText());
		e.setRest(Integer.valueOf(restField.getText()));
		e.setContinuous(Boolean.valueOf(actionField.getText()));
		e.setAction(actionField.getText());
		e.getDiagram().repaint();
	}

}
