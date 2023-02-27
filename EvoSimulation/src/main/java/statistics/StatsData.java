package statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JLabel;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

/**
 * StatsData handles all {@link StatsObserver} events
 * @author Fabrizio Ortega
 *
 */
public abstract class StatsData implements StatsObserver{
	protected AbstractDataset dataset;  
	protected JLabel text;  
	protected int updateRate;
	
	protected PrintWriter fileWriter;
	protected boolean serialize = true;
	
	public StatsData(int updateRate) {
		this.updateRate = updateRate;
	}
	@Override
	public void onRegister() {
		if(serialize)
			try {
				this.fileWriter = new PrintWriter(new FileWriter("resources/loads/simulations/stats_text/"+this.getClass().getName()+".txt"),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public AbstractDataset getDataSet() {return dataset;}
	public JLabel getText() {return text;}

	@Override
	public void clear() {
		if(dataset!=null)((DefaultCategoryDataset)dataset).clear();
		if(text!=null)text.setText("");
	}
}
