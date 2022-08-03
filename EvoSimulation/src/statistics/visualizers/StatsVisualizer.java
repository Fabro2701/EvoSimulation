package statistics.visualizers;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import statistics.StatsData;

public class StatsVisualizer{
	
	protected JPanel panel;
	
	public StatsVisualizer() {
		
	}
	public JPanel getPanel() {
		return this.panel;
	}

	
}
