package post_analysis.fitness_tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

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
import statistics.StatsManager;


public class SimpleMazeFitnessTest2 extends AbstractFitnessTest{
	int timeLimit;
	AbstractViewer viewer;
	public SimpleMazeFitnessTest2(JScrollPane viewPane) {
		super(viewPane);
		this.timeLimit = 1000;
		// TODO Auto-generated constructor stub
	}
	public SimpleMazeFitnessTest2() {
		this(100);
		// TODO Auto-generated constructor stub
	}
	public SimpleMazeFitnessTest2(int timeLimit) {
		super();
		this.timeLimit = timeLimit;
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

		EvoSimulator simulator = new EvoSimulator("simpleMaze2");
		Controller ctrl = new Controller(simulator, entityFactory, eventFactory, eventManager, new StatsManager());
		
		
		try {
			ctrl.loadEvents(new FileInputStream("resources/loads/events/simpleMazeEvents2.json"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		o.getJSONObject("data").put("id","-1");
		o.getJSONObject("data").put("x",0);
		o.getJSONObject("data").put("y",0);
		o.getJSONObject("data").put("energy",100.f);
		ctrl.loadEntities(new JSONArray().put(o));
		entity = simulator.getEntity("-1");
		if(viewPane!=null) {
			viewer = new Viewer(ctrl,700,700);
			viewer.activate();
			viewPane.setViewportView(viewer);
			viewer.repaint();
        	this.runCtrl(ctrl, this.timeLimit);
	        			
		}
		else {
			
			ctrl.run(timeLimit);
			fitness = entity.getAge();
		}

		
		return fitness;
	}
	@Override
	public int evaluate() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
