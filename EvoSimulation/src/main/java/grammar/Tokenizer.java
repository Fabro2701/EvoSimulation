package grammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Tokenizer {
	private int _cursor;
	private String _string;
	
	//Regular Expressions for each token type
	String [][]Spec = {{"^\\s+",null},
			   		   {"^;",";"},
			   		   {"^[{]","{"},
			   		   {"^[}]","}"},
			   		   {"^[(]","("},
			   		   {"^[)]",")"},
			   		   {"^[,]",","},
			   		   {"^\\breturn\\b","return"},
			   		   {"^\\blet\\b","let"},
			   		   {"^\\bif\\b","if"},
			   		   {"^\\belse\\b","else"},
			   		   {"^\\btrue\\b","true"},
			   		   {"^\\bfalse\\b","false"},
			   		   {"^\\bnull\\b","null"},
					   {"^[0-9]+","NUMBER"},
			   		   {"^[\\w\\d\\[\\]]+","IDENTIFIER"},
			   		   {"^[=!]=","EQUALITY_OPERATOR"},
			   		   {"^=","SIMPLE_ASSIGN"},
			   		   {"^[*/+-]=","COMPLEX_ASSIGN"},
			   		   {"^[+-]","ADDITIVE_OPERATOR"},
			   		   {"^[*/]","MULTIPLICATIVE_OPERATOR"},
			   		   {"^[<>]=?","RELATIONAL_OPERATOR"},
			   		   {"^&&","LOGICAL_AND"},
			   		   {"^\\|\\|","LOGICAL_OR"},
			   		   {"^!","LOGICAL_NOT"},
					   {"^\"[^\"]*\"","STRING"}};
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
	public JSONObject getNextToken() {
		if(!this.hasMoreTokens()) {
			//System.err.println("No more tokens");
			return null;
		}
		
		String string = this._string.substring(_cursor);
		String regexp=null;
		String tokenType=null;
		String tokenValue=null;
		for(int i=0;i<Spec.length;i++) {
			regexp=Spec[i][0];
			tokenType=Spec[i][1];
			
			
			tokenValue = this._match(regexp,string);
			
			
			if(tokenValue==null) {
				//System.out.println(0);
				continue;
			}
			if(tokenType == null) {
				//System.out.println(1);
				return this.getNextToken();
			}

			//System.out.println("2  "+tokenValue);
			return new JSONObject().put("type", tokenType).put("value",tokenValue);
		}
		//System.err.println("Unexpected token "+string.charAt(0));
		return null;
	}
	/**
	 * Return the substring that matched with the RE or null otherwise
	 * @param regexp
	 * @param string
	 * @return
	 */
	private String _match(String regexp, String string) {
		//System.out.println(regexp+ "   -   <"+string+">");
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(string); 
		if(!m.find()||m.start()!=0) {
			return null;
		}
		this._cursor+=m.end();
		return string.substring(0, m.end());
	}
}
