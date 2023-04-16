package state_diagram.elements.properties;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class ElementProperties extends JPanel{
	JScrollPane father;
	public ElementProperties(JScrollPane father) {
		this.father = father;
	}
	public void load() {
		_load();
		Component last = father.getViewport().getView();
		if(last!=null) {
			((ElementProperties)last).save();
		}
		father.setViewportView(this);
	}
	protected abstract void _load();
	protected abstract void save();
}
