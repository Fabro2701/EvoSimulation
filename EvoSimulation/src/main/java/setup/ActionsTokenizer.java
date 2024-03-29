package setup;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import grammar.bnf.BNFTokenizer;

public class ActionsTokenizer extends ChildTokenizer{
	static {
		Specs.put(ActionsTokenizer.class, new String[][]{
			  {"^\\s+",null},
	   		  {"^:=",":="},
	   		  {"^;",";"},
	   		  {"^\\.","."},
	   		  {"^\\|\\|","LOGICAL_OR"},
	   		  {"^&&","LOGICAL_AND"},
	   		  {"^\\[","["},
	   		  {"^\\]","]"},
	   		  {"^[|]","|"},
	   		  {"^[{]","{"},
	   		  {"^[}]","}"},
	   		  {"^<[\\w\\d]+>","ACTION_ID"},
	   		  {"^[(]","("},
	   		  {"^[)]",")"},
	   		  {"^[,]",","},
	   		  {"^\\benum\\b","enum"},
	   		  {"^\\bstatic\\b","static"},
	   		  {"^\\bnew\\b","new"},
	   		  {"^\\breturn\\b","return"},
	   		  {"^\\blet\\b","let"},
	   		  {"^\\bif\\b","if"},
	   		  {"^\\belse\\b","else"},
	   		  {"^\\btrue\\b","true"},
	   		  {"^\\bfalse\\b","false"},
	   		  {"^\\bnull\\b","null"},
	   		  {"^\\bfor\\b","for"},
	   		  {"^\\bwhile\\b","while"},
			  {"^[0-9.]+f?d?","NUMBER"},
	   		  {"^[\\w\\d\\$]+","IDENTIFIER"},
	   		  {"^[=!]=","EQUALITY_OPERATOR"},
	   		  {"^=","SIMPLE_ASSIGN"},
	   		  {"^[*/+-]=","COMPLEX_ASSIGN"},
	   		  {"^[+-]","ADDITIVE_OPERATOR"},
	   		  {"^[*/%]","MULTIPLICATIVE_OPERATOR"},
	   		  {"^[<>]=?","RELATIONAL_OPERATOR"},
	   		  {"^!","LOGICAL_NOT"},
			  {"^\"[^\"]*\"","STRING"}
	   		  });
	}
}
