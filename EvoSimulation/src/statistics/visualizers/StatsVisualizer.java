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

	private void initComponents() {
		DefaultCategoryDataset dataset = createDataset();  
	    // Create chart  
	    JFreeChart chart = ChartFactory.createLineChart(  
	        "Site Traffic", // Chart title  
	        "Date", // X-Axis Label  
	        "Number of Visitor", // Y-Axis Label  
	        dataset  
	        );  
		ChartPanel panel = new ChartPanel(chart);  
	    setContentPane(panel);		
	}

	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for(String key:data.getLinearData().keySet()) {
			if(!key.equals("time")) {
				for(int i=0;i<data.getLinearData().get(key).size();i++) {
					dataset.addValue(data.getLinearData().get(key).get(i), 
								     key, 
								     data.getLinearData().get("time").get(i)
								     );
				}
			}
		}
		
		return dataset;
	}

	public void update() {
		 //this.repaint();
			
	}
	
}
