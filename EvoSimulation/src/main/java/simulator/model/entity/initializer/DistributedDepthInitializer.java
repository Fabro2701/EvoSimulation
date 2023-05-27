package simulator.model.entity.initializer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.model.entity.Entity;

public class DistributedDepthInitializer extends AbstractInitializer{
	public List<Entity>createPopulation(AbstractGrammar grammar, Controller ctrl, int num, JSONObject properties){
		List<Entity>population = new ArrayList<Entity>();
		
		JSONArray distributions = properties.getJSONArray("distributions");
		for(int i=0;i<distributions.length();i++) {
			JSONObject distribution = distributions.getJSONObject(i);
			int pctg = distribution.getInt("pctg");
			int depth = distribution.getInt("depth");
			
		}
		
		return population;
	}



	@Override
	public List<Entity> createPopulation(AbstractGrammar grammar, Controller ctrl, int num, JSONObject properties,
			String clazz) {
		// TODO Auto-generated method stub
		return null;
	}
}
