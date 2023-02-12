package parsing;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class AbstractTokenizer {
	protected int _cursor;
	protected String _string;
	protected int newlinecont=0;
	protected int columncont=0;

	public static Map<Class<?>,String [][]>Specs;
	static {
		Specs = new HashMap<>();
	}
	
	public void init(String string) {
		_cursor=0;
		_string=string;
		
	}
	public boolean isEOF() {
		return this._cursor == this._string.length();
	}
	public boolean hasMoreTokens() {
		return this._cursor < this._string.length();
	}
	protected boolean debug = false;
	public JSONObject getNextToken() {
		String [][] Spec = AbstractTokenizer.Specs.get(this.getClass());
		if(!this.hasMoreTokens()) {
			//System.err.println("No more tokens");
			return null;
		}
		
		
		if(this._string.charAt(_cursor)=='\n') {
			newlinecont++;
			columncont=0;
		}
		
		String string = this._string.substring(_cursor);
		String regexp=null;
		String tokenType=null;
		String tokenValue=null;

		int oldc=this._cursor;
		for(int i=0;i<Spec.length;i++) {
			regexp=Spec[i][0];
			tokenType=Spec[i][1];
			
			tokenValue = this._match(regexp,string);
			
			
			if(tokenValue==null) {
				if(debug)System.out.println(0);
				continue;
			}
			if(tokenType == null) {
				if(debug)System.out.println(1);
				columncont+=(this._cursor-oldc);
				return this.getNextToken();
			}

			if(debug)System.out.println("2  "+tokenValue);
			//System.out.println((tokenValue)+" AT: "+newlinecont+", " +columncont);
			JSONObject r = new JSONObject().put("type", tokenType)
			   .put("value",tokenValue)
			   .put("row", newlinecont)
			   .put("column", columncont);
			columncont+=(this._cursor-oldc);
			return r;
		}
		System.err.println("Unexpected token "+string.charAt(0));
		return null;
	}
	/**
	 * Return the substring that matched with the RE or null otherwise
	 * @param regexp
	 * @param string
	 * @return
	 */
	protected String _match(String regexp, String string) {
		if(debug)System.out.println(regexp+ "   -   < "+string+" >");
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(string); 
		if(!m.find()||m.start()!=0) {
			return null;
		}
		this._cursor+=m.end();
		return string.substring(0, m.end());
	}
	public int get_cursor() {
		return _cursor;
	}

	public void set_cursor(int _cursor) {
		this._cursor = _cursor;
	}
}
