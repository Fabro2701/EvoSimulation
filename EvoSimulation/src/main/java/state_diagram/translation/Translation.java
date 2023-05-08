package state_diagram.translation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import state_diagram.Diagram;
import state_diagram.elements.Element;
import state_diagram.elements.SimpleState;
import state_diagram.elements.Transition;
import state_diagram.elements.TransitionableElement;
import state_diagram.elements.Transition.TRANSITION_TYPE;

public class Translation {
	String path, pack;
	List<TransitionableElement>elements;
	List<Transition>ts;
	private String fsm;
	public Translation(List<TransitionableElement>elements, List<Transition>ts, String path, String pack, String fsm) throws FileNotFoundException, UnsupportedEncodingException {
		this.path = path;
		this.pack = pack;
		this.elements = elements;
		this.ts = ts;
		this.fsm = fsm;
		
		
	}
	public void run() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(pack).append(";\n\n");
		sb.append("public class Functions"+fsm+" {\n");
		sb.append(simpleStates());
		sb.append(transitionsStates());
		sb.append("}");
		
		int tab=0;
		StringBuilder sb2 = new StringBuilder();
		for(String line:sb.toString().split("\n")) {
			if(line.contains("}"))tab--;
			for(int i=0;i<tab;i++)sb2.append("      ");
			sb2.append(line).append('\n');
			if(line.contains("{"))tab++;
		}
		
		String code = sb2.toString();
		PrintWriter writer = new PrintWriter(path+"Functions"+fsm+".java", "UTF-8");
		writer.println(code);
		writer.close();
		
		System.out.println("FINISHED");
	}
	static Pattern p = Pattern.compile("^\\([^\\(]*\\)");
	public StringBuilder simpleStates() {
		StringBuilder sb = new StringBuilder();
		SimpleState se = null;
		for(Element e:elements) {
			if(e instanceof SimpleState) {
				se = (SimpleState)e;
				sb.append("public static void ").append(se.getId()).append("Action").append(se.ID);
				Matcher m = p.matcher(se.getAction());
				String realCode = se.getAction();
				if(m.find()) {
					sb.append(m.group());
					realCode = se.getAction().substring(m.end());
				}
				else{
					sb.append("(");
					sb.append(")");
				}
				sb.append(" {\n");
				
				sb.append(realCode);
				
				sb.append("\n}\n");
			}
		}
		return sb;
	}
	public StringBuilder transitionsStates() {
		StringBuilder sb = new StringBuilder();
		for(Transition t:ts) {
			sb.append("public static boolean ").append("transition").append(t.ID);
			
			
			Matcher m = p.matcher(t.getCode());
			String realCode = t.getCode();
			if(m.find()) {
				sb.append(m.group());
				realCode = t.getCode().substring(m.end());
			}
			else{
				sb.append("(");
				sb.append(")");
			}
			sb.append(" {\n");
			if(t.getType()==TRANSITION_TYPE.TRUE)sb.append("return true;");
			else {
				sb.append(realCode);
			}
			sb.append("\n}\n");
			
		}
		return sb;
	}
	public static void main(String args[]) {
		Diagram diagram = new Diagram();
		try {
			diagram.load("resources/scenarios/obesidad/movsFSM.json");
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Translation trs = new Translation(diagram.getElems(),
											  diagram.getTransitions(),
											  "src/main/java/experiment/models/obesidad/",
											  "experiment.models.obesidad",
											  diagram.getFsmID());
			trs.run();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
