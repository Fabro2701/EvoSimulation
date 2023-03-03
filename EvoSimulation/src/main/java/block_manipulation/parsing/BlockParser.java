package block_manipulation.parsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;



public class BlockParser {
	protected String _string;
	protected BlockTokenizer _tokenizer;
	protected JSONObject _lookahead;
	public BlockParser() {
		_string = new String();
		_tokenizer = new BlockTokenizer();
	}
	public JSONObject parseFile(String filename){
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/skeletons/"+filename+".sklt")));){
			String aux = reader.readLine();
			while(aux != null) {
				sb.append(aux);
				aux = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.parse(sb.toString());
	}
	public JSONObject parse(String string){
		_string = string;
		_tokenizer.init(string);
		
		this._lookahead = this._tokenizer.getNextToken();
		return this.Program();
	}
	protected JSONObject Program() {
		return new JSONObject().put("type", "BlocksDescription").put("blocks", this.StatementList());
	}
	protected JSONArray StatementList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null) {
			arr.put(this.Statement());
		}
		return arr;
	}
	protected JSONObject Statement() {
		return this.RuleStatement();
	}

	protected JSONObject RuleStatement() {
		JSONObject key = this.NTSymbol();
		_eat(":-");
		JSONArray productions = this.AlignmentList();
		_eat(".");
		return new JSONObject().put("name", key)
							   .put("alignment", productions);
	}

	protected JSONArray AlignmentList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(".")) {
			arr.put(this.Block());
			if(this._lookahead.getString("type").equals(",")) _eat(",");
		}
		return arr;
	}
	protected JSONArray Block() {
		this._eat("[");
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !(this._lookahead.getString("type").equals(".")||this._lookahead.getString("type").equals("]"))) {
			arr.put(this.Symbol());	
			if(this._lookahead.getString("type").equals(",")) _eat(",");
		}
		this._eat("]");
		return arr;
	}
	protected JSONObject Symbol() {
		if(this._lookahead.getString("type").equals("NTSYMBOL")) {
			return this.NTSymbol();
		}
		else if(this._lookahead.getString("type").equals("LITERAL")) {
			return this.Literal();
		}
		else {
			return this.TSymbol();
		}
	}
	protected JSONObject Literal() {
		String literal = _eat("LITERAL").getString("value");
		return new JSONObject().put("type", "Literal")
				   .put("value", literal.substring(1, literal.length()-1));
	}
	protected JSONObject TSymbol() {
		String literal = _eat("FUNC_CALL").getString("value");
		JSONArray params = this.Parameters();
		this._eat("FUNC_END");
		return new JSONObject().put("type", "PredefinedBlock")
							   .put("id", literal.substring(0, literal.length()-1))
							   .put("params", params);
	}
	protected JSONArray Parameters() {
		JSONArray arr = new JSONArray();
		while(this._lookahead != null && !this._lookahead.getString("type").equals("FUNC_END")) {
			arr.put(this.Param());		
			if(this._lookahead.getString("type").equals(",")) _eat(",");
		}
		return arr;
	}
	protected JSONObject Param() {
		String name = this._eat("PARAM_ASSIGN").getString("value");
		JSONObject value = this.Symbol();
		return new JSONObject().put("type", "Parameter")
							   .put("name", name.substring(0, name.length()-1))
							   .put("value", value);
	}
	protected JSONObject NTSymbol() {
		String literal = _eat("NTSYMBOL").getString("value");
		return new JSONObject().put("type", "RecursiveBlock").put("id", literal.substring(1, literal.length()-1));
	}
	protected JSONObject _eat(String type) {
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
		String e1 ;
		String e2 = "<A>->'a'|<B>."
				  + "<B>->'b'|'a'.";
		
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/skeletons/test.sklt")));
			String aux = reader.readLine();
			while(aux!=null) {
				sb.append(aux);
				aux = reader.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String e3 = sb.toString();
		BlockParser parser = new BlockParser();
		System.out.println(parser.parse(e3).toString(4));
		
		
		String s = "\"rtet\"4st\"";
		Pattern p = Pattern.compile("^\"[^\"]*\"");
		Matcher m = p.matcher(s); 
		System.out.println(m.find());
		System.out.println(s.substring(m.start(), m.end()));
	}
}
