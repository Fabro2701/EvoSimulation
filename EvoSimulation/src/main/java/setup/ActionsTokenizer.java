package setup;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class ActionsTokenizer extends ChildTokenizer{
	public ActionsTokenizer() {
		Spec = new String[][]{
							  {"^\\s+",null},
					   		  {"^:=",":="},
					   		  {"^;",";"},
					   		  {"^\\|\\|","LOGICAL_OR"},
					   		  {"^[|]","|"},
					   		  {"^[.]","."},
					   		  {"^[{]","{"},
					   		  {"^[}]","}"},
					   		  {"^<[^<>]+>","ACTION_ID"},
					   		  {"^;",";"},
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
					   		  {"^[\\w\\d\\[\\].]+","IDENTIFIER"},
					   		  {"^[=!]=","EQUALITY_OPERATOR"},
					   		  {"^=","SIMPLE_ASSIGN"},
					   		  {"^[*/+-]=","COMPLEX_ASSIGN"},
					   		  {"^[+-]","ADDITIVE_OPERATOR"},
					   		  {"^[*/]","MULTIPLICATIVE_OPERATOR"},
					   		  {"^[<>]=?","RELATIONAL_OPERATOR"},
					   		  {"^&&","LOGICAL_AND"},
					   		  {"^!","LOGICAL_NOT"},
							  {"^\"[^\"]*\"","STRING"}
					   		  };
	}
	
}
