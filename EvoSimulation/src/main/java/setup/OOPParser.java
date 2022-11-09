package setup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;


public abstract class OOPParser extends ChildParser{

	public OOPParser() {
	}

	protected JSONArray Especification() {
		return StatementList("}");
	}
	protected JSONArray StatementList(String stop) {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(stop)) {
			arr.put(this.Statement());
		}
		return arr;
	}
	protected JSONObject Statement() {
		switch (this._lookahead.getString("type")) {
			case ";":
				return this.EmptyStatement();
			case "if":
				return this.IfStatement();
			case "{":
				return this.BlockStatement();
			case "let":
				return this.VariableStatement();
			case "while":case "for":
				return this.IterationStatement();
			case "return":
				return this.ReturnStatement();
			default:
				return this.ExpressionStatement();
		}
	}

	protected JSONObject IterationStatement() {
		switch (this._lookahead.getString("type")) {
		case "while":
			return this.WhileStatement();
		case "for":
			return this.ForStatement();
		default:
			return null;
		}
	}

	protected JSONObject ForStatement() {
		this._eat("for");
		this._eat("(");
		JSONObject init = !this._lookahead.getString("type").equals(";")?this.ForStatementInit():null;
		this._eat(";");
		JSONObject test = !this._lookahead.getString("type").equals(";")?this.Expression():null;
		this._eat(";");
		JSONObject update = !this._lookahead.getString("type").equals(")")?this.Expression():null;
		this._eat(")");
		JSONObject body = this.Statement();
		return new JSONObject().put("type", "ForStatement")
							   .put("init", init)
							   .put("test", test)
							   .put("update", update)
							   .put("body", body);
	}

	protected JSONObject ForStatementInit() {
		if(this._lookahead.getString("type").equals("let")) {
			return this.VariableStatement();
		}
		return this.Statement();
	}

	protected JSONObject WhileStatement() {
		this._eat("while");
		this._eat("(");
		JSONObject test = this.Expression();
		this._eat(")");
		JSONObject body = this.Statement();
		return new JSONObject().put("type", "WhileStatement")
							   .put("test", test)
							   .put("body", body);
	}

	protected JSONObject ReturnStatement() {
		_eat("return");
		JSONObject expression = this.Expression();
		_eat(";");
		return new JSONObject().put("type", "ReturnStatement")
				   .put("expression", expression);
	}
	protected JSONObject IfStatement() {
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
	protected JSONObject VariableStatement() {
		_eat("let");
		JSONObject var = this.VaribleDeclaration();
		_eat(";");
		return new JSONObject().put("type", "VariableStatement")
							   .put("declaration",var);
	}
	protected JSONObject VaribleDeclaration() {
		JSONObject id = this.Identifier();
		JSONObject init = !this._lookahead.getString("type").equals(";")?this.VariableInitializer():null;
		return new JSONObject().put("type", "VaribleDeclaration")
							   .put("id", id)
							   .put("init",init);
	}
	protected JSONObject VariableInitializer() {
		_eat("SIMPLE_ASSIGN");
		return this.Expression();
	}
	protected JSONObject EmptyStatement() {
		_eat(";");
		return new JSONObject().put("type", "EmptyStatement");
	}
	protected JSONObject BlockStatement() {
		_eat("{");
		JSONArray body = !this._lookahead.getString("type").equals("}")?this.StatementList("}"):null;
		_eat("}");
		return new JSONObject().put("type", "BlockStatement")
							   .put("body", body);
	
	}
	protected JSONObject ExpressionStatement() {
		JSONObject expression = this.Expression();
		_eat(";");
		return new JSONObject().put("type", "ExpressionStatement").put("expression", expression);
	}
	protected JSONObject Expression() {
		return this.AssignmentExpression();
	}
	protected JSONObject AssignmentExpression() {
		JSONObject left = this.LogicalORExpression();
		if(!_isAssigmentOp(this._lookahead.getString("type")))return left;
		return new JSONObject().put("type", "AssignmentExpression")
						       .put("left", _checkValidAssignmentTarget(left))
							   .put("operator", this.AssignmentOperator().getString("value"))
							   .put("right", this.LogicalORExpression());
		
	}
	protected JSONObject Identifier() {
		return new JSONObject().put("type", "Identifier")
							   .put("name", _eat("IDENTIFIER").getString("value"));
		
	}
	protected JSONObject _checkValidAssignmentTarget(JSONObject o) {
		return o.getString("type").equals("Identifier")||o.getString("type").equals("MemberExpression")?o:null;
	}
	protected boolean _isAssigmentOp(String s) {
		return s.equals("SIMPLE_ASSIGN")||s.equals("COMPLEX_ASSIGN");
	}
	protected JSONObject AssignmentOperator() {
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
	protected JSONObject LogicalORExpression() {
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
	protected JSONObject LogicalANDExpression() {
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
	protected JSONObject EqualityExpression() {
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
	protected JSONObject RelationalExpression() {
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
	protected JSONObject AdditiveExpression() {
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
	protected JSONObject MultiplicativeExpression() {
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
	protected JSONObject UnaryExpression() {
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
	protected JSONObject LeftHandSideExpression() {
		return this.CallMemberExpression();
	}
	protected JSONObject CallMemberExpression() {
		JSONObject member = this.MemberExpression();
		if(this._lookahead.getString("type").equals("(")) {
			return this._CallMemberExpression(member);
		}
		return member;
	}

	protected JSONObject _CallMemberExpression(JSONObject callee) {
		JSONObject callExpression = new JSONObject().put("type", "CallExpression")
													.put("callee", callee)
													.put("arguments", this.Arguments());
		if(this._lookahead.getString("type").equals("(")) {
			callExpression = this._CallMemberExpression(callExpression);
		}
		return callExpression;
	}

	protected JSONArray Arguments() {
		this._eat("(");
		JSONArray argumentList = !this._lookahead.getString("type").equals(")")?this.ArgumentList():new JSONArray();
		this._eat(")");
		return argumentList;
	}

	protected JSONArray ArgumentList() {
		JSONArray argumentList = new JSONArray();
		do {//we know it has at least one
			argumentList.put(this.AssignmentExpression());
		}while(this._lookahead.getString("type").equals(",")&&this._eat(",")!=null);
		return argumentList;
	}

	protected JSONObject MemberExpression() {
		JSONObject object = this.PrimaryExpression();
		while(this._lookahead.getString("type").equals(".")||this._lookahead.getString("type").equals("[")) {
			if(this._lookahead.getString("type").equals(".")) {
				this._eat(".");
				JSONObject property = this.Identifier();
				object = new JSONObject().put("type", "MemberExpression")
										 .put("computed", false)
										 .put("object", object)
										 .put("property", property);
			}
			if(this._lookahead.getString("type").equals("[")) {
				this._eat("[");
				JSONObject property = this.Expression();
				this._eat("]");
				object = new JSONObject().put("type", "MemberExpression")
										 .put("computed", true)
										 .put("object", object)
										 .put("property", property);
			}
		}
		return object;
	}

	/**
	   * PrimaryExpression
	   *   : Literal
	   *   | ParenthesizedExpression
	   *   | Identifier
	   *   ;
	   */
	protected JSONObject PrimaryExpression() {
		if(_isLiteral(this._lookahead.getString("type")))return this.Literal();
		switch(this._lookahead.getString("type")) {
			case "enum":
				return this.EnumIdentifier();
			case "static":
				return this.StaticExpression();
			case "(":
				return this.ParenthesizedExpression();
			case "IDENTIFIER":
				return this.Identifier();
			case "new":
				return this.NewExpression();
			default:
				return this.LeftHandSideExpression();
		}
	}
	protected JSONObject StaticExpression() {
		this._eat("static");
		JSONObject o = this._eat("STRING");

		return new JSONObject().put("type", "StaticExpression")
				.put("value", o.getString("value").substring(1, o.getString("value").length()-1));
	}

	protected JSONObject EnumIdentifier() {
		this._eat("enum");
		JSONObject o = this._eat("STRING");

		return new JSONObject().put("type", "EnumIdentifier")
				.put("value", o.getString("value").substring(1, o.getString("value").length()-1));
	}

	protected JSONObject NewExpression() {
		this._eat("new");
		
		return new JSONObject().put("type", "NewExpression")
							//   .put("callee", this.Identifier())
							   .put("callee", this.MemberExpression())
							   .put("arguments", this.Arguments());
	}

	protected boolean _isLiteral(String s) {
		return s.equals("NUMBER")||s.equals("STRING")||s.equals("true")||s.equals("false")||s.equals("null");
	}
	/**
	   * ParenthesizedExpression
	   *   : '(' Expression ')'
	   *   ;
	   */
	protected JSONObject ParenthesizedExpression() {
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
	protected JSONObject Literal() {
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
	protected JSONObject BooleanLiteral(String value) {
		JSONObject token = _eat(value);
		return new JSONObject().put("type", "BooleanLiteral").put("value", token.getString("value"));
	}
	protected JSONObject NullLiteral() {
		JSONObject token = _eat("null");
		return new JSONObject().put("type", "NullLiteral").put("value", token.getString("value"));
	}
	protected JSONObject NumberLiteral() {
		JSONObject token = _eat("NUMBER");
		JSONObject o = new JSONObject().put("type", "NumberLiteral").put("value", token.getString("value"));
		if(token.getString("value").contains("f")) {
			o.put("class", Float.class.getName());
		}
		else if(token.getString("value").contains("d")||token.getString("value").contains(".")) {
			o.put("class", Double.class.getName());
		}
		else {
			o.put("class", Integer.class.getName());
		}
		return o;
	}
	protected JSONObject StringLiteral() {
		JSONObject token = _eat("STRING");
		return new JSONObject().put("type", "StringLiteral").put("value", token.getString("value").substring(1, token.getString("value").length()-1));
	}
}
