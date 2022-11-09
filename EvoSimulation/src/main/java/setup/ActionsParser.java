package setup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;



public class ActionsParser extends OOPParser{

	public ActionsParser() {
		_tokenizer = new ActionsTokenizer();
	}
	
	@Override
	protected JSONObject Program() {
		this._eat(":=");
		return new JSONObject().put("list", this.ActionsList());
	}

	private JSONArray ActionsList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(".")) {
			arr.put(this.ActionDeclaration());
			if(this._lookahead.getString("type").equals("|")) _eat("|");
		}
		this._eat(".");
		return arr;
	}

	private JSONObject ActionDeclaration() {
		String id = this._eat("ACTION_ID").getString("value");
		id = id.substring(1, id.length()-1);
		
		JSONArray arr = new JSONArray();
		while(this._lookahead != null && !(this._lookahead.getString("type").equals(".")||this._lookahead.getString("type").equals("|"))) {
			arr.put(this.ActionEspecification());
		}
		return new JSONObject().put("id", id)
							   .put("actions", arr);
	}
	private JSONObject ActionEspecification() {
		String name = this._eat("IDENTIFIER").getString("value");
		this._eat("{");
		JSONArray spec = this.Especification();
		this._eat("}");
		return new JSONObject().put("type", "declaration")
							   .put("spec", spec)
							   .put("name", name);
	}

}
