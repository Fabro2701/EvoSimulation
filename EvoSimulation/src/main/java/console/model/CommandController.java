package console.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import console.view.ConsoleEditor;

/**
 * 
 * @author Fabrizio Ortega
 *
 */
public class CommandController {
	ConsoleEditor editor;
	private Map<String, Options> options;
	private Map<String, BiFunction<CommandLine,ConsoleEditor,Boolean>> actions;
	private static CommandLineParser parser = new DefaultParser();
	private OptionsModel model;
	
	public CommandController(OptionsModel model) {
		this.options = model.buildOptions();
		this.actions = model.buildActions();
		if(!this.options.keySet().equals(this.actions.keySet())) {
			System.err.println("Options and Actions keys dont match");
		}
		this.model = model;
	}
	public boolean execute(String query) {
		String args[] = query.split(" ");
		if(!options.containsKey(args[0])) {
			editor.insertString(args[0]+" command not supported\n", ConsoleEditor.errorStyle);
			return false;
		}
		try {
			CommandLine cmd = parser.parse(options.get(args[0]), Arrays.copyOfRange(args, 1, args.length));
			return actions.get(args[0]).apply(cmd, editor);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Searches a command that starts with query and return it
	 * @param query
	 * @return the command if exists or the query otherwise
	 */
	public String autoComplete(String query) {
		for(String k:this.options.keySet()) {
			if(k.indexOf(query)!=-1)return String.copyValueOf(k.toCharArray());
		}
		return query;
	}
	public void setEditor(ConsoleEditor consoleEditor) {
		this.editor = consoleEditor;
	}

}
