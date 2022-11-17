package simulator.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ActionI;
import simulator.model.EvoSimulator;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.evaluation.ActionEvaluator;

public class UpdatesController extends ModuleController{
	Map<String, BiConsumer<Entity, EvoSimulator>>updates;
	Map<String, List<Class<?>>>rules;
	public UpdatesController(JSONObject declaration) {
		super(declaration);
		
	}

	@Override
	protected void parse(JSONObject declaration) {
		updates = new LinkedHashMap<>();
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
			
				this.updates.put(name, (e,s) -> new ActionEvaluator(spec).evaluate(e,s));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, BiConsumer<Entity, EvoSimulator>> getUpdates() {
		return updates;
	}
	public boolean match(String id, Class<?>clazz) {
		return this.rules.get(id).contains(clazz);
	}
}
