package setup.gui.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import setup.gui.model.SetupEditorModel;
import setup.gui.model.SetupEditorModel.EntityInteraction;
import setup.gui.model.SetupEditorModel.EntitySeparator;
import setup.gui.model.SetupEditorModel.FSMDeclaration;

public class SetupEditorController {
	SetupEditorModel model;
	public SetupEditorController() {
		model = SetupEditorModel.emptyModel();
	}
	public Map<Class<?>, EntitySeparator> pullSeparators() {
		return model.getSeparators();
	}
	public void pushSeparator(Class<?> id, String att, String[] vals) {
		EntitySeparator sep = new EntitySeparator(att, vals);
		model.getSeparators().put((Class<?>) id, sep);
	}
	public void pushSeparator(Class<?> id, String att, List<Object> vals) {
		EntitySeparator sep = new EntitySeparator(att, vals);
		model.getSeparators().put((Class<?>) id, sep);
	}
	public void pushFSM(JSONObject jo) {
		JSONArray arr = jo.getJSONObject("root").getJSONArray("blocks");
		JSONObject ob = arr.getJSONObject(0).getJSONArray("right").getJSONObject(0).getJSONArray("blocks").getJSONObject(0);
		if(ob.getString("type").equals("GhostBlock")) {
			return;
		}
		String fsmid = ob.getString("text");
		model.getFsms().put(fsmid, FSMDeclaration.from(jo));
	}
	public JSONArray pullFSMs() {
		JSONArray arr = new JSONArray();
		for(FSMDeclaration fsm:model.getFsms().values()) {
			arr.put(fsm.getJson());
		}
		return arr;
	}
	public JSONObject pullInteraction(Object o1, Object o2) {
		JSONObject jo = null;
		List<EntityInteraction> interactions = model.getInteractions();
		for(EntityInteraction ei:interactions) {
			if(ei.getE1().equals(o1)&&ei.getE2().equals(o2)) {
				jo = ei.getJson();
			}
		}
		return jo;
	}
	public void pushInteraction(JSONObject jo) {
		JSONObject root = jo.getJSONObject("root");
		String name1 = evaluateName(root.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));
		String name2 = evaluateName(root.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(1));
		
		Object o1 = name1;
		Object o2 = name2;
		try {
			if(name1.contains("class")) {
				o1 = Class.forName(name1.substring(6));
			}
			if(name2.contains("class")) {
				o2 = Class.forName(name2.substring(6));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		

		JSONArray intsjo = root.getJSONArray("blocks").getJSONObject(1).getJSONArray("children");
		List<JSONObject> ints = extractFromList(intsjo.getJSONObject(0),"INTERACTIONLIST");
		for(JSONObject it:ints) {
			String id = evaluateName(it.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));
			JSONObject codejo = it.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0);
			String code = evaluateName(codejo);
			
			boolean found = false;
			for(EntityInteraction ei:model.getInteractions()) {
				if(ei.getE1().equals(o1)&&ei.getE2().equals(o2)) {
					ei.setCode(code);
					ei.setJson(codejo);
					found = true;
					break;
				}
			}
			if(!found)model.getInteractions().add(new EntityInteraction(o1,o2,code,codejo));
		}
	}
	private String evaluateName(JSONObject NAME) {
		return NAME.getJSONArray("blocks").getJSONObject(0).getString("text");
	}
	private List<JSONObject> extractFromList(JSONObject ob, String NT) {
		List<JSONObject>list = new ArrayList<>();;
		JSONArray blocks = ob.getJSONArray("blocks");
		if(blocks.length()==1) {
			list.add(blocks.getJSONObject(0).getJSONArray("children").getJSONObject(0));
			list.addAll(extractFromList(blocks.getJSONObject(0).getJSONArray("children").getJSONObject(1), NT));
		}
		else{
			list.add(blocks.getJSONObject(0).getJSONArray("children").getJSONObject(0));
		}
		return list;
	}
}
