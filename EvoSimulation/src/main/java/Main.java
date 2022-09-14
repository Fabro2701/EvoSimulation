import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import simulator.LauncherGUI;
import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.builders.Builder;
import simulator.factories.builders.entity.FoodEntityBuilder;
import simulator.factories.builders.entity.MyIndividual2Builder;
import simulator.factories.builders.entity.MyIndividualBuilder;
import simulator.factories.builders.entity.SimpleRandomEntityBuilder;
import simulator.factories.builders.entity.SimpleUPEntityBuilder;
import simulator.factories.builders.events.AddEntitiesEventBuilder;
import simulator.factories.builders.events.AddFoodConditionGeneratorEventBuilder;
import simulator.factories.builders.events.AddFoodDistributionEventBuilder;
import simulator.factories.builders.events.AddFoodGeneratorEventBuilder;
import simulator.factories.builders.events.AddRandomEntitiesConditionGeneratorEventBuilder;
import simulator.factories.builders.events.AddRandomEntitiesGeneratorEventBuilder;
import simulator.factories.builders.events.SaveSimulationEventBuilder;
import simulator.factories.builders.stats.BestIndividualCodeBuilder;
import simulator.factories.builders.stats.ChildDepthBuilder;
import simulator.factories.builders.stats.DeadOffSpringBuilder;
import simulator.factories.builders.stats.EnergyBuilder;
import simulator.factories.builders.stats.GenotypeHeterogeneityBuilder;
import simulator.factories.builders.stats.MutationReproductionBuilder;
import simulator.factories.builders.stats.PopulationAgeBuilder;
import simulator.factories.builders.stats.PopulationCountBuilder;
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
 
		StatsManager statsManager = new StatsManager(statsFactory);
    	
		EventManager eventManager = new EventManager();

		/*List<Builder<Event>> eventBuilders = new ArrayList<Builder<Event>>();
		eventBuilders.add(new AddEntitiesEventBuilder());
		eventBuilders.add(new AddFoodDistributionEventBuilder());
		eventBuilders.add(new AddFoodGeneratorEventBuilder());
		eventBuilders.add(new AddRandomEntitiesGeneratorEventBuilder());
		eventBuilders.add(new AddFoodConditionGeneratorEventBuilder());
		eventBuilders.add(new AddRandomEntitiesConditionGeneratorEventBuilder());
		eventBuilders.add(new SaveSimulationEventBuilder());
		
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>(eventBuilders);*/
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>("eventsFactory");

		/*List<Builder<Entity>> entityBuilders = new ArrayList<Builder<Entity>>();
		entityBuilders.add(new SimpleRandomEntityBuilder());
		entityBuilders.add(new SimpleUPEntityBuilder());
		entityBuilders.add(new FoodEntityBuilder());
		entityBuilders.add(new MyIndividualBuilder());
		entityBuilders.add(new MyIndividual2Builder());

		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>(entityBuilders);*/
		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>("entitiesFactory");

		simulator = new EvoSimulator();simulator.setDebug(true);
		controller = new Controller(simulator, entityFactory, eventFactory, eventManager,statsManager);
		
		try {
			int op=0;
			if(op==0) {
				controller.loadEvents(new FileInputStream("resources/loads/events/eventstest1.json"));
				//controller.loadEntities(new FileInputStream("resources/loads/entities/test1.json"));
			}
			else {
				controller.loadEntities(new FileInputStream("resources/loads/simulations/filename.json"));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int op=1;
		if(op==0)controller.run(100000);
		else if(op==1){
			SwingUtilities.invokeLater(() -> {
				new LauncherGUI(controller).setVisible(true);
			});
		}
		
	}
}
