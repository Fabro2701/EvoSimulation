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


public class SimpleMazeFitnessTest extends AbstractFitnessTest{
	int timeLimit;
	AbstractViewer viewer;
	Entity entity;
	public SimpleMazeFitnessTest(JScrollPane viewPane) {
		super(viewPane);
		this.timeLimit = 1000;
		// TODO Auto-generated constructor stub
	}
	public SimpleMazeFitnessTest() {
		this(100);
		// TODO Auto-generated constructor stub
	}
	public SimpleMazeFitnessTest(int timeLimit) {
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
		
		return fitness;
	}
	public void runCtrl(Controller ctrl, int n) {
		if ( n>0 && this.entity.isAlive()) {
	         try {
	        	 ctrl.run(1);
	         } catch (Exception e) {
	        	 e.printStackTrace();
	             return;
	         }
	         SwingUtilities.invokeLater( new Runnable() {
	        	@Override
	     		public void run() {
	        		runCtrl(ctrl, n-1);
	     		}
	         });
	   } 
	}

}
