package statistics.visualizers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;

import statistics.StatsData;

public class LineChartVisualizer extends StatsVisualizer{

	public LineChartVisualizer(StatsData data, String name) {
		super(data);
		chart = ChartFactory.createLineChart(  
		        name, 
		        "Time", 
		        "Number Population",  
		        data.getDataSet()  
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
