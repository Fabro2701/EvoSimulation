package statistics.models;

import java.util.Comparator;

import javax.swing.JLabel;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import statistics.StatsData;

public class BestIndividualCodeStats extends StatsData{

	private int currentTime=0;
	
	public BestIndividualCodeStats(int updateRate) {
		super(updateRate);
		this.text = new JLabel();
	}
	
	@Override
	public void onRegister() {
	}

	@Override 
	public void onUpdate(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			Entity entity = simulator.getEntities().stream().filter(e->e instanceof GIndividual).max(Comparator.comparing(Entity::getAge)).get();
			this.text.setText("<html>" + ((GIndividual)entity).getPhenotype().getVisualCode() .replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");

			this.text.repaint();
		
		}
	}
	
}
