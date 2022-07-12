import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import simulator.LauncherGUI;
import simulator.control.Controller;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.builders.Builder;
import simulator.factories.builders.SimpleRandomEntityBuilder;
import simulator.factories.builders.SimpleUPEntityBuilder;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;

public class Main {
	
	private static EvoSimulator simulator;
	private static Controller controller;
	
	public static void main(String args[]) {
		List<Builder<Entity>> builders = new ArrayList<Builder<Entity>>();
		builders.add(new SimpleRandomEntityBuilder());
		builders.add(new SimpleUPEntityBuilder());
		
		BuilderBasedFactory<Entity> factory = new BuilderBasedFactory<Entity>(builders);
		
		simulator = new EvoSimulator();
    	controller = new Controller(simulator,factory);
    	
    	try {
			controller.loadBodies(new FileInputStream("resources/loads/entities/test1.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		SwingUtilities.invokeLater(()->{new LauncherGUI(controller).setVisible(true);});
	}
}
