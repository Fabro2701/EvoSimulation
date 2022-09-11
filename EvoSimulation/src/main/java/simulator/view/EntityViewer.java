package simulator.view;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.JSONObject;

import post_analysis.fitness_tests.AbstractFitnessTest;
import post_analysis.fitness_tests.SimpleMazeFitnessTest;
import post_analysis.fitness_tests.SimpleMazeFitnessTest2;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;

public class EntityViewer{
	private JLabel codeComponent;
	private JTable fitnessScoresComponent;
	private JScrollPane experimentsComponent;
	private Entity e;
	private JSONObject e_j;
	private HashMap<String,AbstractFitnessTest>experimentsMap;
	public EntityViewer(JScrollPane experimentsComponent) {
		this.experimentsComponent = experimentsComponent;
		
		codeComponent = new JLabel();
		
		String header[] = new String[] {"Test","Score"};
		Object data[][] = new Object[][] {{"SimpleMaze",0},{"SimpleMaze2",0}};
		fitnessScoresComponent = new JTable(data,header);
		
		experimentsMap = new HashMap<String,AbstractFitnessTest>();
		experimentsMap.put("SimpleMazeTest", new SimpleMazeFitnessTest(experimentsComponent));
		experimentsMap.put("SimpleMazeTest2", new SimpleMazeFitnessTest2(experimentsComponent));
		
		
	}
	public void setEntity(Entity e) {
		this.e=e;
		codeComponent.setText("<html>" + ((MyIndividual)e).getPhenotype().getVisualCode().replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");
		codeComponent.repaint();
		
		fitnessScoresComponent.getModel().setValueAt(new SimpleMazeFitnessTest(10000).evaluate(e.toJSON()), 0, 1);
		fitnessScoresComponent.getModel().setValueAt(new SimpleMazeFitnessTest2(10000).evaluate(e.toJSON()), 1, 1);
	}
	public void setEntity(JSONObject e) {
		this.e_j=e;
		codeComponent.setText("<html>" + e.getJSONObject("data").getJSONObject("phenotype").getString("code").replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");
		codeComponent.repaint();
		
		//fitnessScoresComponent.getModel().setValueAt(new SimpleMazeFitnessTest(10000).evaluate(e), 0, 1);
		//fitnessScoresComponent.getModel().setValueAt(new SimpleMazeFitnessTest2(10000).evaluate(e), 1, 1);
	}
	public void runExperiment(String test) {
		if(e_j==null)experimentsMap.get(test).evaluate(e.toJSON());
		else experimentsMap.get(test).evaluate(e_j);
	}
	public JLabel getCodeComponent() {
		return codeComponent;
	}
	public JTable geFitnessScoresComponent() {
		return fitnessScoresComponent;
	}
}
