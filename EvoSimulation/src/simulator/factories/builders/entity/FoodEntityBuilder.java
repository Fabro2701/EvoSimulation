package simulator.factories.builders.entity;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.builders.Builder;
import simulator.model.entity.Entity;
import simulator.model.entity.FoodEntity;

public class FoodEntityBuilder extends Builder<Entity>{
	public FoodEntityBuilder() {
		type="f";
	}
	@Override
	public FoodEntity createTheInstance(JSONObject o, Controller ctrl) {
		// TODO Auto-generated method stub
		return new FoodEntity(o.getString("id"),
				  ctrl.getNodeAt(o.getInt("x"),o.getInt("y"))
				  );
	}

}
