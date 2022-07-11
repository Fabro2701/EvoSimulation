package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

import statistics.visualizers.StatsVisualizer;

public abstract class StatsData implements StatsObserver{
	protected HashMap<String, ArrayList<Integer>> linearData;
	protected DefaultCategoryDataset dataset;  
	protected List<StatsVisualizer>visualizers;
	
	public StatsData() {
		StatsManager.getInstance().addObserver(this);
		dataset = new DefaultCategoryDataset();
		visualizers = new ArrayList<StatsVisualizer>();
	}
	public abstract HashMap<String,ArrayList<Integer>> getLinearData();
	public void addVisualizer(StatsVisualizer visualizer) {
		visualizers.add(visualizer);
	}
	public DefaultCategoryDataset getDataSet() {return dataset;}
}
