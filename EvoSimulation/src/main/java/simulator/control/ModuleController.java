package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class ModuleController {
	JSONObject declaration;
	
	public ModuleController(JSONObject declaration) {
		this.declaration = declaration;
		this.parse(declaration);
	}
	protected abstract void parse(JSONObject declaration);
	public abstract String getCode(Object...id);
}
