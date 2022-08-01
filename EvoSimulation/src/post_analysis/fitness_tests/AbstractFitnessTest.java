package post_analysis.fitness_tests;

import javax.swing.JScrollPane;

import org.json.JSONObject;

import util.Pair;

public abstract class AbstractFitnessTest {
	protected JScrollPane viewPane;
	public AbstractFitnessTest(JScrollPane viewPane) {
		this.viewPane=viewPane;
	}
	public AbstractFitnessTest() {
	}
	public abstract int evaluate(JSONObject o);
}
