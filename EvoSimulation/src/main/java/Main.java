import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

import simulator.LauncherGUI;
import simulator.control.Controller;
import simulator.control.SetupController;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import statistics.StatsData;
import statistics.StatsManager;

public class Main {

	private static EvoSimulator simulator;
	private static Controller controller;  

	public static void main(String args[]) {
		/*List<Builder<StatsData>> statsBuilders = new ArrayList<Builder<StatsData>>();
		statsBuilders.add(new PopulationAgeBuilder());
		statsBuilders.add(new PopulationCountBuilder());
		statsBuilders.add(new EnergyBuilder());
		statsBuilders.add(new MutationReproductionBuilder());
		statsBuilders.add(new ChildDepthBuilder());
		statsBuilders.add(new GenotypeHeterogeneityBuilder());
		statsBuilders.add(new DeadOffSpringBuilder());
		statsBuilders.add(new BestIndividualCodeBuilder());

		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>(statsBuilders);
		*/
		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>("statsFactory");
 
		StatsManager statsManager = new StatsManager("obesidad", statsFactory);
    	
		EventManager eventManager = new EventManager();

		/*List<Builder<Event>> eventBuilders = new ArrayList<Builder<Event>>();
		eventBuilders.add(new AddEntitiesEventBuilder());
		eventBuilders.add(new AddFoodDistributionEventBuilder());
		eventBuilders.add(new AddFoodGeneratorEventBuilder());
		eventBuilders.add(new AddRandomEntitiesGeneratorEventBuilder());
		eventBuilders.add(new AddFoodConditionGeneratorEventBuilder());
		eventBuilders.add(new AddRandomEntitiesConditionGeneratorEventBuilder());
		eventBuilders.add(new SaveSimulationEventBuilder());
		
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>(eventBuilders);
		*/
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>("eventsFactory");

		/*List<Builder<Entity>> entityBuilders = new ArrayList<Builder<Entity>>();
		entityBuilders.add(new SimpleRandomEntityBuilder());
		entityBuilders.add(new SimpleUPEntityBuilder());
		entityBuilders.add(new FoodEntityBuilder());
		entityBuilders.add(new MyIndividualBuilder());
		entityBuilders.add(new MyIndividual2Builder());

		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>(entityBuilders);
		*/
		
		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>("entitiesFactory");

		SetupController setup = SetupController.from("resources/setup/defult.stp");
		simulator = new EvoSimulator("test1000");simulator.setDebug(true);
		simulator.loadSetup(setup);
		controller = new Controller(simulator, entityFactory, eventFactory, eventManager,statsManager);
		
		try {
			int op=0;
			if(op==0) {
				controller.loadEvents(new FileInputStream("resources/loads/events/obesidad.json"));
				//controller.loadEntities(new FileInputStream("resources/loads/entities/test1.json"));
			}
			else {
				controller.loadEntities(new FileInputStream("resources/loads/simulations/filename.json"));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		SwingUtilities.invokeLater(() -> {
			new LauncherGUI(controller).setVisible(true);
		});
		
		
	}
}
