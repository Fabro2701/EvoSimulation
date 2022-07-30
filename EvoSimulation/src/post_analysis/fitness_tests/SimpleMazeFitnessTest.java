package post_analysis.fitness_tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.builders.Builder;
import simulator.factories.builders.entity.FoodEntityBuilder;
import simulator.factories.builders.entity.MyIndividualBuilder;
import simulator.factories.builders.entity.SimpleRandomEntityBuilder;
import simulator.factories.builders.entity.SimpleUPEntityBuilder;
import simulator.factories.builders.events.AddEntitiesEventBuilder;
import simulator.factories.builders.events.AddFoodDistributionEventBuilder;
import simulator.factories.builders.events.AddFoodGeneratorEventBuilder;
import simulator.factories.builders.events.AddRandomEntitiesGeneratorEventBuilder;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import util.Pair;

public class SimpleMazeFitnessTest extends AbstractFitnessTest{

	@Override
	public Pair<Integer, Float> evaluate(Entity e) {
		Pair<Integer, Float> fitness = new Pair<Integer, Float>(0,0.f);
		EventManager eventManager = new EventManager();

		List<Builder<Event>> eventBuilders = new ArrayList<Builder<Event>>();
		eventBuilders.add(new AddEntitiesEventBuilder());
		eventBuilders.add(new AddFoodDistributionEventBuilder());
		eventBuilders.add(new AddFoodGeneratorEventBuilder());
		eventBuilders.add(new AddRandomEntitiesGeneratorEventBuilder());

		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>(eventBuilders);

		List<Builder<Entity>> entityBuilders = new ArrayList<Builder<Entity>>();
		entityBuilders.add(new SimpleRandomEntityBuilder());
		entityBuilders.add(new SimpleUPEntityBuilder());
		entityBuilders.add(new FoodEntityBuilder());
		entityBuilders.add(new MyIndividualBuilder());

		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>(entityBuilders);

		EvoSimulator simulator = new EvoSimulator();
		Controller ctrl = new Controller(simulator, entityFactory, eventFactory, eventManager);
		
		try {
			ctrl.loadEvents(new FileInputStream("resources/loads/events/simpleMazeEvents.json"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int timeLimit=1000;
		while(true) {
			ctrl.run(1);
			
			if(simulator.getTime()>=timeLimit) {
				break;
			}
		}
		return fitness;
	}

}
