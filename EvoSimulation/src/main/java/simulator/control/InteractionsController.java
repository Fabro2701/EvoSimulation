package simulator.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ActionI;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.evaluation.ActionEvaluator;
import simulator.model.evaluation.EvaluationException;
import util.Pair;

public class InteractionsController extends ModuleController{
	Map<String, InteractionsInt>interactions;
	Map<String, Integer>interactionsFreq;
	Map<String, Pair<List<Class<?>>,List<Class<?>>>>rules;

	public InteractionsController() {
		super();
	}
	public InteractionsController(JSONObject declaration) {
		super(declaration);
		
	}
	@FunctionalInterface
	public static interface InteractionsInt{
		public Object perform(Entity e1, Entity e2, simulator.model.map.Map map, int time)throws IllegalArgumentException , EvaluationException;
	}
	@Override
	protected void init() {
		interactions = new LinkedHashMap<>();
		interactionsFreq = new LinkedHashMap<>();
		rules = new LinkedHashMap<>();
	}
	@Override
	protected void parse(JSONObject declaration) {
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
				
				this.interactions.put(name, (e1,e2,m,time) -> new ActionEvaluator(spec).evaluatePairs("this", e1, "e2", e2, "m", m, "time", time));
				this.interactionsFreq.put(name, actso.getInt("freq"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Pair<List<Class<?>>,List<Class<?>>> getClasses(String id){
		return this.rules.get(id);
	}
	public Map<String, InteractionsInt> getInteractions() {
		return interactions;
	}
	public Map<String, Integer> getInteractionsFreq() {
		return interactionsFreq;
	}
	public boolean match(String id, Class<?>clazzFrom, Class<?>clazzTo) {
		Pair<List<Class<?>>,List<Class<?>>>pair = this.rules.get(id);
		return pair.first.contains(clazzFrom) && pair.second.contains(clazzTo);
	}

}
