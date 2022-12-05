package setup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;



public class BlocksParser extends OOPParser{

	public BlocksParser() {
		_tokenizer = new BlocksTokenizer();
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
		JSONArray clazzs = new JSONArray();
		while(this._lookahead != null && !this._lookahead.getString("type").equals(")") && !(this._lookahead.getString("type").equals(",") && this._eat(",")==null)) {
			clazzs.put(this.StringLiteral());
		}
		
		this._eat(")");
		
		int c1 = this._tokenizer.get_cursor();
		this._eat("{");
		JSONArray spec = this.Especification();
		int c2 = this._tokenizer.get_cursor();
		String code = this._string.substring(c1+1,c2-1);
		this._eat("}");
		return new JSONObject().put("type", "declaration")
				   			   .put("clazzs", clazzs)
							   .put("spec", spec)
							   .put("name", name)
							   .put("code", code);
	}

}