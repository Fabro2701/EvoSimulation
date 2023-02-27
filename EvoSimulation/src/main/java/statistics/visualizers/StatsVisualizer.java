package statistics.visualizers;

import javax.swing.JComponent;

/**
 * 
 * @author Fabrizio Ortega
 *
 */
public abstract class StatsVisualizer{
	
	protected JComponent panel;
	
	public StatsVisualizer() {
		
	}
	public JComponent getPanel() {
		return this.panel;
	}

	
}
