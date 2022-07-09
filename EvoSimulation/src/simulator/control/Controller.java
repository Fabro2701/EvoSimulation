package simulator.control;

import simulator.model.EvoSimulator;
import simulator.model.SimulatorObserver;
import simulator.view.viewer.Viewer;

public class Controller {
	private EvoSimulator simulator;
	public Controller(EvoSimulator simulator) {
		this.simulator = simulator;
	}
	public void addObserver(SimulatorObserver observer) {
		simulator.addObserver(observer);
		
	}
	public void run(int runs) {
		if(runs<=0)return;
		
		for(int i=0;i<runs;i++) {
			simulator.update();

		}
	}
}
