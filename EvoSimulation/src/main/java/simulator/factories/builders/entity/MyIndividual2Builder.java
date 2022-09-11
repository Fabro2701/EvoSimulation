package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual2;

public class MyIndividual2Builder extends EntityBuilder{
	public MyIndividual2Builder() {
		type="mi2";
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		if(o.has("genotype")) { 
			return new MyIndividual2(o.getString("id"),
									ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
									o.getJSONObject("genotype"),
									o.getJSONObject("phenotype"),
									o.getFloat("energy"),ctrl);
		}
		return new MyIndividual2(o.getString("id"),ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),ctrl);
	}
}
