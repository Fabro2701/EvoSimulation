import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.SwingUtilities;

import simulator.LauncherGUI;
import simulator.OptimizedLauncherGUI;
import simulator.control.ConstantsController;
import simulator.control.Controller;
import simulator.control.SetupController;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.optimizer.BasicOptimizer;
import simulator.model.optimizer.UniformGridOptimizer;
import simulator.view.ConstantsViewer;
import statistics.StatsData;
import statistics.StatsManager;

public class Experiment {
	VISU visualization;
	OPTIMIZER optimizer;
	String map;
	String eventsFactory,entitiesFactory,statsFactory;
	String statsManager;
	String setup;
	String events;
	boolean constantsCtrl;
	String genes;
	int imgRefreshRate;
	public void run() {
		try {
			parse();
		} catch (IOException e) {
			System.err.println("Error while parsing Experiment attributes");
			e.printStackTrace();
		}
	}
	public static Experiment fromFile(String path) throws FileNotFoundException, IOException {
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(path));
		
		Experiment.Builder builder = new Experiment.Builder(properties.getProperty("map"),
															properties.getProperty("setup"));
		if(properties.containsKey("visualization"))builder.setVisualization(VISU.valueOf(properties.getProperty("visualization")));
		if(properties.containsKey("optimizer"))builder.setOptimizer(OPTIMIZER.valueOf(properties.getProperty("optimizer")));
		if(properties.containsKey("stats"))builder.setStatsManager(properties.getProperty("stats"));
		if(properties.containsKey("events"))builder.setEvents(properties.getProperty("events"));
		if(properties.containsKey("constantsCtrl"))builder.setConstantsCtrl(Boolean.valueOf(properties.getProperty("constantsCtrl")));
		if(properties.containsKey("genes"))builder.setGenes(properties.getProperty("genes"));
		if(properties.containsKey("imgRefreshRate"))builder.setImgRefreshRate(Integer.valueOf(properties.getProperty("imgRefreshRate")));
		
		return builder.build();
	}
	/**
	 * Parse the simulation attributes
	 * @throws IOException 
	 */
	private void parse() throws IOException {
		
		//factories
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>(this.eventsFactory);
		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>(this.entitiesFactory);
		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>(this.statsFactory);
		 
		//managers
		StatsManager statsManager = new StatsManager(this.statsManager, statsFactory);
		EventManager eventManager = new EventManager();
		
		//simulator
		EvoSimulator simulator = new EvoSimulator(this.map);
		//simulator optimizer
		if(this.optimizer == OPTIMIZER.BASIC) {
			simulator.setOptimizer(new BasicOptimizer(simulator));
		}
		else if(this.optimizer == OPTIMIZER.UNIFORM_GRID) {
			simulator.setOptimizer(new UniformGridOptimizer(simulator,3,3));
		}
		
		simulator.setDebug(true);
		simulator.loadSetup(SetupController.from(this.setup));
		simulator.setImgRefreshRate(this.imgRefreshRate);
		
		//controller
		Controller controller = new Controller(simulator, entityFactory, eventFactory, eventManager,statsManager);
		controller.loadEvents(new FileInputStream(this.events));
		
		//genes
		if(genes!=null)GIndividual.Genes.loadFromFile(this.genes);
		
		//constants controller
		if(this.constantsCtrl) {
			ConstantsController constantsCtrl = new ConstantsController();
			SwingUtilities.invokeLater(() -> {
		        	new ConstantsViewer(constantsCtrl);
		    		constantsCtrl.paint();
		        });
		}
		
		//visualization
		if(this.visualization == VISU.BASIC) {
			SwingUtilities.invokeLater(() -> {
				new LauncherGUI(controller).setVisible(true);
			});
		}
		else if(this.visualization == VISU.OPTIMIZED) {
			SwingUtilities.invokeLater(() -> {
	        	new OptimizedLauncherGUI(controller).setVisible(true);
	        });
		}
	}
	public static class Builder{
		VISU visualization = VISU.BASIC;
		OPTIMIZER optimizer = OPTIMIZER.BASIC;
		String map;
		String eventsFactory="eventsFactory",
			   entitiesFactory="entitiesFactory",
			   statsFactory="statsFactory";
		String statsManager;
		String setup;
		String events;
		boolean constantsCtrl = false;
		String genes;
		int imgRefreshRate = 1;
		public Builder(String map, String setup) {
			this.map = map;
			this.setup = setup;
		}
		public Builder setVisualization(VISU visu) {
			this.visualization = visu;
			return this;
		}
		public Builder setOptimizer(OPTIMIZER optimizer) {
			this.optimizer = optimizer;
			return this;
		}
		public Builder setStatsManager(String statsManager) {
			this.statsManager = statsManager;
			return this;
		}
		public Builder setEvents(String events) {
			this.events = events;
			return this;
		}
		public Builder setConstantsCtrl(boolean constantsCtrl) {
			this.constantsCtrl = constantsCtrl;
			return this;
		}
		public Builder setGenes(String genes) {
			this.genes = genes;
			return this;
		}
		public Builder setImgRefreshRate(int imgRefreshRate) {
			this.imgRefreshRate = imgRefreshRate;
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
		this.eventsFactory = builder.eventsFactory;
		this.entitiesFactory = builder.entitiesFactory;
		this.statsFactory = builder.statsFactory;
		this.statsManager = builder.statsManager;
		this.setup = builder.setup;
		this.events = builder.events;
		this.constantsCtrl = builder.constantsCtrl;
		this.genes = builder.genes;
		this.imgRefreshRate = builder.imgRefreshRate;
	}
	public static void main(String args[]) throws FileNotFoundException, IOException {
		/*Experiment exp = new Experiment.Builder("resources/maps/flat1000", "resources/setup/obesidad.stp")
												 .setVisualization(VISU.OPTIMIZED)
												 .setOptimizer(OPTIMIZER.UNIFORM_GRID)
												 .setStatsManager("resources/loads/stats/obesidad.json")
												 .setEvents("resources/loads/events/obesidad.json")
												 .setConstantsCtrl(true)
												 .build();
		exp.run();
		*/
		
		Experiment exp2 = Experiment.fromFile("resources/experiment/obesidad.experiment");
		exp2.run();
	}
}
