package simulator.view.viewer;

import java.util.List;

import javax.swing.JPanel;

import simulator.model.SimulatorObserver;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

public abstract class AbstractViewer extends JPanel implements SimulatorObserver{

	@Override
	public abstract void onRegister(List<Entity> entities, Map map, int time);

	@Override
	public abstract void onUpdate(List<Entity> entities, Map map, int time);

}
