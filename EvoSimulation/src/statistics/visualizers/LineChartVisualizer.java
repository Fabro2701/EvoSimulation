package statistics.visualizers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import statistics.StatsData;

public class LineChartVisualizer extends StatsVisualizer{

	public LineChartVisualizer(StatsData data, String name, String x, String y) {
		JFreeChart chart = ChartFactory.createLineChart(  
		        name, 
		        x, 
		        y,  
		        data.getDataSet()  
		        ); 
		this.panel = new ChartPanel(chart);  

	}

}
