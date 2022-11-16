package simulator.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ActionI;
import simulator.model.InteractionI;
import simulator.model.evaluation.ActionEvaluator;

public class InteractionsController extends ModuleController{
	Map<String, InteractionI>interactions;
	Map<String, List<Class<?>>>rules;
	public InteractionsController(JSONObject declaration) {
		super(declaration);
		
	}

	@Override
	protected void parse(JSONObject declaration) {
		interactions = new LinkedHashMap<>();
		rules = new LinkedHashMap<>();
		try {
			JSONArray list = declaration.getJSONArray("list");
			for(int i=0;i<list.length();i++) {
				JSONObject actso = list.getJSONObject(i);
				
				String name = actso.getString("name");
				JSONArray spec = actso.getJSONArray("spec");
				JSONArray clazzs = actso.getJSONArray("clazzs");
				for(int j=0;j<clazzs.length();j++) {
					JSONObject clazz = clazzs.getJSONObject(j);
					rules.computeIfAbsent(name, c->new ArrayList<>()).add(Class.forName(clazz.getString("value")));
				}
				
				this.interactions.put(name, (e1,e2,m) -> new ActionEvaluator(spec).evaluate(e1, e2, m));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, InteractionI> getInteractions() {
		return interactions;
	}
	public boolean match(String id, Class<?>clazz) {
		return this.rules.get(id).contains(clazz);
	}
}
