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
import util.Pair;

public class InteractionsController extends ModuleController{
	Map<String, InteractionI>interactions;
	Map<String, Pair<List<Class<?>>,List<Class<?>>>>rules;
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
				JSONArray clazzsFrom = actso.getJSONArray("clazzsFrom");
				JSONArray clazzsTo = actso.getJSONArray("clazzsTo");
				for(int j=0;j<clazzsFrom.length();j++) {
					JSONObject clazz = clazzsFrom.getJSONObject(j);
					rules.computeIfAbsent(name, c->new Pair<>(new ArrayList<>(),new ArrayList<>())).first.add(Class.forName(clazz.getString("value")));
				}
				for(int j=0;j<clazzsTo.length();j++) {
					JSONObject clazz = clazzsTo.getJSONObject(j);
					rules.computeIfAbsent(name, c->new Pair<>(new ArrayList<>(),new ArrayList<>())).second.add(Class.forName(clazz.getString("value")));
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
	public boolean match(String id, Class<?>clazzFrom, Class<?>clazzTo) {
		Pair<List<Class<?>>,List<Class<?>>>pair = this.rules.get(id);
		return pair.first.contains(clazzFrom) && pair.second.contains(clazzTo);
	}
}
