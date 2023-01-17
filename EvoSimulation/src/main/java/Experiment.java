import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

import simulator.LauncherGUI;
import simulator.OptimizedLauncherGUI;
import simulator.control.Controller;
import simulator.control.SetupController;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.optimizer.BasicOptimizer;
import simulator.model.optimizer.UniformGridOptimizer;
import statistics.StatsData;
import statistics.StatsManager;

public class Experiment {
	VISU visualization;
	OPTIMIZER optimizer;
	String map;
	public void run() {
		try {
			parse();
		} catch (FileNotFoundException e) {
			System.err.println("Error while parsing Experiment attributes");
			e.printStackTrace();
		}
	}
	private void parse() throws FileNotFoundException {
		

		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>("eventsFactory");
		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>("entitiesFactory");
		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>("statsFactory");
		 
		StatsManager statsManager = new StatsManager("obesidad", statsFactory);
		EventManager eventManager = new EventManager();
		
		SetupController setup = SetupController.from("resources/setup/obesidad.stp");
		EvoSimulator simulator = new EvoSimulator(this.map);
		if(this.optimizer == OPTIMIZER.BASIC) {
			simulator.setOptimizer(new BasicOptimizer(simulator));
		}
		else if(this.optimizer == OPTIMIZER.UNIFORM_GRID) {
			simulator.setOptimizer(new UniformGridOptimizer(simulator,3,3));
		}
		simulator.setDebug(true);
		simulator.loadSetup(setup);
		Controller controller = new Controller(simulator, entityFactory, eventFactory, eventManager,statsManager);
		
		controller.loadEvents(new FileInputStream("resources/loads/events/obesidad.json"));
		
		if(this.visualization == VISU.BASIC) {
			SwingUtilities.invokeLater(() -> {
				new LauncherGUI(controller).setVisible(true);
			});
		}
		else if(this.visualization == VISU.OPTIMIZED) {
			SwingUtilities.invokeLater(() -> {
	        	new OptimizedLauncherGUI().setVisible(true);
	        });
		}
	}
	public static class Builder{
		VISU visualization = VISU.BASIC;
		OPTIMIZER optimizer = OPTIMIZER.BASIC;
		String map;
		public Builder(String map) {
			this.map = map;
		}
		public Builder setVisualization(VISU visu) {
			this.visualization = visu;
			return this;
		}
		public Builder setOptimizer(OPTIMIZER optimizer) {
			this.optimizer = optimizer;
			return this;
		}
		public Experiment build() {
			return new Experiment(this);
		}
	}
	public enum VISU{
		BASIC,OPTIMIZED;
	}
	public enum OPTIMIZER{
		BASIC,UNIFORM_GRID;
	}
	private Experiment(Builder builder) {
		this.visualization = builder.visualization;
		this.optimizer = builder.optimizer;
		this.map = builder.map;
	}
	public static void main(String args[]) {
		Experiment exp = new Experiment.Builder("flat1000").setVisualization(VISU.BASIC)
												 .setOptimizer(OPTIMIZER.UNIFORM_GRID)
												 .build();
		exp.run();
	}
}
