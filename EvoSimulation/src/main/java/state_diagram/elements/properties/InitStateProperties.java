package state_diagram.elements.properties;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import state_diagram.elements.InitState;

public class InitStateProperties extends ElementProperties {
	InitState e;
	public InitStateProperties(JScrollPane father, InitState e) {
		super(father);
		this.e = e;
	}

	@Override
	protected void _load() {
	}
	@Override
	protected void save() {
	
	}

}
