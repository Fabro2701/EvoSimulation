package statistics.models;

import java.io.FileWriter;
import java.io.PrintWriter;
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
public class AttributeStats extends StatsData{

	private int currentTime=0;
	boolean groupby;
	String att;
	
	public AttributeStats(int updateRate, String att, boolean groupby) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
		this.groupby = groupby;
		this.att = att;
		this.serialize = false;
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			if(this.groupby) {
				List<Entity>listTotal = simulator.getEntities().stream().filter(e->e instanceof MyIndividual).collect(Collectors.toList());
				Map<Object, List<Entity>>list = listTotal.stream().collect(Collectors.groupingBy(Entity.groupF));
				
				for(Object id:list.keySet()) {
					long count = list.get(id).stream().filter((Entity m)->m.hasAttribute(this.att)&&(Boolean)m.getAttribute(this.att)).count();
					((DefaultCategoryDataset)dataset).addValue(count, (String)id, Integer.valueOf(currentTime));
				}
			}
			else {
				long count = simulator.getEntities().stream().filter(e->e instanceof MyIndividual&&e.hasAttribute(this.att)&&(Boolean)e.getAttribute(this.att)).count();				
				((DefaultCategoryDataset)dataset).addValue(count, MyIndividual.class.getSimpleName(), Integer.valueOf(currentTime));
				
			}

		}
	}

}
