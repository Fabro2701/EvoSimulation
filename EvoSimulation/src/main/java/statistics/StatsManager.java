package statistics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import statistics.visualizers.AreaChartVisualizer;
import statistics.visualizers.BarChartVisualizer;
import statistics.visualizers.BoxChartVisualizer;
import statistics.visualizers.LineChartVisualizer;
import statistics.visualizers.StatsVisualizer;
import statistics.visualizers.TextVisualizer;

/**
 * StatsManager is a Frame where all stats models are allocated
 * This class manages his own factory for StatsData models
 * @author fabrizioortega
 *
 */
public class StatsManager extends JFrame implements StatsObserver{
 
	//the childs StatsObserver
    private List<StatsObserver> observers;
    //the content panel
    private JPanel _panel;
    //factory for StatsData models
	private BuilderBasedFactory<StatsData> modelFactory;
	
	/**
	 * Default constructor (no stats use)
	 */
	public StatsManager() {
		observers = new ArrayList<StatsObserver>();
	}
	/**
	 * Load the default models
	 * @param modelFactory factory
	 */
	public StatsManager(BuilderBasedFactory<StatsData> modelFactory){
		this("default",modelFactory);
	}
    public StatsManager(String config, BuilderBasedFactory<StatsData> modelFactory){
    	this.modelFactory = modelFactory;
    	observers = new ArrayList<StatsObserver>();
    	loadConfig(config);
    	Dimension d = new Dimension(700,700);
    	this.setMaximumSize(d);
    	this.setMinimumSize(d);
    	this.setPreferredSize(d);
    	this.setVisible(true);
    }
    /**
     * Load from filename the models to be displayed
     * @param filename
     */
    public void loadConfig(String filename) {
    	observers.clear();
    	
    	JSONArray config = null;
    	
    	try {
    		config = new JSONArray(new JSONTokener(new FileInputStream("resources/loads/stats/"+filename+".json")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	_panel = new JPanel(new GridLayout(3, 3));//this will be dynamically resized
    	this.setContentPane(_panel);
    	
    	//read 
    	for(int i=0;i<config.length();i++) {
    		JSONObject o = config.getJSONObject(i);
    		//create a StatsData
    		StatsData model = modelFactory.createInstance(o, null);
    		//given the StatsData create a StatsVisualizer
    		StatsVisualizer visu = _chooseVisualizer(o.getJSONObject("data"), model);
    		this.addObserver(model);
    		_panel.add(visu.getPanel());
    	}
    	this.pack();
    }
    /**
     * Return a StatsVisualizer depending on the "visualizer" json attribute
     * We use a method and not a factory because with the time we re not adding new ChartVisualizers
     * @param data
     * @param model
     * @return
     */
    private StatsVisualizer _chooseVisualizer(JSONObject data, StatsData model) {
    	switch(data.getString("visualizer")) {
    		case "line":
    			return new LineChartVisualizer(model,data.getString("title"),data.getString("x"),data.getString("y"));
    		case "area":
    			return new AreaChartVisualizer(model,data.getString("title"),data.getString("x"),data.getString("y"));
    		case "bar":
    			return new BarChartVisualizer(model,data.getString("title"),data.getString("x"),data.getString("y"));
    		case "box":
    			return new BoxChartVisualizer(model,data.getString("title"),data.getString("x"),data.getString("y"));
    		case "text":
    			return new TextVisualizer(model);
    		
    	}
    	return null;
    }
    /**
     * Add a child StatsObserver to be managed
     * @param observer
     */
	public void addObserver(StatsObserver observer) {
		observers.add(observer);
		observer.onRegister();
	}
	
	@Override
	public void onRegister() {}
	
	/**
	 * onStep the StatsObserver children
	 */
	@Override 
	public void onStep(EvoSimulator simulator) {
		if(simulator.getEntities().size()<50)return;
		for(StatsObserver o:observers) {
			o.onStep(simulator);
		}
	}
	@Override 
	public void onMutation() {
		for(StatsObserver o:observers) {
			o.onMutation();
		}
	}
	@Override 
	public void onReproduction() {
		for(StatsObserver o:observers) {
			o.onReproduction();
		}
	}
	@Override 
	public void onDeadOffSpring(int type) {
		for(StatsObserver o:observers) {
			o.onDeadOffSpring(type);
		}
	}
}