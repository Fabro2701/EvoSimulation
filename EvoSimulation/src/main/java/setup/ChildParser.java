package setup;

import org.json.JSONObject;


public abstract class ChildParser {
	protected String _string;
	public ChildTokenizer _tokenizer;
	protected JSONObject _lookahead;
	
	public ChildParser() {
	}
	public JSONObject parse(MainParser father){
		_string = father.get_string();
		_tokenizer.init(_string, father.get_tokenizer().get_cursor());
		_lookahead = father.get_lookahead();
		_lookahead = _tokenizer.getNextToken();
		JSONObject program = this.Program();
		father.update(_lookahead,this._tokenizer.getLastChange());
		return program;
	}
	public JSONObject parse(String code){
		_string = code;
		_tokenizer = new OOPTokenizer();
		_tokenizer.init(code);
		this._lookahead = this._tokenizer.getNextToken();
		return this.Program();
	}
	protected abstract JSONObject Program();
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
}
