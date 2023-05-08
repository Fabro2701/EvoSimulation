package statistics.models;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
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
			/*Map<String, List<VARIATION>> polys = PolymorphismController.getVariations();
			int total = (int) simulator.getEntities().stream().filter(e->e instanceof MyIndividual).count();
			Map<Object, List<Entity>>list = simulator.getEntities().stream().filter(e->e instanceof MyIndividual).collect(Collectors.groupingBy(Entity.groupF));
			for(Object id:list.keySet()) {
				int totalGroup = list.get(id).size();
				for(VARIATION v:polys.get(poly)) {
					long count = list.get(id).stream().filter(e->((MyIndividual)e).getPhenotype().getVariation(poly).equals(v)).count();
					((DefaultCategoryDataset)dataset).addValue(100.0*count/totalGroup, v.toString(),(String)id);
				}
			}*/
			
			Map<String, List<VARIATION>> polys = PolymorphismController.getVariations();

			for(VARIATION v:polys.get(poly)) {

				List<Entity>list = simulator.getEntities().stream().filter(e->e instanceof MyIndividual&&((MyIndividual)e).getPhenotype().getVariation(poly).equals(v)).collect(Collectors.toList());
				int total = list.size();

				Map<Object, List<Entity>> glist = list.stream().collect(Collectors.groupingBy(Entity.groupF));

				for(Object id:glist.keySet()) {
					long count = glist.get(id).size();
					((DefaultCategoryDataset)dataset).addValue(100.0*count/total,(String)id, v.toString());
					
				}
			}
			
		}
	}

}
