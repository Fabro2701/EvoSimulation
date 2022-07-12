package simulator.factories;

import java.util.List;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.builders.Builder;
import simulator.model.entity.Entity;

public class BuilderBasedFactory<T> implements Factory<T>{
	private List<Builder<T>> builders;
	public BuilderBasedFactory(List<Builder<T>>builders) {
		this.builders = builders;
	}
	public T createInstance(JSONObject info, Controller controller) {
		for(Builder<T> b: builders) {
			T tmp = b.createInstance(info,controller);
			if(tmp!=null) {
				
				return tmp;
			}
		}
		return null;
		//throw new IllegalArgumentException();
	}
}
