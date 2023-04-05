package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class ModuleController {
	JSONObject declaration;

	public ModuleController() {
		this.init();
	}
	public ModuleController(JSONObject declaration) {
		this();
		this.declaration = declaration;
		this.parse(declaration);
	}
	protected abstract void init();
	protected abstract void parse(JSONObject declaration);

}
