package statistics.models;

import java.util.Comparator;

import javax.swing.JLabel;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import statistics.StatsData;

public class BestIndividualCodeStats extends StatsData{

	private int currentTime=0;
	
	public BestIndividualCodeStats(int updateRate, boolean serialize) {
		super(updateRate, serialize);
		this.text = new JLabel();
	}
	
	@Override
	public void onRegister() {
		super.onRegister();
	}
	double maxv=0d;
	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			//Entity entity = simulator.getEntities().stream().filter(e->e instanceof GIndividual).max(Comparator.comparing((Entity en)->(Double)en.getAttribute("life"))).get();
			Entity entity = simulator.getEntities().stream().filter(e->e instanceof GIndividual).max(Comparator.comparing((Entity e)->(Double)e.getAttribute("life"))).orElseGet(null);
			if(entity==null)return;
			double life = (Double)entity.getAttribute("life");
			this.text.setText("<html>" + ((GIndividual)entity).getPhenotype().getVisualCode() .replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");
			if(serialize) {
				if(life>maxv) {
					maxv=life;
					this.fileWriter.print("---------");
					this.fileWriter.printf("%s:\n %f\n %s\n", currentTime,entity.getAttribute("life"), ((GIndividual)entity).getPhenotype().getVisualCode());
					this.fileWriter.print("---------");
				}
			}
			this.text.repaint();
		
		}
	}

	
}
