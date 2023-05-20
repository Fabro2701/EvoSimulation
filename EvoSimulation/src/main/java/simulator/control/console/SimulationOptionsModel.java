package simulator.control.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.json.JSONException;

import console.model.OptionsModel;
import console.view.ConsoleEditor;
import simulator.LauncherGUI;
import simulator.control.Controller;
import simulator.events.models.SaveSimulationEvent;
import simulator.factories.BuilderBasedFactory;
import simulator.model.evaluation.ActionEvaluator;
import simulator.model.evaluation.EvaluationException;
import statistics.StatsData;
import statistics.StatsManager;

public class SimulationOptionsModel implements OptionsModel{
	private Controller ctrl;
	private LauncherGUI lgui;
	private Map<String, Options> options;
	private Map<String, String> helps;
	public SimulationOptionsModel(Controller ctrl, LauncherGUI lgui) {
		this.ctrl = ctrl;
		this.lgui = lgui;
	}
	@Override
	public Map<String, Options> buildOptions() {
		Map<String, Options> options = new LinkedHashMap<>();
		helps = new LinkedHashMap<>();
		
		Options playOptions = playOption();
		options.put("run",  playOptions);
		options.put("play",  playOptions);
		addHelpDescription("Runs the simulation","run","play");
		
		options.put("pause",  new Options());
		options.put("stop",  new Options());
		addHelpDescription("Pauses the simulation","pause","stop");
		
		options.put("event",  eventsOption());
		addHelpDescription("Loads the events specified in a file","event");
		
		options.put("constant",  constantsOption());
		addHelpDescription("View or/and edit global constants","constant");
		
		options.put("stats",  statsOption());
		addHelpDescription("Load stats models","stats");

		options.put("save",  saveOption());
		addHelpDescription("Save simulation","save");
		
		options.put("help",  new Options());
		options.put("h",  new Options());
		addHelpDescription("Prints commands help descriptions","help","h");
		
		this.options = options;
		return options;
	}
	@Override
	public Map<String, BiFunction<CommandLine, ConsoleEditor, Boolean>> buildActions() {
		Map<String, BiFunction<CommandLine,ConsoleEditor,Boolean>> actions = new LinkedHashMap<>();
		
		
		actions.put("run", (c,e)->playAction(c,e));
		actions.put("play", (c,e)->playAction(c,e));
		
		actions.put("pause", (c,e)->pauseAction(c,e));
		actions.put("stop", (c,e)->pauseAction(c,e));

		actions.put("event", (c,e)->eventsAction(c,e));
		
		actions.put("constant", (c,e)->constantsAction(c,e));
		
		actions.put("stats", (c,e)->statsAction(c,e));
		
		actions.put("save", (c,e)->saveAction(c,e));

		actions.put("help", (c,e)->helpAction(c,e));
		actions.put("h", (c,e)->helpAction(c,e));
		
		return actions;
	}
	
	//Save
	private Options saveOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("d").longOpt("destination").hasArg().desc("Destination file").build());

		return ops;
	}
	private boolean saveAction(CommandLine cmd, ConsoleEditor editor){
		SaveSimulationEvent ev = new SaveSimulationEvent(0, 1, 0, cmd.getOptionValue("destination"), true);
		this.ctrl.getEventManager().addEvent(ev);
		editor.sendInfo("Simulation saved in "+cmd.getOptionValue("destination")+"\n");
		return true;
	}
	
	//Stats
	private Options statsOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("f").longOpt("file").hasArg().desc("Models file").build());
		ops.addOption(Option.builder("c").longOpt("factory").hasArg().desc("Factory file").build());

		return ops;
	}
	private boolean statsAction(CommandLine cmd, ConsoleEditor editor){
		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>(cmd.getOptionValue("factory"));
		StatsManager statsManager = null;
		try {
			statsManager = new StatsManager(cmd.getOptionValue("file"), statsFactory);
			this.ctrl.addStatsManager(statsManager);
			editor.sendInfo("Stats Models created\n");
		} catch (JSONException | FileNotFoundException | IllegalArgumentException | EvaluationException e) {
			editor.sendError("Error loading statsManager\n");
			e.printStackTrace();
			return false;
		} 
		return false;
	}
	
	//Constants
	private Options constantsOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("e").longOpt("edit").numberOfArgs(2).desc("Edit global constant [key,value]").build());
		ops.addOption(Option.builder("v").longOpt("view").desc("Visualize global constants").build());
		return ops;
	}
	private boolean constantsAction(CommandLine cmd, ConsoleEditor editor){
		boolean r = true;
		if(cmd.hasOption("edit")) {
			String args[] = cmd.getOptionValues("edit");
			try {
				ActionEvaluator.globalEnv.assign(args[0], Double.valueOf(args[1]));
			} catch (EvaluationException e) {
				editor.sendError("Error in global variable assingment\n");
				e.printStackTrace();
				r = false;
			}
		}
		if(cmd.hasOption("view")) {//correct format pending
			StringBuilder sb = new StringBuilder();
			Map<String, Object>map = ActionEvaluator.globalEnv.getRecord();
		
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				
				String formattedKey = String.format("%-10s", key);
				String formattedValue = String.format("%-10s%.5f", "= ", value);
				
				sb.append(formattedKey).append(formattedValue).append("\n");
			}
			editor.sendInfo(sb.toString());
		}
		return r;
	}
	
	//Events
	private Options eventsOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("f").longOpt("file").desc("file path").hasArg().build());
		return ops;
	}
	private boolean eventsAction(CommandLine cmd, ConsoleEditor editor){
		if(cmd.hasOption("file")) {
			String filepath = cmd.getOptionValue("file");
			editor.sendInfo("Loading events from: "+filepath+'\n');
			try {
				this.ctrl.loadEvents(new FileInputStream(new File(filepath)));
				editor.sendInfo("Events succesfully loaded\n");//n events
				return true;
			} catch (FileNotFoundException e) {
				editor.sendInfo("Failed\n");
				editor.sendInfo(e.getMessage()+'\n');
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//Play
	private Options playOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("s").longOpt("steps").hasArg().build());
		return ops;
	}
	private boolean playAction(CommandLine cmd, ConsoleEditor editor){
		int steps = 10000;
		if(cmd.hasOption("steps")) {
			steps = Integer.valueOf(cmd.getOptionValue("steps"));
		}
		lgui.setSimStop(false);
		lgui.runEvent(steps);
		editor.sendInfo("Running "+steps+" steps"+'\n');
		return true;
	}

	//Pause
	private boolean pauseAction(CommandLine cmd, ConsoleEditor editor){
		lgui.setSimStop(true);
		editor.sendInfo("Simulation stopped\n");
		return true;
	}
	
	//Help
	private boolean helpAction(CommandLine cmd, ConsoleEditor editor){
		StringBuilder sb = new StringBuilder();

		for(Entry<String, Options> entry:this.options.entrySet()) {
			sb.append(this.helps.get(entry.getKey())).append('\n');
			sb.append(entry.getKey()).append(" --->");
			HelpFormatter formatter = new HelpFormatter();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			
			formatter.printHelp(printWriter, 
								80, 
								entry.getKey(), 
								"", 
								entry.getValue(), 
								entry.getKey().length()+5, 
								1, 
								"", 
								true);
		
			sb.append(stringWriter.toString()).append('\n');
		}
		sb.append('\n');
		editor.sendInfo(sb.toString());
		return true;
	}
	
	private void addHelpDescription(String desc, String...keys) {
		for(String key:keys)this.helps.put(key, desc);
	}
}
