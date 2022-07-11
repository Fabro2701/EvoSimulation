package statistics.visualizers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import statistics.StatsData;

public class BarChartVisualizer extends StatsVisualizer{

	public BarChartVisualizer(StatsData data, String name) {
		super(data);
		JFreeChart chart=ChartFactory.createBarChart(  
				name, //Chart Title  
		        "", // Category axis  
		        "Population", // Value axis  
		        data.getDataSet(),  
		        PlotOrientation.VERTICAL,  
		        true,true,false  
		       );  
		ChartPanel panel = new ChartPanel(chart);  
	    setContentPane(panel);	
		//initComponents();
		this.pack();
	    this.setSize(300, 300);  
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
