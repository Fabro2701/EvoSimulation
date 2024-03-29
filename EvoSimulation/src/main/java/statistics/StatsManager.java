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

import simulator.Constants;
import simulator.factories.BuilderBasedFactory;
import simulator.model.EvoSimulator;
import simulator.model.evaluation.EvaluationException;
import statistics.visualizers.AreaChartVisualizer;
import statistics.visualizers.BarChartVisualizer;
import statistics.visualizers.BoxChartVisualizer;
import statistics.visualizers.LineChartVisualizer;
import statistics.visualizers.StatsVisualizer;
import statistics.visualizers.TextVisualizer;

/**
 * StatsManager is a Frame where all stats models are allocated
 * This class manages his own factory for {@code StatsData} models
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
    public StatsManager(String config, BuilderBasedFactory<StatsData> modelFactory) throws JSONException, FileNotFoundException, IllegalArgumentException, EvaluationException{
    	this();
    	this.modelFactory = modelFactory;
    	loadConfig(config);
    	Dimension d = new Dimension(900,700);
    	this.setMinimumSize(d);
    	this.setPreferredSize(d);
    	this.setResizable(true);
    	this.pack();
    	this.setVisible(true);
    }
    /**
     * Loads from filename the models to be displayed
     * @param filename
     * @throws EvaluationException 
     * @throws Exception 
     */
    public void loadConfig(String filename) throws JSONException, FileNotFoundException, IllegalArgumentException, EvaluationException{
    	observers.clear();
    	
    	JSONArray config = new JSONArray(new JSONTokener(new FileInputStream(filename)));
		
    	
    	_panel = this.generateProperPanel(config.length());
    	//_panel = new JPanel(new GridLayout(3, 3));
    	this.setContentPane(_panel);
    	
    	//read 
    	for(int i=0;i<config.length();i++) {
    		JSONObject o = config.getJSONObject(i);
    		//create a StatsData
    		StatsData model = modelFactory.createInstance(o);
    		//given the StatsData create a StatsVisualizer
    		StatsVisualizer visu = chooseVisualizer(o.getJSONObject("data"), model);
    		
    		this.addObserver(model);
    		_panel.add(visu.getPanel());
    	}
    	this.pack();
    }
    /**
     * Generates the proper panels distribution given by the {@link Constants#STATS_PANEL_RATIO}
     * @param num
     * @return
     */
    private JPanel generateProperPanel(int num) {
    	final double ratio = Constants.STATS_PANEL_RATIO;
    	int rows = (int)Math.ceil(num/ratio);
    	return new JPanel(new GridLayout(rows, (int) ratio));
    }
    /**
     * Return a StatsVisualizer depending on the "visualizer" json attribute
     * We use a method and not a factory because with the time we re not adding new ChartVisualizers
     * @param data
     * @param model
     * @return
     */
    private StatsVisualizer chooseVisualizer(JSONObject data, StatsData model) throws IllegalArgumentException{
    	switch(data.getString("visualizer")) {
    		case "line":
    			return new LineChartVisualizer(model, data.optString("title",""), data.optString("x",""), data.optString("y",""));
    		case "area":
    			return new AreaChartVisualizer(model, data.optString("title",""), data.optString("x",""), data.optString("y",""));
    		case "bar":
    			return new BarChartVisualizer(model, data.optString("title",""), data.optString("x",""), data.optString("y",""));
    		case "box":
    			return new BoxChartVisualizer(model, data.optString("title",""), data.optString("x",""), data.optString("y",""));
    		case "text":
    			return new TextVisualizer(model);
    		default:
    			throw new IllegalArgumentException(data.getString("visualizer")+" doesnt match with any visualizer");
    		
    	}
    }
    public List<StatsObserver>getModels(){
    	return this.observers;
    }
    /**
     * Add a child {@link StatsObserver} to be managed
     * @param observer
     */
	public void addObserver(StatsObserver observer) {
		observers.add(observer);
		observer.onRegister();
	}
	
	@Override
	public void onRegister() {
	}
	
	/**
	 * onStep the StatsObserver children
	 */
	@Override 
	public void onStep(EvoSimulator simulator) {
		//if(simulator.getEntities().size()<50)return;
		for(StatsObserver o:observers) {
			o.onStep(simulator);
		}
	}
	@Override 
	public void onEvent(String type) {
		for(StatsObserver o:observers) {
			o.onEvent(type);
		}
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}