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
import simulator.control.InteractionsController;
import simulator.control.SetupController;
import simulator.model.ActionI;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import simulator.model.entity.FoodEntity;
import simulator.model.entity.individuals.MyIndividual;
import simulator.model.map.Map;


public class ActionEvaluator {
	public static class TestEv{
		public int i=0;
		public int x=4;
		public float f=4;
		public static int ss=9;
		public TestEv2 test2 = new TestEv2();
		public TestEv() {
			
			i++;
		}
		public void testi(TestEv t) {
			i+=t.i;
		}
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
	public static Environment globalEnv;
	static {
		globalEnv = new Environment(null);
	}
	public ActionEvaluator(JSONArray program) {
		this.program = program;
		//System.out.println(program.toString(4));
	}
	//evaluate for all possible parameters...
	public Object evaluate(Environment env, boolean clear) {
		Object r = null;
		JSONObject expression = null;
		for(int i=0;i<program.length();i++) {
			expression = program.getJSONObject(i);
			r = this.eval(expression, env);
		}
		if(clear)env.clear();
		return r;
	}
	public Object evaluate(java.util.Map<String, Object>vars) {
		Environment env = new Environment(globalEnv);
		for(String key:vars.keySet()) env.define(key, vars.get(key));
		env.define("test", new TestEv());
		
		return evaluate(env, true);
	}
	public Object evaluate(Entity e) {
		Environment env = new Environment(globalEnv);
		env.define("e", e);
		env.define("test", new TestEv());
		
		return evaluate(env, true);
	}
	public Object evaluate(Entity e1, Entity e2, Map map) {
		Environment env = new Environment(globalEnv);
		env.define("e1", e1);
		env.define("e2", e2);
		env.define("map", map);
		env.define("test", new TestEv());
		
		return evaluate(env, true);
	}
	public Object evaluate(Entity e, List<Entity>entities, Map map) {
		Environment env = new Environment(globalEnv);
		env.define("e", e);
		env.define("entities", entities);
		env.define("map", map);
		env.define("test", new TestEv());

		return evaluate(env, true);
	}

