import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

import console.model.CommandController;
import console.view.AbstractConsoleGUI;
import simulator.LauncherGUI;
import simulator.OptimizedLauncherGUI;
import simulator.control.ConstantsController;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.SetupController;
import simulator.control.console.SimulationOptionsModel;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.entity.individuals.genome.PolymorphismController;
import simulator.model.optimizer.BasicOptimizer;
import simulator.model.optimizer.DensityOptimizer;
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
	String attId;
	String imgsdir;
	String poly;
	boolean terminalCtrl;
	public void run() {
		try {
			parse();
		} catch (Exception e) {
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
		if(properties.containsKey("attId"))builder.setAttId(properties.getProperty("attId"));
		if(properties.containsKey("imgsdir"))builder.setImgsdir(properties.getProperty("imgsdir"));
		if(properties.containsKey("poly"))builder.setPoly(properties.getProperty("poly"));
		if(properties.containsKey("terminalCtrl"))builder.setTerminalCtrl(Boolean.valueOf(properties.getProperty("terminalCtrl")));

		return builder.build();
	}
	/**
	 * Parse the simulation attributes
	 * @throws IOException 
	 */
	private void parse() throws Exception {
		
		/*try {
		    UIManager.setLookAndFeel( new FlatIntelliJLaf() );
		} catch( Exception ex ) {
		    System.err.println( "Failed to initialize theme. Using fallback." );
		}*/
		
		//factories
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>(this.eventsFactory);
		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>(this.entitiesFactory);
		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>(this.statsFactory);
		 
		//managers
		StatsManager statsManager = null;
		
		statsManager = new StatsManager(this.statsManager, statsFactory);
		
		
		EventManager eventManager = new EventManager();
		
		//simulator
		EvoSimulator simulator = new EvoSimulator(this.map);
		Entity.attId = this.attId;
		if(this.attId!=null)Entity.groupF = e->e.getAttribute(this.attId);
		else Entity.groupF = e->"";
		
		//simulator optimizer
		simulator.setOptimizer(new UniformGridOptimizer(simulator,3,3));
		if(this.optimizer == OPTIMIZER.BASIC) {
			simulator.setOptimizer(new BasicOptimizer(simulator));
		}
		else if(this.optimizer == OPTIMIZER.UNIFORM_GRID) {
			simulator.setOptimizer(new UniformGridOptimizer(simulator,3,3));
		}
		else if(this.optimizer == OPTIMIZER.DENSITY) {
			simulator.setOptimizer(new DensityOptimizer(simulator,8));
		}
		
		simulator.setDebug(true);
		simulator.setImgRefreshRate(this.imgRefreshRate);
		
		//imgs
		if(this.imgsdir!=null)ImageController.loadFromDirectory(this.imgsdir);
		
		//controller
		Controller controller = new Controller(simulator, entityFactory, eventFactory, eventManager,statsManager);
		controller.loadSetup(SetupController.from(this.setup));
		controller.loadEvents(new FileInputStream(this.events));
		controller.loadPE(simulator.getMap().getEntitiesInfo());
		
		//genes
		if(genes!=null)GIndividual.Genes.loadFromFile(this.genes);
		
		//polymorphims
		if(poly!=null)PolymorphismController.loadFromFile(this.poly);
		
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
				LauncherGUI lgui = new LauncherGUI(controller);
				lgui.setFocusable(false);
				lgui.setVisible(true);
				if(this.terminalCtrl) {
					SwingUtilities.invokeLater(()->{
						new AbstractConsoleGUI(700,400,
											   new CommandController(new SimulationOptionsModel(controller, lgui)))
						.setVisible(true);
					});
				}
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
		String eventsFactory="resources/loads/factories/eventsFactory.factory",
			   entitiesFactory="resources/loads/factories/entitiesFactory.factory",
			   statsFactory="resources/loads/factories/statsFactory.factory";
		String statsManager;
		String setup;
		String events;
		boolean constantsCtrl = false;
		String genes;
		int imgRefreshRate = 1;
		String attId;
		String imgsdir;
		String poly;
		boolean terminalCtrl = false;
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
		public Builder setAttId(String attId) {
			this.attId = attId;
			return this;
		}
		public Builder setImgsdir(String imgsdir) {
			this.imgsdir = imgsdir;
			return this;
		}
		public Builder setPoly(String poly) {
			this.poly = poly;
			return this;
		}
		public Builder setTerminalCtrl(boolean terminalCtrl) {
			this.terminalCtrl = terminalCtrl;
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
		BASIC,UNIFORM_GRID,DENSITY;
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
		this.attId = builder.attId;
		this.imgsdir = builder.imgsdir;
		this.poly = builder.poly;
		this.terminalCtrl = builder.terminalCtrl;
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
		//Experiment exp2 = Experiment.fromFile("resources/experiment/obesidad.experiment");
		Experiment exp2 = Experiment.fromFile("resources/scenarios/obesidad/obesidad.experiment");
		exp2.run();
	}
}
