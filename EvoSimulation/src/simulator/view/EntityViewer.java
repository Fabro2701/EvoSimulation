package simulator.view;

import javax.swing.JLabel;
import javax.swing.JTable;

import post_analysis.fitness_tests.SimpleMazeFitnessTest;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;

public class EntityViewer{
	private JLabel codeComponent;
	private JTable fitnessScoresComponent;
	
	public EntityViewer() {
		codeComponent = new JLabel();
		
		String header[] = new String[] {"Test","Score"};
		Object data[][] = new Object[][] {{"SimpleMaze",0},{"X",0}};
		fitnessScoresComponent = new JTable(data,header);
	}
	public void setEntity(Entity e) {
		((JLabel)codeComponent).setText("<html>" + ((MyIndividual)e).getPhenotype().getVisualCode().replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");
		codeComponent.repaint();
		
		fitnessScoresComponent.getModel().setValueAt(new SimpleMazeFitnessTest(1000).evaluate(e.toJSON()), 0, 1);
	}
	public JLabel getCodeComponent() {
		return codeComponent;
	}
	public JTable geFitnessScoresComponent() {
		return fitnessScoresComponent;
	}
}
