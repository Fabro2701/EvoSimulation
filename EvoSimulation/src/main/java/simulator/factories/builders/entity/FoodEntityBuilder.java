package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.entity.FoodEntity;

/**
 * FoodEntityBuilder instantiates a single FoodEntity 
 * @author fabrizioortega
 *
 */
public class FoodEntityBuilder extends EntityBuilder{
	public FoodEntityBuilder() {
		type = Constants.FoodEntityBuilder_TYPE;;
	}
	@Override
	public FoodEntity createTheInstance(JSONObject o, Controller ctrl) {
		if(o.has("foodAmount")) {
			 return new FoodEntity(o.getString("id"),
					  ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),o.getFloat("foodAmount"),o.getInt("lifeTime"),
					  ctrl);
		}
		return new FoodEntity(o.getString("id"),
				  ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
				  ctrl);
	}
}
