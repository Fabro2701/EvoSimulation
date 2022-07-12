package simulator.view;

import java.util.List;

import javax.swing.JLabel;

import simulator.control.Controller;
import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

public class TimeLabel extends JLabel implements SimulatorObserver{
	
	public TimeLabel(Controller ctrl) {
	      //initGUI();
	      ctrl.addObserver(this);
	}
	
	@Override
	public void onRegister(List<Entity> entities, Map map, int time) {
		this.setText(String.valueOf(time));
		repaint();
	}

	@Override
	public void onUpdate(List<Entity> entities, Map map, int time) {
		this.setText(String.valueOf(time));
		repaint();
	}

}
