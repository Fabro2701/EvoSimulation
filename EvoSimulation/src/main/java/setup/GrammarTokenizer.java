package setup;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class GrammarTokenizer extends ChildTokenizer{
	public GrammarTokenizer() {
		Spec = new String[][]{{"^\\s+",null},
					   		  {"^:=",":="},
					   		  {"^;",";"},
					   		  {"^[|]","|"},
					   		  {"^[.]","."},
					   		  {"^\"[^\"]*\"","Literal"},
					   		  {"^[^;.|]+","Literal"}
					   		  };
	}
	
}
