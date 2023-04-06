package setup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;



public class InteractionsParser extends OOPParser{

	public InteractionsParser() {
		_tokenizer = new InteractionsTokenizer();
	}
	
	@Override
	protected JSONObject Program() {
		this._eat(":=");
		return new JSONObject().put("list", this.InteractionsList());
	}

	protected JSONArray InteractionsList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(".")) {
			arr.put(this.InteractionDeclaration());
			if(this._lookahead.getString("type").equals("|")) _eat("|");
		}
		this._eat(".");
		return arr;
	}

	protected JSONObject InteractionDeclaration() {
		String name = this._eat("IDENTIFIER").getString("value");
		
		this._eat("(");
		int freq = 1;
		if(this._lookahead.getString("type").equals("NUMBER")) {
			freq = this._eat("NUMBER").getInt("value");
			this._eat(")");
			this._eat("(");
		}
		
		JSONArray clazzsFrom = this.ClassList();
		if(this._lookahead.getString("type").equals("->"))this._eat("->");
		JSONArray clazzsTo = this.ClassList();
	
		this._eat(")");

		this._eat("{");
		JSONArray spec = this.Especification();
		this._eat("}");
		return new JSONObject().put("type", "declaration")
				   			   .put("clazzsFrom", clazzsFrom)
				   			   .put("clazzsTo", clazzsTo)
							   .put("spec", spec)
							   .put("name", name)
							   .put("freq", freq)
							  ;
	}
	protected JSONArray ClassList() {
		JSONArray clazzs = new JSONArray();
		while(this._lookahead != null && !this._lookahead.getString("type").equals(")")&& !this._lookahead.getString("type").equals("->") && !(this._lookahead.getString("type").equals(",") && this._eat(",")==null)) {
			clazzs.put(this.StringLiteral());
		}
		return clazzs;
	}
	

}
