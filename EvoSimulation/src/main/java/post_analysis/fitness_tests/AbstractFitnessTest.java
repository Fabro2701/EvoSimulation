package post_analysis.fitness_tests;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;

public abstract class AbstractFitnessTest {
	protected JScrollPane viewPane;
	Entity entity;
	public AbstractFitnessTest(JScrollPane viewPane) {
		this.viewPane=viewPane;
	}
	public AbstractFitnessTest() {
	}
	public abstract int evaluate(JSONObject o);
	public void runCtrl(Controller ctrl, int n) {
		if ( n>0 && this.entity.isAlive()) {
	         try {
	        	 ctrl.run(1);
	         } catch (Exception e) {
	        	 e.printStackTrace();
	             return;
	         }
	         SwingUtilities.invokeLater( new Runnable() {
	        	@Override
	     		public void run() {
	        		runCtrl(ctrl, n-1);
	     		}
	         });
	   } 
	}
}
