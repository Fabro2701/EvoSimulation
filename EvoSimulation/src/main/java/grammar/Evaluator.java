package grammar;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.Constants.ACTION;
import simulator.Constants.MOVE;

/**
 * Evaluator evaluates a given AST and returns the next MOVE storing the cursor of the program
 * 
 * Another implementation idea would be when initializing create a expanded version of the received Program
 * all being 'pure' staments (no if's) each one stores a JSONArray of conditions in a similar way as done before
 * this expanded statements never get deleted and the main pipeline will only test the conditions of each statement
 * if the test returns false then just increment the cursor
 * @author fabrizioortega
 *
 */
public class Evaluator {
	
	//List of statements in order to be executed 
	ArrayList<JSONObject>_statements;
	HashMap<String,String>_variables;//for simplicity all variables are global
	//the program cursor
	int _current;
	
	/**
	 * Reads a AST in JSON format
	 * @param program
	 */
	public Evaluator(JSONObject program){
		_variables = new HashMap<String,String>();
		
		_statements = new ArrayList<JSONObject>();
		for(Object o:program.getJSONArray("body")) {
			_statements.add((JSONObject)o);
		}
		_current=0;
	}
	/**
	 * Returns the next result in the program given the internal cursor 
	 * @return
	 */
	public String getNextResult() {
		//cont tracks the number of statements processed 
		int cont=0;
		while(true) {
			if(_current == 0) this._deleteMarkedStatements();//we clean up all the temporary statements 
			
			JSONObject query = _statements.get(_current);
			
			//retest marked statements with index >=_current  and delete those who fails
			int deletions = this._deleteInvalidatedStatements(_current);
			if(deletions>0) {
				_current%=_statements.size();
				//_current--;//we go back the previous statement (not really beacuse we have to advande to the next)
				return getNextResult();//we call getNext again to make sure the next statement is retested
			}
			
			//result could be null or a Literal which in this case corresponds to an MOVE
			String result=this._evaluate(query);
			
			_current++;
			_current%=_statements.size();
			
			cont++;
			//there could be a program with a no guaranteed Literal to be reached, this way we avoid infinite loop
			if(cont>=100)return null;
			
			if(result!=null)return result;
			
		}
	}
//	public MOVE getNextMove() {
//		String result = this.getNextResult();
//		try {
//			MOVE move = MOVE.valueOf(result);
//			return move;
//		}catch (Exception e) {}
//		return MOVE.NEUTRAL;
//	}
	public Object getNextAction() {
		
		String result = this.getNextResult();
		return result;
	}
	/**
	 * Delete marked statements (with index >= idx) whose conditions are no longer true 
	 * @param idx
	 * @return
	 */
	private int _deleteInvalidatedStatements(int idx) {
		int deletions=0;
		for(int i=idx;i<_statements.size();i++) {
			JSONObject o = _statements.get(i);
			if(o.has("marked")) {
				JSONArray conds = o.getJSONArray("conditions");
				for(int j=0;j<conds.length();j++) {
					if(!_BooleanValueOf(_evaluate(conds.getJSONObject(j)))) {
						_statements.remove(i);
						i--;
						deletions++;
						break;
					}
				}
				
			}
		}
		return deletions;
	}
	/**
	 * Delete all marked statements
	 */
	private void _deleteMarkedStatements() {
		for(int i=0;i<_statements.size();i++) {
			JSONObject o = _statements.get(i);
			if(o.has("marked")) {
				_statements.remove(i);
				i--;
			}
		}
	}
	/**
	 * Evaluates the given query delegating the evaluation depending on the statement type
	 * @param query
	 * @return
	 */
	private String _evaluate(JSONObject query) {
//		System.out.println(query.toString(4));
//		for(String k:this._variables.keySet()) {
//			System.out.print(this._variables.get(k)+" ");
//		}System.out.println();
		String type = query.getString("type");
		
		//this is our returning statements in this case a MOVE
		if(type.contains("Literal")) {
			return query.getString("value");
		}

		//Return expression
		if(type.equals("ReturnStatement")) {
			return query.getJSONObject("expression").getString("name");
			//return _evaluate(query.getJSONObject("expression"));
		}
		
		//Normal expression
		if(type.equals("ExpressionStatement")) {
			return _evaluate(query.getJSONObject("expression"));
		}
		
		//Binary expression: left operator right
		if(type.equals("BinaryExpression")) {
			String operator = query.getString("operator");
			JSONObject left = query.getJSONObject("left");
			JSONObject right = query.getJSONObject("right");
			return _binaryEvaluation(left,operator,right);
		}
		
		//Logical expression: left operator right
		if(type.equals("LogicalExpression")) {
			String operator = query.getString("operator");
			JSONObject left = query.getJSONObject("left");
			JSONObject right = query.getJSONObject("right");
			return _logicalEvaluation(left,operator,right);
		}
		
		//If Statement
		//if(test) consequent
		//else alternate
		if(type.equals("IfStatement")) {
			JSONObject test = query.getJSONObject("test");
			
			//we put test into consequent conditions JSONArray to keep track of the parent condition
			JSONObject consequent = query.getJSONObject("consequent");
			this._addPreCondition(consequent, query, test);
			
			JSONObject alternate = null;
			if(query.has("alternate")) {
				//we do the same with the alternate but putting a !
				JSONObject newtest = new JSONObject().put("argument", test)
						 							 .put("type", "UnaryExpression")
						 							 .put("operator", "!");
				
				alternate = query.getJSONObject("alternate");
				this._addPreCondition(alternate, query, newtest);
			}
			return _ifEvaluation(test,consequent,alternate);
		}
		
		//Block Statement 
		if(type.equals("BlockStatement")) {
			JSONArray body = query.getJSONArray("body");
		
			for(int i=0;i<body.length();i++) {
				//we put each of the block statements temporally in _statements
				//they are marked and has his parent conditions
				this._statements.add(_current+i+1,body.getJSONObject(i).put("marked", true)
																	   .put("conditions", query.getJSONArray("conditions"))
									);
				
			}
			return null;
		}
		
		//Unary Expression
		// operator (argument)
		if(type.equals("UnaryExpression")) {
			JSONObject argument = query.getJSONObject("argument");
			String operator = query.getString("operator");
			return this._unaryEvaluation(argument, operator);
		}
		
		//Identifier are the variables
		if(type.equals("Identifier")) {
			String name = query.getString("name");
			return _variables.get(name);//we look up them directly on our map
		}
		return null;
	}
	
