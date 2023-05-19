package simulator.factories;


import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.evaluation.EvaluationException;

public interface Factory<T> {
	public T createInstance(JSONObject info, Controller controller) throws JSONException, EvaluationException;
}
