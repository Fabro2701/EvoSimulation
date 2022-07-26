package grammar;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.Constants.MOVE;

public class Evaluator {
	JSONArray statements;
	int current;
	public Evaluator(JSONObject program){
		statements = program.getJSONArray("body");
		current=0;
	}
	public MOVE getNext() {
		while(true) {
			JSONObject query = statements.getJSONObject(current);
			
			String result=this._evaluate(query);
			
			System.out.println(result);
			
			current++;
			current%=statements.length();
			
			try {
				MOVE move = MOVE.valueOf(result);
				System.out.println("-");
				return move;
			}catch (IllegalArgumentException e) {
				
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
			//put all body statements into the statements ArrayList (create one) and mark then 
			//when the iterator ends the array delete the marked statements
			
			//another idea: do the written before and each marked statement has also a list of conditions
			//that have been tested positive in all the father blocks
			
			//all this to preserve a more accurate and reliable cursor of the program
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
	public static void main(String args[]) {
		String test1 = "3+3*5+3;\"RIGHT\";true&&false||true;if(true){\"LEFT\";}";
		Parser parser = new Parser();
		JSONObject program = parser.parse(test1);
		System.out.println(program.toString(4));
		
		Evaluator evaluator = new Evaluator(program);
		evaluator.getNext();
		evaluator.getNext();
		evaluator.getNext();
		evaluator.getNext();
	}
}
