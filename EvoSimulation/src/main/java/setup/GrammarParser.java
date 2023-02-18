package setup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;



public class GrammarParser extends ChildParser{

	public GrammarParser() {
		_tokenizer = new GrammarTokenizer();
	}
	
	@Override
	protected JSONObject Program() {
		this._eat(":=");
		return new JSONObject().put("list", this.GrammarsList());
	}

	private JSONArray GrammarsList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(".")) {
			arr.put(this.GrammarAttributes());
			if(this._lookahead.getString("type").equals("|")) _eat("|");
		}
		this._eat(".");
		return arr;
	}

	private JSONObject GrammarAttributes() {
		String name = this._eat("IDENTIFIER").getString("value");
		this._eat("(");
		JSONObject time = this._eat("NUMBER");
		this._eat(")");
		this._eat(";");
		String clazz = this.StringLiteral().getString("value");
		this._eat(";");
		String path = this.StringLiteral().getString("value");
		return new JSONObject().put("type", "attributes")
							   .put("name", name)
							   .put("time", time.get("value"))
							   .put("clazz", clazz)
							   .put("path", path);
	}

	private JSONObject StringLiteral() {
		String literal = _eat("Literal").getString("value");
		if(literal.charAt(0) == '"')return new JSONObject().put("type", "StringLiteral").put("value", literal.substring(1, literal.length()-1));

		return new JSONObject().put("type", "StringLiteral").put("value", literal);
	}

}
