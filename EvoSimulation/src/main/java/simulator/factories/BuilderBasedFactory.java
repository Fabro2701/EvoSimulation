package simulator.factories;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.builders.Builder;

public class BuilderBasedFactory<T> implements Factory<T>{
	private List<Builder<T>> builders;
	public BuilderBasedFactory(List<Builder<T>>builders) {
		this.builders = builders;
	}
	public BuilderBasedFactory(String filename) {
		this.builders = new ArrayList<Builder<T>>();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("resources/loads/properties/"+filename+".properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String buildersS = properties.getProperty("builders");
		for(String builder:buildersS.split(",")) {
			try {
				Builder<T> b =(Builder<T>)Class.forName(builder).getConstructor().newInstance();
				this.builders.add(b);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public T createInstance(JSONObject info, Controller controller) {
		for(Builder<T> b: builders) {
			T tmp = b.createInstance(info,controller);
			if(tmp!=null) {
				return tmp;
			}
		}
		System.err.println("type not found: "+info.getString("type"));
		return null;
		//throw new IllegalArgumentException();
	}
	
}
