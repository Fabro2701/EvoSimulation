package simulator.control;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar;

public class GrammarController extends ModuleController{
	Map<String, AbstractGrammar>grammars;
	public GrammarController(JSONObject declaration) {
		super(declaration);
		grammars = new LinkedHashMap<>();
	}

	@Override
	protected void parse(JSONObject declaration) {
		try {
			JSONArray list = declaration.getJSONArray("list");
			for(int i=0;i<list.length();i++) {
				JSONObject atts = list.getJSONObject(i);
				String path = atts.getString("path");
				String name = atts.getString("name");
				Class<?>clazz = Class.forName(atts.getString("clazz"));
				
				AbstractGrammar gr = (AbstractGrammar)clazz.getConstructor().newInstance();
				gr.parseBNF(path);
				this.grammars.put(name, gr);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
