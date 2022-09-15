package grammar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.*;

public class Parser {
	private String _string;
	private Tokenizer _tokenizer;
	private JSONObject _lookahead;
	public Parser() {
		_string = new String();
		_tokenizer = new Tokenizer();
	}
	public JSONObject parse(String string){
		_string = string;
		_tokenizer.init(string);
		
		
		this._lookahead = this._tokenizer.getNextToken();
		return this.Program();
	}
	
	
	private JSONObject Program() {
		return new JSONObject().put("type", "Program").put("body", this.StatementList("null"));
	}
	private JSONArray StatementList(String stop) {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(stop)) {
			arr.put(this.Statement());
		}
		return arr;
	}
	private JSONObject Statement() {
		switch (this._lookahead.getString("type")) {
			case ";":
				return this.EmptyStatement();
			case "if":
				return this.IfStatement();
			case "{":
				return this.BlockStatement();
			case "let":
				return this.VariableStatement();
			case "return":
				return this.ReturnStatement();
			default:
				return this.ExpressionStatement();
		}
	}
	
	private JSONObject ReturnStatement() {
		_eat("return");
		JSONObject expression = this.Expression();
		_eat(";");
		return new JSONObject().put("type", "ReturnStatement")
				   .put("expression", expression);
	}
	private JSONObject IfStatement() {
		_eat("if");
		_eat("(");
		JSONObject test =this.Expression();
		_eat(")");
		JSONObject consequent =this.Statement();//BlockStatement or inline if
		JSONObject alternate = null;
		if(this._lookahead!=null&&this._lookahead.getString("type").equals("else")) {
			_eat("else");
			alternate =this.Statement();
		}
		return new JSONObject().put("type", "IfStatement")
							   .put("test", test)
							   .put("consequent", consequent)
							   .put("alternate", alternate);
	}
	
	private JSONObject VariableStatement() {
		_eat("let");
		JSONObject var = this.VaribleDeclaration();
		_eat(";");
		return new JSONObject().put("type", "VariableStatement")
							   .put("declaration",var);
	}
	private JSONObject VaribleDeclaration() {
		JSONObject id = this.Identifier();
		JSONObject init = !this._lookahead.getString("type").equals(";")?this.VariableInitializer():null;
		return new JSONObject().put("type", "VaribleDeclaration")
							   .put("id", id)
							   .put("init",init);
	}
	private JSONObject VariableInitializer() {
		_eat("SIMPLE_ASSIGN");
		return this.Expression();
	}
	private JSONObject EmptyStatement() {
		_eat(";");
		return new JSONObject().put("type", "EmptyStatement");
	}
	/**
	   * BlockStatement
	   *   : '{' OptStatementList '}'
	   *   ;
	   */
	private JSONObject BlockStatement() {
		_eat("{");
		JSONArray body = !this._lookahead.getString("type").equals("}")?this.StatementList("}"):null;
		_eat("}");
		return new JSONObject().put("type", "BlockStatement")
							   .put("body", body);
	
	}
	/**
	   * ExpressionStatement
	   *   : Expression ';'
	   *   ;
	   */
	private JSONObject ExpressionStatement() {
		JSONObject expression = this.Expression();
		_eat(";");
		return new JSONObject().put("type", "ExpressionStatement").put("expression", expression);
	}
	private JSONObject Expression() {
		return this.AssignmentExpression();
	}
	/**
	   * AssignmentExpression
	   *   : LogicalORExpression
	   *   | LeftHandSideExpression AssignmentOperator AssignmentExpression
	   *   ;
	   */
	private JSONObject AssignmentExpression() {
		JSONObject left = this.LogicalORExpression();
		if(!_isAssigmentOp(this._lookahead.getString("type")))return left;
		return new JSONObject().put("type", "AssignmentExpression")
						       .put("left", _checkValidAssignmentTarget(left))
							   .put("operator", this.AssignmentOperator().getString("value"))
							   .put("right", this.LogicalORExpression());
		
	}
	
	private JSONObject Identifier() {
		return new JSONObject().put("type", "Identifier")
							   .put("name", _eat("IDENTIFIER").getString("value"));
		
	}
	private JSONObject _checkValidAssignmentTarget(JSONObject o) {
		return o.getString("type").equals("Identifier")?o:null;
	}
	private boolean _isAssigmentOp(String s) {
		return s.equals("SIMPLE_ASSIGN")||s.equals("COMPLEX_ASSIGN");
	}
	private JSONObject AssignmentOperator() {
		switch(this._lookahead.getString("type")) {
			case "SIMPLE_ASSIGN":
				return _eat("SIMPLE_ASSIGN");
			default:
				return _eat("COMPLEX_ASSIGN");
		}
	}
	/**
	   * Logical OR expression.
	   *
	   *   x || y
	   *
	   * LogicalORExpression
	   *   : LogicalORExpression
	   *   | LogicalORExpression LOGICAL_OR LogicalANDExpression
	   *   ;
	   */
	private JSONObject LogicalORExpression() {
		JSONObject left = this.LogicalANDExpression();
		JSONObject op = null;
		JSONObject right = null;
		while(this._lookahead.getString("type").equals("LOGICAL_OR")) {
			op = this._eat("LOGICAL_OR");
			right = this.LogicalANDExpression();
			left = new JSONObject().put("type", "LogicalExpression")
								   .put("operator", op.getString("value"))
								   .put("left",left)
								   .put("right", right);
		}
		return left;
	}
	/**
	   * Logical AND expression.
	   *
	   *   x && y
	   *
	   * LogicalANDExpression
	   *   : EqualityExpression
	   *   | LogicalANDExpression LOGICAL_AND EqualityExpression
	   *   ;
	   */
	private JSONObject LogicalANDExpression() {
		JSONObject left = this.EqualityExpression();
		JSONObject op = null;
		JSONObject right = null;
		while(this._lookahead.getString("type").equals("LOGICAL_AND")) {
			op = this._eat("LOGICAL_AND");
			right = this.EqualityExpression();
			left = new JSONObject().put("type", "LogicalExpression")
								   .put("operator", op.getString("value"))
								   .put("left",left)
								   .put("right", right);
		}
		return left;
	}
	/**
	   * EQUALITY_OPERATOR: ==, !=
	   *
	   *   x == y
	   *   x != y
	   *
	   * EqualityExpression
	   *   : RelationalExpression
	   *   | EqualityExpression EQUALITY_OPERATOR RelationalExpression
	   *   ;
	   */
	private JSONObject EqualityExpression() {
		JSONObject left = this.RelationalExpression();
		JSONObject op = null;
		JSONObject right = null;
		while(this._lookahead.getString("type").equals("EQUALITY_OPERATOR")) {
			op = this._eat("EQUALITY_OPERATOR");
			right = this.RelationalExpression();
			left = new JSONObject().put("type", "LogicalExpression")
								   .put("operator", op.getString("value"))
								   .put("left",left)
								   .put("right", right);
		}
		return left;
	}
	/**
	   * RELATIONAL_OPERATOR: >, >=, <, <=
	   *
	   *   x > y
	   *   x >= y
	   *   x < y
	   *   x <= y
	   *
	   * RelationalExpression
	   *   : AdditiveExpression
	   *   | RelationalExpression RELATIONAL_OPERATOR AdditiveExpression
	   *   ;
	   */
	private JSONObject RelationalExpression() {
		JSONObject left = this.AdditiveExpression();
		JSONObject op = null;
		JSONObject right = null;
		while(this._lookahead.getString("type").equals("RELATIONAL_OPERATOR")) {
			op = this._eat("RELATIONAL_OPERATOR");
			right = this.AdditiveExpression();
			left = new JSONObject().put("type", "LogicalExpression")
								   .put("operator", op.getString("value"))
								   .put("left",left)
								   .put("right", right);
		}
		return left;
	}
	/**
	   * AdditiveExpression
	   *   : MultiplicativeExpression
	   *   | AdditiveExpression ADDITIVE_OPERATOR MultiplicativeExpression
	   *   ;
	   */
	private JSONObject AdditiveExpression() {
		JSONObject left = this.MultiplicativeExpression();
		JSONObject op = null;
		JSONObject right = null;
		while(this._lookahead.getString("type").equals("ADDITIVE_OPERATOR")) {
			op = this._eat("ADDITIVE_OPERATOR");
			right = this.MultiplicativeExpression();
			left = new JSONObject().put("type", "BinaryExpression")
								   .put("operator", op.getString("value"))
								   .put("left",left)
								   .put("right", right);
		}
		return left;
	}
	 /**
	   * MultiplicativeExpression
	   *   : UnaryExpression
	   *   | MultiplicativeExpression MULTIPLICATIVE_OPERATOR UnaryExpression
	   *   ;
	   */
	private JSONObject MultiplicativeExpression() {
		JSONObject left = this.UnaryExpression();
		JSONObject op = null;
		JSONObject right = null;
		while(this._lookahead.getString("type").equals("MULTIPLICATIVE_OPERATOR")) {
			op = this._eat("MULTIPLICATIVE_OPERATOR");
			right = this.UnaryExpression();
			left = new JSONObject().put("type", "BinaryExpression")
							       .put("operator", op.getString("value"))
								   .put("left",left)
								   .put("right", right);
		}
		return left;
	}
	/**
	   * UnaryExpression
	   *   : LeftHandSideExpression
	   *   | ADDITIVE_OPERATOR UnaryExpression
	   *   | LOGICAL_NOT UnaryExpression
	   *   ;
	   */
	private JSONObject UnaryExpression() {
		String op=null;
		switch(this._lookahead.getString("type")) {
			case "ADDITIVE_OPERATOR":
				op=_eat("ADDITIVE_OPERATOR").getString("value");
				break;
			case "LOGICAL_NOT":
				op=_eat("LOGICAL_NOT").getString("value");
				break;
			
		}
		if(op!=null) {
			return new JSONObject().put("type", "UnaryExpression")
								   .put("operator", op)
								   .put("argument", this.UnaryExpression());
		}
		return this.LeftHandSideExpression();
	}
	/**
	   * LeftHandSideExpression
	   *   : CallMemberExpression
	   *   ;
	   */
	private JSONObject LeftHandSideExpression() {
		return this.PrimaryExpression();
	}
	/**
	   * PrimaryExpression
	   *   : Literal
	   *   | ParenthesizedExpression
	   *   | Identifier
	   *   ;
	   */
	private JSONObject PrimaryExpression() {
		if(_isLiteral(this._lookahead.getString("type")))return this.Literal();
		switch(this._lookahead.getString("type")) {
			case "(":
				return this.ParenthesizedExpression();
			case "IDENTIFIER":
				return this.Identifier();
			default:
				return this.LeftHandSideExpression();
		}
	}
	private boolean _isLiteral(String s) {
		return s.equals("NUMBER")||s.equals("STRING")||s.equals("true")||s.equals("false")||s.equals("null");
	}
	/**
	   * ParenthesizedExpression
	   *   : '(' Expression ')'
	   *   ;
	   */
	private JSONObject ParenthesizedExpression() {
		_eat("(");
		JSONObject e = this.Expression();
		_eat(")");
		return e;
	}
	/**
	   * Literal
	   *   : NumericLiteral
	   *   | StringLiteral
	   *   | BooleanLiteral
	   *   | NullLiteral
	   *   ;
	   */
	private JSONObject Literal() {
		switch (this._lookahead.getString("type")) {
			case "NUMBER":
				return this.NumberLiteral();
			case "STRING":
				return this.StringLiteral();
			case "true":
				return this.BooleanLiteral("true");
			case "false":
				return this.BooleanLiteral("false");
			case "null":
				return this.NullLiteral();
		}
		System.err.println("error");
		return null;
	}
	private JSONObject BooleanLiteral(String value) {
		JSONObject token = _eat(value);
		return new JSONObject().put("type", "BooleanLiteral").put("value", token.getString("value"));
	}
	private JSONObject NullLiteral() {
		JSONObject token = _eat("null");
		return new JSONObject().put("type", "NullLiteral").put("value", token.getString("value"));
	}
	private JSONObject NumberLiteral() {
		JSONObject token = _eat("NUMBER");
		return new JSONObject().put("type", "NumberLiteral").put("value", token.getString("value"));
	}
	private JSONObject StringLiteral() {
		JSONObject token = _eat("STRING");
		return new JSONObject().put("type", "StringLiteral").put("value", token.getString("value").substring(1, token.getString("value").length()-1));
	}
	private JSONObject _eat(String type) {
		JSONObject token=_lookahead;
		if(this._lookahead==null) {
			System.err.println("unex end of input");
			return null;
		}
		if(!this._lookahead.getString("type").equals(type)) {
			System.err.println("unexpected "+this._lookahead.getString("type")+" expected "+type);
			return null;
		}
		this._lookahead=_tokenizer.getNextToken();
		return token;
	}
	
	public static void main(String args[]) {
		String e = "if (u>d){\n"
				+ "   up=true;\n"
				+ "}\n"
				+ "else{\n"
				+ "   if(d&&true){\n"
				+ "       down=true;\n"
				+ "   }\n"
				+ "}\n"
				+ "\n"
				+ "if (r>l){\n"
				+ "   right=true;\n"
				+ "}\n"
				+ "else{\n"
				+ "   if(l||true){\n"
				+ "       left=true;\n"
				+ "   }\n"
				+ "   else{\n"
				+ "       neutral=true;\n"
				+ "   }\n"
				+ "}";
		String e2 = "if(distLEFT>p[7]RIGHT){return DOWN;}";
		Parser parser = new Parser();
		System.out.println(parser.parse(e2).toString(4));
		
		
		/*Pattern p = Pattern.compile("^\\blet\\b");
		Matcher m = p.matcher(" let x =12;"); 
		System.out.println(m.find());*/
		//System.out.println(m.end());
	}
}
