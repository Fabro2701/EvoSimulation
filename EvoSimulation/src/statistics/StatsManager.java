package statistics;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.model.EvoSimulator;
import statistics.models.PopulationAgeStats;
import statistics.models.PopulationCountStats;
import statistics.visualizers.AreaChartVisualizer;
import statistics.visualizers.LineChartVisualizer;

public class StatsManager extends JFrame implements StatsObserver{
 
    private List<StatsObserver> observers;
    private JPanel _panel;
    public StatsManager(){
    	
    	loadConfig("default");
    	this.setVisible(true);
    	
    }
    public void loadConfig(String filename) {
    	observers = new ArrayList<StatsObserver>();

    	_panel = new JPanel(new GridLayout(3, 2));
    	//_panel.add(new LineChartVisualizer(new PopulationAgeStats(this), "Age Population"));
    	//_panel.add(new AreaChartVisualizer(new PopulationCountStats(this), "Population status"));
    	this.setContentPane(_panel);
    	this.pack();
    }

	public void addObserver(StatsObserver observer) {
		observers.add(observer);
		observer.onRegister();
	}
	
	@Override
	public void onRegister() {}
	
	@Override 
	public void onUpdate(EvoSimulator simulator) {
		for(StatsObserver o:observers) {
			o.onUpdate(simulator);
		}
	}

}