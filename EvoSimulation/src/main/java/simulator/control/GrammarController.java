package simulator.control;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import grammar.AbstractGrammar;

public class GrammarController extends ModuleController{
	Map<String, AbstractGrammar>grammars;
	Map<String, Integer>times;
	public GrammarController() {
		super();
	}
	public GrammarController(JSONObject declaration) {
		super(declaration);
	}
	@Override
	protected void init() {
		grammars = new LinkedHashMap<>();
		times = new LinkedHashMap<>();
	}

	@Override
	protected void parse(JSONObject declaration) {
		try {
			JSONArray list = declaration.getJSONArray("list");
			for(int i=0;i<list.length();i++) {
				JSONObject atts = list.getJSONObject(i);
				
				String path = atts.getString("path");
				String name = atts.getString("name");
				int time = atts.getInt("time");
				
				Class<?>clazz = Class.forName(atts.getString("clazz"));
				
				AbstractGrammar gr = (AbstractGrammar)clazz.getConstructor().newInstance();
				gr.parseBNF(path);
				this.grammars.put(name, gr);
				this.times.put(name, time);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, AbstractGrammar> getGrammars() {
		return grammars;
	}
	public int getTime(String id) {
		return this.times.get(id);
	}
	@Override
	public String getCode(Object... id) {
		System.out.println("unimplented GrammarController");
		return null;
	}
}
