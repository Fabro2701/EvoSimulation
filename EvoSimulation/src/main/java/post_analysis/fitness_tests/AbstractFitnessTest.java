package post_analysis.fitness_tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import statistics.StatsManager;

public abstract class AbstractFitnessTest {
	protected JScrollPane viewPane;
	Entity entity;
	EvoSimulator simulator;
	Controller ctrl;
	public AbstractFitnessTest(JScrollPane viewPane) {
		this();
		this.viewPane=viewPane;
	}
	public AbstractFitnessTest() {
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>("eventsFactory");
		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>("entitiesFactory");
		EventManager eventManager = new EventManager();
		simulator = new EvoSimulator("simpleMaze1");
		ctrl = new Controller(simulator, entityFactory, eventFactory, eventManager, new StatsManager());
		
		try {
			ctrl.loadEvents(new FileInputStream("resources/loads/events/simpleMazeEvents.json"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public abstract int evaluate();
	public abstract int evaluate(JSONObject o);
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
	public float getResult() {
		return this.entity.getAge();
	}
	public void reset() {
		this.ctrl.reset();
		
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
		List<Entity>ents = new ArrayList<Entity>();ents.add(entity);
		this.ctrl.loadEntities(ents);
	}
	public Controller getCtrl() {
		return ctrl;
	}
	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}
}
