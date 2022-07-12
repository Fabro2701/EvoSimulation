import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import simulator.LauncherGUI;
import simulator.control.Controller;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.SimpleRandomEntity;

public class Main {
	
	private static EvoSimulator simulator;
	private static Controller controller;
	
	public static void main(String args[]) {
		List<Entity.Builder> builders = new ArrayList<Entity.Builder>();
		builders.add(new SimpleRandomEntity.Builder());
		BuilderBasedFactory factory = new BuilderBasedFactory(builders);
		
		simulator = new EvoSimulator();
    	controller = new Controller(simulator,factory);
    	
    	try {
			controller.loadBodies(new FileInputStream("resources/loads/entities/test1.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		SwingUtilities.invokeLater(()->{new LauncherGUI(controller);});
	}
}
