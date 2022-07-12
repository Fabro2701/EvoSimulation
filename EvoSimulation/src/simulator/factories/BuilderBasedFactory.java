package simulator.factories;

import java.util.List;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;

public class BuilderBasedFactory implements Factory{
	private List<Entity.Builder> builders;
	public BuilderBasedFactory(List<Entity.Builder>builders) {
		this.builders = builders;
	}
	public Entity createInstance(JSONObject info, Controller controller) {
		for(Entity.Builder b: builders) {
			Entity tmp = b.createInstance(info,controller);
			if(tmp!=null) {
				return tmp;
			}
		}
		return null;
		//throw new IllegalArgumentException();
	}
}
