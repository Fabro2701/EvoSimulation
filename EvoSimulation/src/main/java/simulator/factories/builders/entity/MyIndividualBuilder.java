package simulator.factories.builders.entity;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.MyIndividual;
import simulator.model.evaluation.EvaluationException;

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
	public Entity createTheInstance(JSONObject o, Controller ctrl) throws JSONException, EvaluationException {
		MyIndividual mi = null;
		if(o.has("genotype")) { 
			mi = MyIndividual.fromJSON(o.getString("id"),
									ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
									o.getJSONObject("genotype"),
									o.getJSONObject("phenotype"),
									o.getJSONObject("properties"),ctrl);
		}
		else{
			if(o.has("properties")) {
				JSONObject properties = o.getJSONObject("properties");
				if(properties.has("apply")) {
					mi =  new MyIndividual(o.getString("id"),ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),ctrl,properties.getString("apply"));
				}
			}
			else mi =  new MyIndividual(o.getString("id"),ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),ctrl);
		}
	
		return mi;
	}
}
