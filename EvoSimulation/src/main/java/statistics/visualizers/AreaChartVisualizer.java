package statistics.visualizers;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

import statistics.StatsData;

public class AreaChartVisualizer extends StatsVisualizer{

	public AreaChartVisualizer(StatsData data, String name, String x, String y) {
		JFreeChart chart = ChartFactory.createAreaChart(  
		        name, 
		        x, 
		        y,  
		        (CategoryDataset) data.getDataSet()  
		        ); 
		this.panel = new ChartPanel(chart);  
	}
}