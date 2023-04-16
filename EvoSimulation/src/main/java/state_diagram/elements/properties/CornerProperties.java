package state_diagram.elements.properties;

import javax.swing.JScrollPane;

import state_diagram.elements.Corner;

public class CornerProperties extends ElementProperties {
	Corner e;
	public CornerProperties(JScrollPane father, Corner e) {
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
