package simulator.control.console;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import console.model.OptionsModel;
import console.view.ConsoleEditor;
import simulator.LauncherGUI;
import simulator.control.Controller;

public class SimulationOptionsModel implements OptionsModel{
	private Controller ctrl;
	private LauncherGUI lgui;
	private Map<String, BiFunction<CommandLine,ConsoleEditor,Boolean>>actions; 
	public SimulationOptionsModel(Controller ctrl, LauncherGUI lgui) {
		this.ctrl = ctrl;
		this.actions = new HashMap<>();
		this.lgui = lgui;
	}
	@Override
	public Map<String, Options> buildOptions() {
		Map<String, Options> options = new HashMap<>();
		
		Options playOptions = playOption();
		options.put("run",  playOptions);
		options.put("play",  playOptions);
		
		options.put("pause",  new Options());
		options.put("stop",  new Options());
		
		return options;
	}
	@Override
	public Map<String, BiFunction<CommandLine, ConsoleEditor, Boolean>> buildActions() {
		Map<String, BiFunction<CommandLine,ConsoleEditor,Boolean>> actions = new HashMap<>();
		
		
		actions.put("run", (c,e)->playAction(c,e));
		actions.put("play", (c,e)->playAction(c,e));
		
		actions.put("pause", (c,e)->pauseAction(c,e));
		actions.put("stop", (c,e)->pauseAction(c,e));
		
		return actions;
	}
	private Options playOption() {
		Options ops = new Options();
		ops.addOption(Option.builder("s").longOpt("steps").hasArg().build());
		return ops;
	}
	private boolean playAction(CommandLine cmd, ConsoleEditor editor){
		if(cmd.hasOption("steps")) {
			lgui.setSimStop(false);
			lgui.runEvent(Integer.valueOf(cmd.getOptionValue("steps")));
			editor.insertString("running "+cmd.getOptionValue("steps")+'\n');
			return true;
		}
		return false;
	}

	private boolean pauseAction(CommandLine cmd, ConsoleEditor editor){
		lgui.setSimStop(true);
		return true;
	}
}