	/**
	 * Add the condition 'test' along with the query (parent) conditions into o
	 * @param o JSONObject were the conditions are put
	 * @param query Parent 
	 * @param test New condition
	 */
	private void _addPreCondition(JSONObject o, JSONObject query, JSONObject test) {
		JSONArray arr = null;
		if(query.has("conditions")) {
			arr = query.getJSONArray("conditions");
			o.put("conditions", new JSONArray(arr).put(test));
		}
		else {//this is the first condition to be set
			arr = new JSONArray();
			arr.put(test);
			o.put("conditions", arr);
		}
		
	}
	/**
	 * Evaluates if statement 
	 * @param test
	 * @param consequent
	 * @param alternate
	 * @return
	 */
	private String _ifEvaluation(JSONObject test, JSONObject consequent, JSONObject alternate) {
		Boolean cond = _BooleanValueOf(this._evaluate(test));
		if(cond) {
			return this._evaluate(consequent);
		}
		else if(alternate!=null) {
			return this._evaluate(alternate);
		}
		else return null;
	}
	/**
	 * Evaluates logical operation 
	 * @param left
	 * @param operator
	 * @param right
	 * @return
	 */
	private String _logicalEvaluation(JSONObject left, String operator, JSONObject right) {
		Boolean result=null;
		switch (operator) {
			case "||":
				result = _BooleanValueOf(_evaluate(left)) || _BooleanValueOf(_evaluate(right));
				break;
			case "&&":
				result = _BooleanValueOf(_evaluate(left)) && _BooleanValueOf(_evaluate(right));
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
	/**
	 * Evaluates binary operations
	 * @param left
	 * @param operator
	 * @param right
	 * @return
	 */
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
	/**
	 * Evaluates unary operations
	 * @param argument
	 * @param operator
	 * @return
	 */
	private String _unaryEvaluation(JSONObject argument, String operator) {
		if(operator.equals("!")) {
			return String.valueOf(!_BooleanValueOf(this._evaluate(argument)));
		}
		return null;
	}
	/**
	 * Alternative to Boolean.valueOf() 
	 * This method evaluates true the numbers>0
	 * @param s
	 * @return
	 */
	private boolean _BooleanValueOf(String s) {
		switch(s) {
			case "true":
				return true;
			case "false":
				return false;
			default:
				return Integer.valueOf(s)>0;
		}
	}
	/**
	 * Add observations to the global variable environment
	 * @param obs
	 */
	public void addObservations(HashMap<String,String>obs) {
		this._variables.putAll(obs);
	}
	public static void main(String args[]) {
		String test1 = "if(true){\n"
					 + "	return RIGHT;\n"
					 + "}";
		Parser parser = new Parser();
		JSONObject program = parser.parse(test1);
		System.out.println(program.toString(4));
		
		Evaluator evaluator = new Evaluator(program);
		evaluator._variables.put("RIGHT", "RIGHT");
		evaluator._variables.put("y", "true");
		System.out.println("Resulting move1: "+evaluator.getNextResult());
		System.out.println("Resulting move2: "+evaluator.getNextResult());
		evaluator._variables.put("y", "true");
		for(JSONObject o:evaluator._statements) {
			//System.out.println(o.toString(4));
		}
		System.out.println("Resulting move3: "+evaluator.getNextResult());
		System.out.println("Resulting move4: "+evaluator.getNextResult());
		System.out.println("Resulting move5: "+evaluator.getNextResult());
		System.out.println("Resulting move6: "+evaluator.getNextResult());
		System.out.println("Resulting move7: "+evaluator.getNextResult());
	}
}
