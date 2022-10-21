package simulator.control;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ActionI;
import simulator.model.evaluation.ActionEvaluator;

public class ActionsController extends ModuleController{
	Map<String, Map<String, ActionI>>actions;
	public ActionsController(JSONObject declaration) {
		super(declaration);
		
	}

	@Override
	protected void parse(JSONObject declaration) {
		actions = new LinkedHashMap<>();
		try {
			JSONArray list = declaration.getJSONArray("list");
			for(int i=0;i<list.length();i++) {
				JSONObject atts = list.getJSONObject(i);
				
				String id = atts.getString("id");
				JSONArray acs = atts.getJSONArray("actions");
				Map<String, ActionI> inacts = new LinkedHashMap<>();
				for(int j=0;j<acs.length();j++) {
					JSONObject ac = acs.getJSONObject(j);
					
					String name = ac.getString("name");
					JSONArray spec = ac.getJSONArray("spec");
					inacts.put(name, (e,es,m) -> new ActionEvaluator(spec).evaluate(e, es, m));
				}
				this.actions.put(id, inacts);
				//this.actions.computeIfAbsent(id, k -> new LinkedHashSet<ActionI>()).add((e,es,m)->{});
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, Map<String, ActionI>> getActions() {
		return actions;
	}
}