	private Object eval(JSONObject query, Environment env) {
		Objects.requireNonNull(query);
		String type = query.getString("type");
		
		switch(type) {
		case "ExpressionStatement":
			return this.eval(query.getJSONObject("expression"), env);
		case "ForStatement":
			return this.evalForStatement(query, env);
		case "WhileStatement":
			return this.evalWhileStatement(query, env);
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
		case "StringLiteral":
			return this.evalStringLiteral(query);
		case "EnumIdentifier":
			return this.evalEnumIdentifier(query);
		case "StaticExpression":
			return this.evalStaticExpression(query);
		case "Identifier":
			return this.evalIdentifier(query, env);
		case "EmptyStatement":
			return null;
		default:
			System.err.println("unsupported type: "+type);
			return null;
		}
	}
	private Object evalStringLiteral(JSONObject query) {
		return query.getString("value");
	}
	private Object evalStaticExpression(JSONObject query) {
		String name = query.getString("value");
		try {
			Class clazz = Class.forName(name);
			return clazz;
			//return clazz.getConstructors()[0].newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private Object evalEnumIdentifier(JSONObject query) {
		String name = query.getString("value");
		String[] s = name.split("[\\.$]");
		String value = s[s.length-1];
		try {
			Class clazz = Class.forName(name.substring(0, name.length() - value.length()-1));
			return Enum.valueOf(clazz, value);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("enum not found: "+name);
		return null;
	}
	private Object evalIdentifier(JSONObject query, Environment env) {
		return env.search(query.getString("name"));
	}
	private Object evalWhileStatement(JSONObject query, Environment env) {
		JSONObject test = query.getJSONObject("test");
		JSONObject body = query.getJSONObject("body");
		
		Object r = null;
		while((boolean)eval(test, env)) {
			r = eval(body, env);
		}
		return r;
	}
	private Object evalForStatement(JSONObject query, Environment env) {
		JSONObject init = query.has("init")?query.getJSONObject("init"):null;
		JSONObject test = query.has("test")?query.getJSONObject("test"):null;
		JSONObject update = query.has("update")?query.getJSONObject("update"):null;
		JSONObject body = query.has("body")?query.getJSONObject("body"):null;
		
		Object r = null;
		if(init!=null)eval(init, env);
		for(;test!=null?(boolean)eval(test, env):true;) {
			if(body!=null)r = eval(body, env);
			if(update!=null)eval(update, env);
		}

		return r;
	}
	private Object evalNewExpression(JSONObject query, Environment env) {
		StringBuilder str = new StringBuilder();
		
		JSONArray arguments = query.getJSONArray("arguments");
		
//		Class<?>clazzs[] = new Class<?>[arguments.length()];
//		Object args[] = new Object[arguments.length()];
//		for(int i=0;i<arguments.length();i++) {
//			args[i] = eval(arguments.getJSONObject(i), env);
//			clazzs[i] = args[i].getClass();
//		}
		
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
		if(ob instanceof Class) {
			try {
				Method m = null;
				//Method m = ob.getClass().getMethod(property.getString("name"), clazzs);
				for(Method mi:((Class)ob).getMethods())if(mi.getName().equals(property.getString("name")))m=mi;
				Class<?>prms[] = m.getParameterTypes();
				for(int i=0;i<arguments.length();i++) {
					if(args[i] instanceof Number) {
						if(args[i].getClass()==Double.class) {
							double tmp = ((Number)args[i]).doubleValue();
							if(prms[i]==Integer.class || prms[i]==int.class) {
								args[i] = (int)tmp;
							}
						}
						//args[i] = prms[i].cast(((Number)args[i]).floatValue()) ;
					}
					else args[i] = prms[i].cast(args[i]);
				}
				return m.invoke(null, args);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				Method m = null;
				//Method m = ob.getClass().getMethod(property.getString("name"), clazzs);
				for(Method mi:ob.getClass().getMethods())if(mi.getName().equals(property.getString("name")))m=mi;
				Class<?>prms[] = m.getParameterTypes();
				for(int i=0;i<arguments.length();i++) {
					if(args[i] instanceof Number) {
						if(args[i].getClass()==Double.class) {
							double tmp = ((Number)args[i]).floatValue();
							if(prms[i]==Integer.class || prms[i]==int.class) {
								args[i] = (int)tmp;
							}
						}
						//args[i] = prms[i].cast(((Number)args[i]).floatValue()) ;
					}
					else args[i] = prms[i].cast(args[i]);
				}
				return m.invoke(ob, args);
			} catch (Exception e) {
				System.err.println(query.toString(4));
				e.printStackTrace();
			}
		}
		
		
		return null;
	}
	private Object evalMemberExpression(JSONObject query, Environment env) {
		JSONObject property = query.getJSONObject("property");
		JSONObject object = query.getJSONObject("object");
		boolean computed = query.getBoolean("computed");
		if(!computed) {
			Object ob = this.eval(object, env);
			if(ob instanceof Class) {
				try {
					Field f = ((Class)ob).getField(property.getString("name"));
					return f.get(ob);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				try {
					Field f = ob.getClass().getField(property.getString("name"));
					return f.get(ob);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		JSONObject alternate = query.has("alternate")?query.getJSONObject("alternate"):null;
		
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
			return ((Number)eval(left, env)).doubleValue()>((Number)eval(right, env)).doubleValue();
		case "<":
			return ((Number)eval(left, env)).doubleValue()<((Number)eval(right, env)).doubleValue();
		case "==":
			Object l = eval(left, env);
			if(l instanceof Number)return ((Number)l).doubleValue()==((Number)eval(right, env)).doubleValue();
			else return l == eval(right, env);
		case ">=":
			return ((Number)eval(left, env)).doubleValue()>=((Number)eval(right, env)).doubleValue();
		case "<=":
			return ((Number)eval(left, env)).doubleValue()<=((Number)eval(right, env)).doubleValue();
		case "&&":
			return (boolean)eval(left, env)&&(boolean)eval(right, env);
		case "||":
			return (boolean)eval(left, env)||(boolean)eval(right, env);
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
			return ((Number)eval(left, env)).doubleValue()+((Number)eval(right, env)).doubleValue();
		case "-":
			return ((Number)eval(left, env)).doubleValue()-((Number)eval(right, env)).doubleValue();
		case "*":
			return ((Number)eval(left, env)).doubleValue()*((Number)eval(right, env)).doubleValue();
		case "/":
			return ((Number)eval(left, env)).doubleValue()/((Number)eval(right, env)).doubleValue();
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
		env.assign(left.getString("name"), lefto);
		
		return lefto;
	}

	public static void main(String args[]) {
		//actions();
		interactions();
	}
	private static void actions() {
		SetupController stc = SetupController.from("resources/setup/default.stp");
		ActionsController ac = (ActionsController)stc.getModule("ActionsController");
		
		java.util.Map<String, java.util.Map<String, ActionI>> acs = ac.getActions();
		ActionI a = acs.get("move").get("NEUTRAL");
		
		System.out.println(a.perform(null, null, null));
	}
	private static void interactions() {
		SetupController stc = SetupController.from("resources/setup/default.stp");
		InteractionsController ac = (InteractionsController)stc.getModule("InteractionsController");
		
		java.util.Map<String, InteractionI> acs = ac.getInteractions();
		InteractionI a = acs.get("EAT");
		
		System.out.println(a.perform(null, null, null));
	}
}
