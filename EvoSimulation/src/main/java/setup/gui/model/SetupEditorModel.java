package setup.gui.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import simulator.control.fsm.FSM;
import simulator.model.entity.PasiveEntity;
import simulator.model.entity.individuals.MyIndividual;

public class SetupEditorModel {
	private Map<Class<?>, EntitySeparator>separators;
	private Map<String, FSMDeclaration>fsms;
	private List<EntityInteraction>interactions;
	private JSONObject global;
	private SetupEditorModel() {
		this.separators = new LinkedHashMap<>();
		this.fsms = new LinkedHashMap<>();
		this.interactions = new ArrayList<>();
	}
	public static SetupEditorModel emptyModel() {
		SetupEditorModel m = new SetupEditorModel();
		
		m.separators.put(MyIndividual.class, new EntitySeparator("imc","N", "S", "O"));
		m.separators.put(PasiveEntity.class, new EntitySeparator("info","supermarket", "house", "bar", "restaurant"));
		/*m.separators.put(MyIndividual.class, new EntitySeparator());
		m.separators.put(PasiveEntity.class, new EntitySeparator());
		 */
		return m;
	}
	public static class EntityInteraction{
		Object e1, e2;
		JSONObject json;
		String code;
		boolean generated = false;
		public EntityInteraction(Object e1, Object e2, String code, JSONObject ob) {
			this.e1 = e1;
			this.e2 = e2;
			this.json = ob;
			this.code = code;
			this.generated = false;
		}
		public Object getE1() {
			return e1;
		}
		public Object getE2() {
			return e2;
		}
		public JSONObject getJson() {
			return json;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public void setJson(JSONObject json) {
			this.json = json;
		}
	}
	public static class FSMDeclaration{
		JSONObject json;
		FSM<String, String> fsm;
		boolean generated = false;
		public static FSMDeclaration from(JSONObject ob) {
			FSMDeclaration fsm = new FSMDeclaration();
			fsm.json = ob;
			fsm.generated = false;
			return fsm;
		}
		public JSONObject getJson() {
			return json;
		}
	}
	public static class EntitySeparator{
		private String att;//Supplier based on expression for future updates
		private List<String>values;//Objects to abstract att grouping
		
		public EntitySeparator(){
			this.values = new ArrayList<>();
		}
		public EntitySeparator(String att, String...values){
			this();
			this.att = att;
			for(String v:values)this.values.add(v);
		}
		public EntitySeparator(String att, List<Object>values){
			this();
			this.att = att;
			for(Object v:values)this.values.add((String) v);
		}
		public List<String> getValues() {
			return values;
		}
		public void setValues(String...vs) {
			this.values.clear();
			for(String v:vs)this.values.add(v);
		}
		public void setAtt(String att) {
			this.att = att;
		}
		public String getAtt() {
			return att;
		}
	}
	public Map<Class<?>, EntitySeparator> getSeparators() {
		return separators;
	}
	public Map<String, FSMDeclaration> getFsms() {
		return fsms;
	}
	public List<EntityInteraction> getInteractions() {
		return interactions;
	}
	public JSONObject getGlobal() {
		return global;
	}
	public void setGlobal(JSONObject global) {
		this.global = global;
	}
}
