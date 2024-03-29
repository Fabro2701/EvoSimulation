package simulator.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import setup.MainParser;
import setup.preprocessing.TextFilePreprocessing;
import setup.preprocessing.CommentPreprocessing;
import setup.preprocessing.TreeClassifierPreprocessing;

public class SetupController {
	JSONObject program;
	Map<String, ModuleController> controllers;
	String filename;
	public SetupController() {
		controllers = new LinkedHashMap<String, ModuleController>();
	}
	public ModuleController getModule(String key) {
		return this.controllers.get(key);
	}
	public static SetupController from(String path) {
		SetupController ctrl = new SetupController();
		ctrl.filename = path;
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));){
			String aux = reader.readLine();
			while(aux!=null) {
				sb.append(aux);
				sb.append("\n");
				aux = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String text = sb.toString();
		text = CommentPreprocessing.apply(text);
		text = TextFilePreprocessing.apply(text);
		text = TreeClassifierPreprocessing.apply(text);
		//System.out.println(text);
		
		MainParser parser = new MainParser();
		ctrl.program = parser.parse(text);
		
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
	public String getFilename() {
		return filename;
	}
}
