package statistics.visualizers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import statistics.StatsData;

public class BarChartVisualizer extends StatsVisualizer{

	public BarChartVisualizer(StatsData data, String name, String x, String y) {
		JFreeChart chart=ChartFactory.createBarChart(  
				name, 
		        x, 
		        y, 
		        data.getDataSet(),  
		        PlotOrientation.VERTICAL,  
		        true,true,false  
		       );  
		this.panel = new ChartPanel(chart);  

	}

}
