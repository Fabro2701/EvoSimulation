package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.model.entity.PasiveEntity;

/**
 * 
 * @author fabrizioortega
 *
 */
public class PasiveEntityBuilder extends EntityBuilder{
	public PasiveEntityBuilder() {
		type = Constants.PasiveEntityBuilder_TYPE;
	}
	@Override
	public PasiveEntity createTheInstance(JSONObject o, Controller ctrl) {
		String code = null;
		if(o.has("properties")) {
			JSONObject properties = o.getJSONObject("properties");
			if(properties.has("apply")) {
				code = properties.getString("apply");
			}
		}
		PasiveEntity pe = new PasiveEntity(o.getString("id"),
				  ctrl.getNodeAt(o.getInt("x"),o.getInt("y")),
				  ctrl,code);
		
		
		return pe;
	}
}
