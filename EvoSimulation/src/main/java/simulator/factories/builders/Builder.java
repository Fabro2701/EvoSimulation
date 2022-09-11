package simulator.factories.builders;

import org.json.JSONObject;

import simulator.control.Controller;



public abstract class Builder <T>{
	protected String type;
	public Builder() {type=".";}
	public T createInstance(JSONObject info, Controller ctrl) {
		if(type.equals(info.getString("type"))) {
			return createTheInstance(info.getJSONObject("data"),ctrl);
		}
		else return null;
	}
	public abstract T createTheInstance(JSONObject o, Controller ctrl);
}
