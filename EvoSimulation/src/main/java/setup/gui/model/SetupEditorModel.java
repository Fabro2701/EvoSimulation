package setup.gui.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import simulator.model.entity.PasiveEntity;
import simulator.model.entity.individuals.MyIndividual;

public class SetupEditorModel {
	private Map<Class<?>, EntitySeparator>separators;
	private SetupEditorModel() {
		separators = new LinkedHashMap<>();
		separators.put(MyIndividual.class, new EntitySeparator("imc","N", "S", "O"));
		separators.put(PasiveEntity.class, new EntitySeparator("info","supermarket", "house", "bar", "restaurant"));
	}
	public static SetupEditorModel emptyModel() {
		SetupEditorModel m = new SetupEditorModel();
		
		
		
		return m;
	}
	public static class EntitySeparator{
		private String att;//Supplier based on expression for future updates
		private List<String>values;//Objects to abstract att grouping
		public EntitySeparator(String att, String...values){
			this.att=att;
			this.values = new ArrayList<>();
			for(String v:values)this.values.add(v);
		}
		public List<String> getValues() {
			return values;
		}
		public String getAtt() {
			return att;
		}
	}
	public Map<Class<?>, EntitySeparator> getSeparators() {
		return separators;
	}
}
