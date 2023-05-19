package simulator.control;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ActionI;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.evaluation.ActionEvaluator;
import simulator.model.evaluation.EvaluationException;

public class GlobalController extends ModuleController{
	Map<String, GlobalInt>statements;
	public GlobalController() {
		super();
	}
	public GlobalController(JSONObject declaration) {
		super(declaration);
		
	}
	@FunctionalInterface
	public static interface GlobalInt{
		void accept(Entity t)throws IllegalArgumentException , EvaluationException;
	}
	@Override
	protected void init() {
		statements = new LinkedHashMap<>();
	}
	@Override
	protected void parse(JSONObject declaration) {
		
		try {
			JSONArray list = declaration.getJSONArray("list");
			for(int i=0;i<list.length();i++) {
				JSONObject actso = list.getJSONObject(i);
				
				String name = actso.getString("name");
				JSONArray spec = actso.getJSONArray("spec");
			
				this.statements.put(name, (e) -> new ActionEvaluator(spec).evaluate(ActionEvaluator.globalEnv, false));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//for global vars
		for(String id:statements.keySet()) {
			try {
				statements.get(id).accept(null);
			} catch (IllegalArgumentException | EvaluationException e) {
				System.err.println("Error executin global vars");
				e.printStackTrace();
			}
		}
	}

	public Map<String, GlobalInt> getStatements() {
		return statements;
	}

	
}
