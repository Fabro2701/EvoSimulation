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
import statistics.visualizers.LineChartVisualizer;
import statistics.visualizers.StatsVisualizer;

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
	 * Load the default models
	 * @param modelFactory factory
	 */
    public StatsManager(BuilderBasedFactory<StatsData> modelFactory){
    	this.modelFactory = modelFactory;
    	observers = new ArrayList<StatsObserver>();
    	loadConfig("default");
    	this.setMaximumSize(new Dimension(500,500));
    	this.setMinimumSize(new Dimension(500,500));
    	this.setPreferredSize(new Dimension(500,500));
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
    	
    	_panel = new JPanel(new GridLayout(2, 2));//this will be dynamically resized
    	this.setContentPane(_panel);
    	
    	//read 
    	for(int i=0;i<config.length();i++) {
    		JSONObject o = config.getJSONObject(i);
    		//create a StatsData
    		StatsData model = modelFactory.createInstance(o, null);
    		//given de StatsData create a StatsVisualizer
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
	 * OnUpdate the StatsObserver childs
	 */
	@Override 
	public void onUpdate(EvoSimulator simulator) {
		for(StatsObserver o:observers) {
			o.onUpdate(simulator);
		}
	}

}