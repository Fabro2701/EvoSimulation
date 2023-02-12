package setup;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import parsing.AbstractTokenizer;

public class ChildTokenizer extends AbstractTokenizer{
	private int lastChange=0;
	
	
	public void init(String string, int c) {
		_cursor=c;
		_string=string;
	}
	
	/**
	 * Return the substring that matched with the RE or null otherwise
	 * @param regexp
	 * @param string
	 * @return
	 */
	@Override
	protected String _match(String regexp, String string) {
		if(debug)System.out.println(regexp+ "   -   < "+string+" >");
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(string); 
		if(!m.find()||m.start()!=0) {
			return null;
		}
		lastChange=this._cursor;
		this._cursor+=m.end();
		return string.substring(0, m.end());
	}

	public int getLastChange() {
		return lastChange;
	}
}
