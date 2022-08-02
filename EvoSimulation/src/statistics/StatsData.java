package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

import statistics.visualizers.StatsVisualizer;

public abstract class StatsData implements StatsObserver{
	protected DefaultCategoryDataset dataset;  
	protected List<StatsVisualizer>visualizers;
	
	public StatsData(StatsManager manager) {
		manager.addObserver(this);
		dataset = new DefaultCategoryDataset();
		visualizers = new ArrayList<StatsVisualizer>();
	}
	public void addVisualizer(StatsVisualizer visualizer) {
		visualizers.add(visualizer);
	}
	public DefaultCategoryDataset getDataSet() {return dataset;}
}
