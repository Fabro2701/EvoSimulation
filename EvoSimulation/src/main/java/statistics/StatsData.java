package statistics;

import javax.swing.JLabel;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

public abstract class StatsData implements StatsObserver{
	protected AbstractDataset dataset;  
	protected JLabel text;  
	protected int updateRate;
	
	public StatsData(int updateRate) {
		this.updateRate = updateRate;
		//manager.addObserver(this);
	}
	
	public AbstractDataset getDataSet() {return dataset;}
	public JLabel getText() {return text;}

	@Override
	public void clear() {
		((DefaultCategoryDataset)dataset).clear();
		text.setText("");
	}
}
