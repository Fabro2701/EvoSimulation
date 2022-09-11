package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;

/**
 * MyIndividualBuilder instantiates a single MyIndividual
 * @author fabrizioortega
 *
 */
public class MyIndividualBuilder extends EntityBuilder{
	public MyIndividualBuilder() {
		type = Constants.MyIndividualBuilder_TYPE;
	}
	@Override
	public Entity createTheInstance(JSONObject o, Controller ctrl) {
		if(o.has("genotype")) { 
			return new MyIndividual(o.getString("id"),
									ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
									o.getJSONObject("genotype"),
									o.getJSONObject("phenotype"),
									o.getFloat("energy"),ctrl);
		}
		return new MyIndividual(o.getString("id"),ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),ctrl);
	}
}
