package statistics.visualizers;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
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
		chart.getPlot().setBackgroundPaint(Color.white);
		
		this.panel = new ChartPanel(chart);  
		
		//((ChartPanel)this.panel).setBackground(Color.red);
	}

}
