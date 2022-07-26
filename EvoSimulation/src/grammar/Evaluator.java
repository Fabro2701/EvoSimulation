package grammar;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.Constants.MOVE;

public class Evaluator {
	ArrayList<JSONObject>_statements;
	HashMap<String,String>_variables;//for simplicity all variables are global
	int _current;
	public Evaluator(JSONObject program){
		_variables = new HashMap<String,String>();
		
		_statements = new ArrayList<JSONObject>();
		for(Object o:program.getJSONArray("body")) {
			_statements.add((JSONObject)o);
		}
		_current=0;
	}
	public MOVE getNext() {
		while(true) {
			if(_current == 0) _deleteMarkedStatements();
			JSONObject query = _statements.get(_current);
			
			String result=this._evaluate(query);
			
			//System.out.println(result);
			
			_current++;
			_current%=_statements.size();
			
			try {
				MOVE move = MOVE.valueOf(result);
				//System.out.println("-");
				return move;
			}catch (Exception e) {
				
			}
			
		}
	}
	private void _deleteMarkedStatements() {
		for(int i=0;i<_statements.size();i++) {
			JSONObject o = _statements.get(i);
			if(o.has("marked")) {
				_statements.remove(i);
				i--;
			}
		}
	}
	private String _evaluate(JSONObject query) {
		String type = query.getString("type");
		if(type.contains("Literal")) {
			return query.getString("value");
		}
		if(type.equals("ExpressionStatement")) {
			return _evaluate(query.getJSONObject("expression"));
		}
		if(type.equals("BinaryExpression")) {
			String operator = query.getString("operator");
			JSONObject left = query.getJSONObject("left");
			JSONObject right = query.getJSONObject("right");
			return _binaryEvaluation(left,operator,right);
		}
		if(type.equals("LogicalExpression")) {
			String operator = query.getString("operator");
			JSONObject left = query.getJSONObject("left");
			JSONObject right = query.getJSONObject("right");
			return _logicalEvaluation(left,operator,right);
		}
		
		if(type.equals("IfStatement")) {
			JSONObject test = query.getJSONObject("test");
			JSONObject consequent = query.getJSONObject("consequent");
			JSONObject alternate = null;
			if(query.has("alternate"))alternate = query.getJSONObject("alternate");
			return _ifEvaluation(test,consequent,alternate);
		}
		if(type.equals("BlockStatement")) {
			JSONArray body = query.getJSONArray("body");
		
			for(int i=0;i<body.length();i++) {
				
				this._statements.add(_current+i+1,body.getJSONObject(i).put("marked", true));
			}
		
			//pending...
			//another idea: do the written before and each marked statement has also a list of conditions
			//that have been tested positive in all the father blocks
			
			//all this to preserve a more accurate and reliable cursor of the program
			//use a queue to store the test conditions 
			return null;
		}
		if(type.equals("Identifier")) {
			String name = query.getString("name");
			return _variables.get(name);
		}
		return null;
	}
	private String _ifEvaluation(JSONObject test, JSONObject consequent, JSONObject alternate) {
		Boolean cond = Boolean.valueOf(this._evaluate(test));
		if(cond) return this._evaluate(consequent);
		else if(alternate!=null)return this._evaluate(alternate);
		else return null;
	}
	private String _logicalEvaluation(JSONObject left, String operator, JSONObject right) {
		Boolean result=null;
		switch (operator) {
			case "||":
				result = Boolean.valueOf(_evaluate(left)) || Boolean.valueOf(_evaluate(right));
				break;
			case "&&":
				result = Boolean.valueOf(_evaluate(left)) && Boolean.valueOf(_evaluate(right));
				break;
			case "<":
				result = Integer.valueOf(_evaluate(left)) < Integer.valueOf(_evaluate(right));
				break;
			case "<=":
				result = Integer.valueOf(_evaluate(left)) <= Integer.valueOf(_evaluate(right));
				break;
			case ">":
				result = Integer.valueOf(_evaluate(left)) > Integer.valueOf(_evaluate(right));
				break;
			case ">=":
				result = Integer.valueOf(_evaluate(left)) >= Integer.valueOf(_evaluate(right));
				break;
			case "==":
				result = Integer.valueOf(_evaluate(left)) == Integer.valueOf(_evaluate(right));
				break;
		}
		return result.toString();
	}
	private String _binaryEvaluation(JSONObject left, String operator, JSONObject right) {
		Integer result=null;
		switch (operator) {
			case "+":
				result = Integer.valueOf(_evaluate(left)) + Integer.valueOf(_evaluate(right));
				break;
			case "-":
				result = Integer.valueOf(_evaluate(left)) - Integer.valueOf(_evaluate(right));
				break;
			case "*":
				result = Integer.valueOf(_evaluate(left)) * Integer.valueOf(_evaluate(right));
				break;
			case "/":
				result = Integer.valueOf(_evaluate(left)) / Integer.valueOf(_evaluate(right));
				break;
		}
		return result.toString();
		
	}
	public void addObservations(HashMap<String,String>obs) {
		this._variables.putAll(obs);
	}
	public static void main(String args[]) {
		String test1 = "3+3*(5+3); \"RIGHT\"; true&&false||true;if(3+5<9){\"LEFT\";\"DOWN\";"
				+ "if(x){3+2;}else{1;}}";
		Parser parser = new Parser();
		JSONObject program = parser.parse(test1);
		//System.out.println(program.toString(4));
		
		Evaluator evaluator = new Evaluator(program);
		evaluator._variables.put("x", "false");
		evaluator.getNext();
		evaluator.getNext();
		evaluator.getNext();
		evaluator.getNext();
		evaluator.getNext();
	}
}
