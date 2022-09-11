package simulator.events.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;

public class SaveSimulationEvent extends MultipleTimeEvent{
	String filename;
	boolean replacement;
	int cont;
	public SaveSimulationEvent(int executionTime, int times, int interval, String filename, boolean replacement) {
		super(executionTime, times, interval);
		this.filename = filename;
		this.replacement = replacement;
		cont=0;
	}

	@Override
	protected void _execute(Controller ctrl) {
		File file = null;
		if(replacement) {
			file = new File("resources/loads/simulations/"+filename+".json");
		}
		else {
			file = new File("resources/loads/simulations/"+filename+cont+".json");
			cont++;
		}
		try {
        	JSONObject o = ctrl.getSimulatorJSON();
			PrintWriter out = new PrintWriter(new FileWriter(file));
			out.write(o.toString());
			out.close();
			System.out.println("Simulation saved in: "+file.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
