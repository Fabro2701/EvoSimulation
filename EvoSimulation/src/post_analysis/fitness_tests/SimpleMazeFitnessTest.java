package post_analysis.fitness_tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

import org.json.JSONArray;
import org.json.JSONObject;

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
import simulator.view.viewer.AbstractViewer;
import simulator.view.viewer.Viewer;
import util.Pair;

public class SimpleMazeFitnessTest extends AbstractFitnessTest{

	public SimpleMazeFitnessTest(JScrollPane viewPane) {
		super(viewPane);
		// TODO Auto-generated constructor stub
	}
	public SimpleMazeFitnessTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int evaluate(JSONObject o) {
		int fitness = 0;
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

		EvoSimulator simulator = new EvoSimulator("simpleMaze1");
		Controller ctrl = new Controller(simulator, entityFactory, eventFactory, eventManager);
		

		
		try {
			ctrl.loadEvents(new FileInputStream("resources/loads/events/simpleMazeEvents.json"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		o.getJSONObject("data").put("id","-1");
		o.getJSONObject("data").put("x",0);
		o.getJSONObject("data").put("y",0);
		ctrl.loadEntities(new JSONArray().put(o));
		Entity entity = simulator.getEntity("-1");
		/*AbstractViewer viewer = new Viewer(ctrl,700,700);
		viewer.activate();
		viewPane.setViewportView(viewer);
		viewPane.repaint();*/
		
		int timeLimit=100;
		ctrl.run(timeLimit);
	
		
		/*new Thread() {
        	public void run() {
        		while(true) {
        			ctrl.run(1);
        			
        			if(simulator.getTime()>=timeLimit) {
        				break;
        			}
        		}
        	}
        }.start();*/
		fitness = entity.getAge();
		return fitness;
	}

}
