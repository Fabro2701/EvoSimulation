package statistics.models;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.jfree.data.category.DefaultCategoryDataset;

import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.GIndividual;
import simulator.model.entity.individuals.MyIndividual;
import simulator.model.entity.individuals.genome.PolymorphismController;
import simulator.model.entity.individuals.genome.PolymorphismController.VARIATION;
import statistics.StatsData;

/**
 * 
 * @author Fabrizio Ortega
 *
 */
public class PolymorphismStats extends StatsData{

	private int currentTime=0;
	String poly;
	public PolymorphismStats(int updateRate, boolean serialize, String poly) {
		super(updateRate, serialize);
		dataset = new DefaultCategoryDataset();	
		this.poly = poly;
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			Map<String, List<VARIATION>> polys = PolymorphismController.getVariations();
			int total = (int) simulator.getEntities().stream().filter(e->e instanceof MyIndividual).count();
			Map<Object, List<Entity>>list = simulator.getEntities().stream().filter(e->e instanceof MyIndividual).collect(Collectors.groupingBy(Entity.groupF));
			for(Object id:list.keySet()) {
				//int total = list.get(id).size();
				for(VARIATION v:polys.get(poly)) {
					long count = list.get(id).stream().filter(e->((MyIndividual)e).getPhenotype().getVariation(poly).equals(v)).count();
					((DefaultCategoryDataset)dataset).addValue(100.0*count/total, v.toString(),(String)id);
				}
			}
		}
	}

}
