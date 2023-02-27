package statistics.visualizers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

import statistics.StatsData;

public class LineChartVisualizer extends StatsVisualizer{

	public LineChartVisualizer(StatsData data, String name, String x, String y) {
		JFreeChart chart = ChartFactory.createLineChart(  
		        name, 
		        x, 
		        y,  
		        (CategoryDataset) data.getDataSet()  
		        ); 
		this.panel = new ChartPanel(chart);  

	}

}
