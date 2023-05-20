package statistics.models;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.entity.individuals.MyIndividual;
import statistics.StatsData;

/**
 * 
 * @author Fabrizio Ortega
 *
 */
public class NumericAttributeStats extends StatsData{

	private int currentTime=0;
	boolean groupby;
	String att;
	
	public NumericAttributeStats(int updateRate, boolean serialize, String att) {
		super(updateRate, serialize);
		dataset = new DefaultCategoryDataset();

		this.att = att;
		this.serialize = false;
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			
			
			DoubleSummaryStatistics stats = simulator.getEntities().stream().filter(e->e instanceof MyIndividual&&e.hasAttribute(this.att)).mapToDouble((Entity e)->(Double)e.getAttribute(att)).summaryStatistics();
			((DefaultCategoryDataset)dataset).addValue(stats.getAverage(), "avg", Integer.valueOf(currentTime));
				
			((DefaultCategoryDataset)dataset).addValue(stats.getMax(), "max", Integer.valueOf(currentTime));

		}
	}

}
