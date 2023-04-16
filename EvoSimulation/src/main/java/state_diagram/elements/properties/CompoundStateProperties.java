package state_diagram.elements.properties;

import javax.swing.JScrollPane;

import state_diagram.elements.CompoundState;

public class CompoundStateProperties extends ElementProperties {
	CompoundState e;
	public CompoundStateProperties(JScrollPane father, CompoundState e) {
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
