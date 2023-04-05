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
	Map<String, Integer>updatesFreq;
	Map<String, List<Class<?>>>rules;

	public UpdatesController() {
		super();
	}
	public UpdatesController(JSONObject declaration) {
		super(declaration);
	}
	@Override
	protected void init() {
		updates = new LinkedHashMap<>();
		rules = new LinkedHashMap<>();
		updatesFreq = new LinkedHashMap<>();

	}
	@Override
	protected void parse(JSONObject declaration) {
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
				
				int freq = actso.getInt("freq");
				this.updatesFreq.put(name, freq);
				this.updates.put(name, (e,s) -> new ActionEvaluator(spec).evaluatePairs("this", e, "simulator", s));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public List<Class<?>> getClasses(String id){
		return this.rules.get(id);
	}
	public Map<String, BiConsumer<Entity, EvoSimulator>> getUpdates() {
		return updates;
	}
	public Map<String, Integer> getUpdatesFreq() {
		return updatesFreq;
	}
	public boolean match(String id, Class<?>clazz) {
		return this.rules.get(id).contains(clazz);
	}


}
