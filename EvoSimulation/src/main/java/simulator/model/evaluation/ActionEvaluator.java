package simulator.model.evaluation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.ActionsController;
import simulator.control.SetupController;
import simulator.model.ActionI;
import simulator.model.entity.Entity;
import simulator.model.map.Map;


public class ActionEvaluator {
	public static class TestEv{
		public int x=4;
		public float f=4;
		public TestEv2 test2 = new TestEv2();
		public void setF(float x) {
			this.f = x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getX() {
			return x;
		}
		public int inc(int inc) {
			return x+inc;
		}
		public TestEv2 getTest2() {
			return test2;
		}
	}
	public static class TestEv2{
		public int y=1;
	
	}
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
		env.define("test", new TestEv());
		
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
		case "ExpressionStatement":
			return this.eval(query.getJSONObject("expression"), env);
		case "NewExpression":
			return this.evalNewExpression(query, env);
		case "AssignmentExpression":
			return this.evalAssignmentExpression(query, env);
		case "MemberExpression":
			return this.evalMemberExpression(query, env);
		case "CallExpression":
			return this.evalCallExpression(query, env);
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
			return this.evalNumberLiteral(query);
		case "Identifier":
			return env.search(query.getString("name"));
		case "EmptyStatement":
			return null;
		default:
			System.err.println("unsupported type: "+type);
			return null;
		}
	}
	private Object evalNewExpression(JSONObject query, Environment env) {
		StringBuilder str = new StringBuilder();
		
		JSONObject callee = query.getJSONObject("callee");
		str.append(callee.getJSONObject("property").getString("name"));
		while(callee.has("object")) {
			callee = callee.getJSONObject("object");
			if(callee.has("property")) {
				str.append(".").append(callee.getJSONObject("property").getString("name"));
			}
			else {
				str.append(".").append(callee.getString("name"));
			}
		}
		
		String realname[] = str.toString().split("\\.");
		str = new StringBuilder();
		for(int i=realname.length-1;i>=0;i--) {
			str.append(realname[i]).append(".");
		}str.deleteCharAt(str.length()-1);
		try {
			Class<?>clazz = Class.forName(str.toString());
			Object object = clazz.getConstructors()[0].newInstance(null);
			return object;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private Object evalNumberLiteral(JSONObject query) {
		try {
			String clazzs = query.getString("class");
			Class<?>clazz = Class.forName(clazzs);
			
			return clazz.getMethod("valueOf", String.class).invoke(clazz, query.get("value"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private Object evalCallExpression(JSONObject query, Environment env) {
		JSONObject callee = query.getJSONObject("callee");
		JSONArray arguments = query.getJSONArray("arguments");
		
		Class<?>clazzs[] = new Class<?>[arguments.length()];
		Object args[] = new Object[arguments.length()];
		for(int i=0;i<arguments.length();i++) {
			args[i] = eval(arguments.getJSONObject(i), env);
			clazzs[i] = args[i].getClass();
		}
		
		JSONObject property = callee.getJSONObject("property");
		JSONObject object = callee.getJSONObject("object");
		Object ob = this.eval(object, env);
		try {
			Method m = null;
			//Method m = ob.getClass().getDeclaredMethod(property.getString("name"), clazzs);
			for(Method mi:ob.getClass().getDeclaredMethods())if(mi.getName().equals(property.getString("name")))m=mi;
			Class<?>prms[] = m.getParameterTypes();
//			for(int i=0;i<arguments.length();i++) {
//				args[i] = prms[i].cast(args[i]);
//			}
			return m.invoke(ob, args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	private Object evalMemberExpression(JSONObject query, Environment env) {
		JSONObject property = query.getJSONObject("property");
		JSONObject object = query.getJSONObject("object");
		boolean computed = query.getBoolean("computed");
		if(!computed) {
			Object ob = this.eval(object, env);
			try {
				Field f = ob.getClass().getDeclaredField(property.getString("name"));
				return f.get(ob);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
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
		JSONObject right = query.getJSONObject("right");
		JSONObject left = query.getJSONObject("left");
		String op = query.getString("operator");
		
		Object lefto = eval(left, env);
		Object righto = eval(right, env);
		
		lefto = righto;
		return lefto;
	}

	public static void main(String args[]) {
		SetupController stc = SetupController.from("resources/setup/default.stp");
		ActionsController ac = (ActionsController)stc.getModule("ActionsController");
		
		java.util.Map<String, java.util.Map<String, ActionI>> acs = ac.getActions();
		ActionI a = acs.get("move").get("NEUTRAL");
		
		System.out.println(a.perform(null, null, null));
		
	}
}
