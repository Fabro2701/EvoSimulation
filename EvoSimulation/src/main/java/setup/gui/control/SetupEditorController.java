package setup.gui.control;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import setup.gui.model.SetupEditorModel;
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
}
