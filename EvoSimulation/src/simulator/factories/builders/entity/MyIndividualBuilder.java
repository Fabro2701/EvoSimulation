package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;

public class MyIndividualBuilder extends EntityBuilder{
	public MyIndividualBuilder() {
		type="mi";
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		if(o.has("genotype")) { 
			return new MyIndividual(o.getString("id"),
									ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
									o.getJSONObject("genotype"),
									o.getJSONObject("phenotype"),
									o.getFloat("energy"));
		}
		return new MyIndividual(o.getString("id"),ctrl.getNodeAt(o.getInt("x"),o.getInt("y")));
	}
}
