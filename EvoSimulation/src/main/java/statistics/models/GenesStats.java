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

public class GenesStats extends StatsData{

	private int currentTime=0;
	Map<String, List<Integer>> rules;
	int op = 1;
	public GenesStats(int updateRate) {
		super(updateRate);
		dataset = new DefaultCategoryDataset();
		
		this.rules = GIndividual.Genes.getRules();
	}
	
	@Override
	public void onRegister() {
		super.onRegister();
	}

	@Override 
	public void onStep(EvoSimulator simulator) {
		currentTime=simulator.getTime();
		if(currentTime%updateRate==0) {
			if(op==0) {
				for(String gen:rules.keySet()) {
					List<Entity>listTotal = simulator.getEntities().stream().filter(e->e instanceof MyIndividual && ((MyIndividual)e).getPhenotype().hasGene(gen)).collect(Collectors.toList());
					Map<Object, List<Entity>>list = listTotal.stream().collect(Collectors.groupingBy(e->e.getAttribute("imc")));
					long total = listTotal.size(); 
					for(Object id:list.keySet()) {
						((DefaultCategoryDataset)dataset).addValue(100.0*list.get(id).size()/total, (String)id, gen);
						if(this.serialize) {
							this.fileWriter.printf("%d %s %s %f\n", currentTime, gen, id, 100.0*list.get(id).size()/total);
						}
					}
				}
			}
			else if(op==1) {
				Map<Object, List<Entity>>list = simulator.getEntities().stream().filter(e->e instanceof MyIndividual).collect(Collectors.groupingBy(e->e.getAttribute("imc")));
				for(Object id:list.keySet()) {
					int total = list.get(id).size();
					for(String gen:rules.keySet()) {
						long count = list.get(id).stream().filter(e->((MyIndividual)e).getPhenotype().hasGene(gen)).count();
						((DefaultCategoryDataset)dataset).addValue(100.0*count/total, gen,(String)id);
						if(this.serialize) {
							this.fileWriter.printf("%d %s %s %f\n", currentTime, id, gen, 100.0*count/total);
						}
					}

				}
			}
			
		}
	}

}
