package statistics.visualizers;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import statistics.StatsData;

public class StatsVisualizer extends JFrame{
	
	private StatsData data;
	protected JFreeChart chart;
	
	public StatsVisualizer(StatsData data) {
		this.data=data;
		this.data.addVisualizer(this);
		
	}
	public void update() {
		 //this.repaint();
			
	}
	
}
