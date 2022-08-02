package simulator.view;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import post_analysis.fitness_tests.AbstractFitnessTest;
import post_analysis.fitness_tests.SimpleMazeFitnessTest;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;

public class EntityViewer{
	private JLabel codeComponent;
	private JTable fitnessScoresComponent;
	private JScrollPane experimentsComponent;
	private Entity e;
	private HashMap<String,AbstractFitnessTest>experimentsMap;
	public EntityViewer(JScrollPane experimentsComponent) {
		codeComponent = new JLabel();
		
		String header[] = new String[] {"Test","Score"};
		Object data[][] = new Object[][] {{"SimpleMaze",0},{"X",0}};
		fitnessScoresComponent = new JTable(data,header);
		
		experimentsMap = new HashMap<String,AbstractFitnessTest>();
		experimentsMap.put("SimpleMazeTest", new SimpleMazeFitnessTest(experimentsComponent));
		
		this.experimentsComponent = experimentsComponent;
	}
	public void setEntity(Entity e) {
		this.e=e;
		codeComponent.setText("<html>" + ((MyIndividual)e).getPhenotype().getVisualCode().replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");
		codeComponent.repaint();
		
		fitnessScoresComponent.getModel().setValueAt(new SimpleMazeFitnessTest(10000).evaluate(e.toJSON()), 0, 1);
	}
	public void runExperiment(String test) {
			experimentsMap.get(test).evaluate(e.toJSON());
			
		
	}
	public JLabel getCodeComponent() {
		return codeComponent;
	}
	public JTable geFitnessScoresComponent() {
		return fitnessScoresComponent;
	}
}
