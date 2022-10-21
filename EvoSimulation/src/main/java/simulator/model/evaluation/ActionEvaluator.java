package simulator.model.evaluation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.ActionsController;
import simulator.control.SetupController;
import simulator.model.ActionI;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

public class ActionEvaluator {
	
	JSONArray program;
	
	public ActionEvaluator(JSONArray program) {
		this.program = program;
		System.out.println(program.toString(4));
	}
	public Object evaluate(Entity e, List<Entity>entities, Map map) {
		Environment env = new Environment(null);
		env.define("e", e);
		env.define("entities", entities);
		env.define("map", map);
		
		Object r = null;
		JSONObject expression = null;
		for(int i=0;i<program.length();i++) {
			expression = program.getJSONObject(i);
			r = this.eval(expression, env);
		}
		env.clear();
		return r;
	}

	private Object eval(JSONObject query, Environment env) {
		Objects.requireNonNull(query);
		String type = query.getString("type");
		
		switch(type) {
//		case "AssignmentExpression":
//			return this.evalAssignmentExpression(query, env);
		case "BlockStatement":
			return this.evalBlockStatement(query, env);
		case "VariableStatement":
			return this.evalVariableStatement(query, env);
		case "BinaryExpression":
			return this.evalBinaryExpression(query, env);
		case "LogicalExpression":
			return this.evalLogicalExpression(query, env);
		case "IfStatement":
			return this.evalIfStatement(query, env);
		case "NumberLiteral":
			return Float.parseFloat(query.getString("value"));
		case "Identifier":
			return env.search(query.getString("name"));
		case "EmptyStatement":
			return null;
		default:
			System.err.println("unsupported type: "+type);
			return null;
		}
	}
	private Object evalBlockStatement(JSONObject query, Environment env) {
		Object r = null;
		
		JSONArray body = query.getJSONArray("body");
		for(int i=0;i<body.length();i++) {
			JSONObject b = body.getJSONObject(i);
			r = eval(b, new Environment(env));
		}
		return r;
	}
	private Object evalIfStatement(JSONObject query, Environment env) {
		JSONObject test = query.getJSONObject("test");
		JSONObject consequent = query.getJSONObject("consequent");
		JSONObject alternate = query.getJSONObject("alternate");
		
		if((boolean) eval(test, env)) {
			return eval(consequent, env);
		}
		else if(alternate!=null){
			return eval(alternate, env);
		}
		return null;
	}
	private Object evalLogicalExpression(JSONObject query, Environment env) {
		JSONObject left = query.getJSONObject("left");
		JSONObject right = query.getJSONObject("right");
		String op = query.getString("operator");
		
		switch(op) {
		case ">":
			return (float)eval(left, env)>(float)eval(right, env);
		case "<":
			return (float)eval(left, env)-(float)eval(right, env);
		case "==":
			return (float)eval(left, env)*(float)eval(right, env);
		default:
			System.err.println("Unsupported operator "+op);
			return null;
		}
	}
	private Object evalBinaryExpression(JSONObject query, Environment env) {
		JSONObject left = query.getJSONObject("left");
		JSONObject right = query.getJSONObject("right");
		String op = query.getString("operator");
		
		switch(op) {
		case "+":
			return (float)eval(left, env)+(float)eval(right, env);
		case "-":
			return (float)eval(left, env)-(float)eval(right, env);
		case "*":
			return (float)eval(left, env)*(float)eval(right, env);
		default:
			System.err.println("Unsupported operator "+op);
			return null;
		}
	}
	private Object evalVariableStatement(JSONObject query, Environment env) {
		JSONObject declaration = query.getJSONObject("declaration");
		
		return env.define(declaration.getJSONObject("id").getString("name"), 
				     eval(declaration.getJSONObject("init"), env));
	}
	private Object evalAssignmentExpression(JSONObject query, Environment env) {
//		JSONObject right = query.getJSONObject("right");
//		JSONObject left = query.getJSONObject("left");
//		String op = query.getString("operator");
//		
//		String attname = left.getString("name");
//		if(this.variables.containsKey(attname)) {
//			this.variables.put(attname, attname)
//		}
//		int idx = attname.lastIndexOf('.');
//		if(idx == -1) {
//			this.variables.put(attname, );
//		}
//		else {
//			
//		}
//		String varname = attname.substring(0, idx); 
		return null;
	}
	private Object searchVar(String name) {
		Object base = null;
		try {
			String[] path = name.split("\\.");
			base = variables.get(path[0]);
			for(int i=1;i<path.length;i++) {
				String mName = "get"+(char)(path[i].charAt(0)-32)+path[i].substring(1, path[i].length());
				base = base.getClass().getMethod(mName, null).invoke(base,null);
				
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base;
	}
	private float getValue(String name) {
		Object var = searchVar(name);
		Objects.requireNonNull(var);
		return ((Float)var).floatValue();
	}
	private void setValue(Object var, String name, Object value) {
		Objects.requireNonNull(var);
		try {
			Method m = var.getClass().getMethod("set"+(char)(name.charAt(0)-32), value.getClass());
			m.invoke(var, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		SetupController stc = SetupController.from("resources/setup/default.stp");
		ActionsController ac = (ActionsController)stc.getModule("ActionsController");
		
		java.util.Map<String, java.util.Map<String, ActionI>> acs = ac.getActions();
		ActionI a = acs.get("move").get("RIGHT");
		
		System.out.println(a.perform(null, null, null));
		
	}
}
