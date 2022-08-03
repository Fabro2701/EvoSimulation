package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

import statistics.visualizers.StatsVisualizer;

public abstract class StatsData implements StatsObserver{
	protected DefaultCategoryDataset dataset;  
	protected int updateRate;
	
	public StatsData(int updateRate) {
		this.updateRate = updateRate;
		//manager.addObserver(this);
		dataset = new DefaultCategoryDataset();
	}
	
	public DefaultCategoryDataset getDataSet() {return dataset;}
}
