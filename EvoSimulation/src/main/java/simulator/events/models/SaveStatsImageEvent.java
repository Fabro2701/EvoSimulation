package simulator.events.models;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import simulator.control.Controller;
import simulator.events.MultipleTimeEvent;
import statistics.StatsManager;

public class SaveStatsImageEvent extends MultipleTimeEvent{
	String filename;
	boolean replacement;
	int cont;
	public SaveStatsImageEvent(int executionTime, int times, int interval, String filename, boolean replacement) {
		super(executionTime, times, interval);
		this.filename = filename;
		this.replacement = replacement;
		cont=0;
	}

	@Override
	protected void _execute(Controller ctrl) {
		List<StatsManager> managers = ctrl.getStatsManagers();
		for(int i=0;i<managers.size();i++) {
			File file = null;
			if(replacement) {
				file = new File("resources/loads/simulations/stats/"+filename+i+".jpeg");
			}
			else {
				file = new File("resources/loads/simulations/stats/"+filename+i+"_"+cont+".jpeg");
				cont++;
			}
			try {
				JFrame frame = ctrl.getStatsManagers().get(i);
	            BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
	            Graphics2D graphics2D = image.createGraphics();
	            frame.paint(graphics2D);
	            ImageIO.write(image,"jpeg", file);
		        
				System.out.println("Stats saved in: "+file.getName());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
