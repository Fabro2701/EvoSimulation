package simulator.factories.builders;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.evaluation.EvaluationException;



public abstract class Builder <T>{
	protected String type;
	public Builder() {type=".";}
	public T createInstance(JSONObject info, Controller ctrl) throws JSONException, EvaluationException {
		if(type.equals(info.getString("type"))) {
			return createTheInstance(info.getJSONObject("data"),ctrl);
		}
		else return null;
	}
	public abstract T createTheInstance(JSONObject o, Controller ctrl) throws JSONException, EvaluationException;
}
