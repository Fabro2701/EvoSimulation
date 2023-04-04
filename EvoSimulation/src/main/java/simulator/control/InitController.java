package simulator.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.entity.Entity;
import simulator.model.evaluation.ActionEvaluator;

public class InitController extends ModuleController{
	Map<String, Consumer<Entity>>statements;
	Map<String, List<Class<?>>>rules;
	Map<String, String>codes;
	public InitController() {
		super();
	}
	public InitController(JSONObject declaration) {
		super(declaration);
		
	}

	@Override
	protected void init() {
		statements = new LinkedHashMap<>();
		rules = new LinkedHashMap<>();
		codes = new LinkedHashMap<>();
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
				this.statements.put(name, (e) -> new ActionEvaluator(spec).evaluatePairs("this",e));
				codes.put(name, actso.getString("code"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<Class<?>> getClasses(String id){
		return this.rules.get(id);
	}
	public Map<String, Consumer<Entity>> getStatements() {
		return statements;
	}
	public boolean match(String id, Class<?>clazz) {
		return this.rules.get(id).contains(clazz);
	}

	@Override
	public String getCode(Object... id) {
		// TODO Auto-generated method stub
		return codes.get(id[0]);
	}
}
