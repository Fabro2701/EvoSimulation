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

import console.model.OptionsModel;
import console.view.ConsoleEditor;
import simulator.LauncherGUI;
import simulator.control.Controller;

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
		
		options.put("help",  helpOption());
		options.put("h",  helpOption());
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

		actions.put("help", (c,e)->helpAction(c,e));
		actions.put("h", (c,e)->helpAction(c,e));
		
		return actions;
	}
	private Options helpOption() {
		Options ops = new Options();
		return ops;
	}
	private boolean helpAction(CommandLine cmd, ConsoleEditor editor){
		StringBuilder sb = new StringBuilder();
		//StringJoiner sj
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
		editor.insertString(sb.toString());
		return true;
	}
	private Options eventsOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("f").longOpt("file").hasArg().build());
		return ops;
	}
	private boolean eventsAction(CommandLine cmd, ConsoleEditor editor){
		if(cmd.hasOption("file")) {
			String filepath = cmd.getOptionValue("file");
			editor.insertString("Loading events from: "+filepath+'\n');
			try {
				this.ctrl.loadEvents(new FileInputStream(new File(filepath)));
				editor.insertString("Events succesfully loaded\n");//n events
				return true;
			} catch (FileNotFoundException e) {
				editor.insertString("Failed\n");
				editor.insertString(e.getMessage()+'\n');
				e.printStackTrace();
			}
		}
		return false;
	}
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
		editor.insertString("Running "+steps+" steps"+'\n');
		return true;
	}

	private boolean pauseAction(CommandLine cmd, ConsoleEditor editor){
		lgui.setSimStop(true);
		editor.insertString("Simulation stopped\n");
		return true;
	}
	private void addHelpDescription(String desc, String...keys) {
		for(String key:keys)this.helps.put(key, desc);
	}
}
