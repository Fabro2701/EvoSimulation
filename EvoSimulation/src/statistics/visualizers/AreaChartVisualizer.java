package statistics.visualizers;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;

import statistics.StatsData;

public class AreaChartVisualizer extends StatsVisualizer{

	public AreaChartVisualizer(StatsData data, String name) {
		super(data);
		chart = ChartFactory.createAreaChart(  
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
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(0,screen.height-300);
		//this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}