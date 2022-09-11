package statistics.visualizers;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;

import statistics.StatsData;

public class BoxChartVisualizer extends StatsVisualizer implements ChartI{

	public BoxChartVisualizer(StatsData data, String name, String x, String y) {
		JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(  
		        name, 
		        x, 
		        y,  
		        (DefaultBoxAndWhiskerXYDataset)data.getDataSet(),
		        false
		        ); 
	
		this.panel = new ChartPanel(chart); 
		
	
	}
}