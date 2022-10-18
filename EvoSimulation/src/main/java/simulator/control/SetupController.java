package simulator.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import setup.MainParser;

public class SetupController {
	JSONObject program;
	Map<String, ModuleController> controllers;
	public SetupController() {
		
	}
	public ModuleController getModule(String key) {
		return this.controllers.get(key);
	}
	public static SetupController from(String path) {
		SetupController ctrl = new SetupController();
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/setup/default.stp")));){
			String aux = reader.readLine();
			while(aux!=null) {
				sb.append(aux);
				sb.append("\n");
				aux = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String e3 = sb.toString();
		MainParser parser = new MainParser();
		ctrl.program = parser.parse(e3);
		
		JSONArray rules = ctrl.program.getJSONArray("rules");
		for(int i=0;i<rules.length();i++) {
			JSONObject rule = rules.getJSONObject(i);
			String type = rule.getString("type");
			JSONObject declaration = rule.getJSONObject("declaration");
			
			ModuleController mctrl = null;
			try {
				mctrl = (ModuleController) Class.forName("simulator.control."+type).getDeclaredConstructor(JSONObject.class).newInstance(declaration);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
			ctrl.controllers.put(type, mctrl);
		}
		return ctrl;
	}
}
