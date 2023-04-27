package statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JLabel;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;

import simulator.model.EvoSimulator;

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
	protected boolean serialize;
	
	protected int currentTime=0;
	private int clear;
	
	public StatsData(int updateRate, boolean serialize, int clear) {
		this.updateRate = updateRate;
		this.serialize = serialize;
		this.clear = clear;
	}
	public StatsData(int updateRate, boolean serialize) {
		this(updateRate, serialize,-1);
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
	@Override 
	public void onStep(EvoSimulator simulator) {
		if(clear!=-1&&currentTime%clear==0)clear();
	}
	public AbstractDataset getDataSet() {return dataset;}
	public JLabel getText() {return text;}

	@Override
	public void clear() {
		if(dataset!=null)((DefaultCategoryDataset)dataset).clear();
		if(text!=null)text.setText("");
	}
}
