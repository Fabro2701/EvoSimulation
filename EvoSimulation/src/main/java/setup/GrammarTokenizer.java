package setup;

import grammar.bnf.BNFTokenizer;

public class GrammarTokenizer extends ChildTokenizer{
	static {
		Specs.put(GrammarTokenizer.class, new String[][]{
			{"^\\s+",null},
	   		  {"^:=",":="},
	   		  {"^;",";"},
	   		  {"^[(]","("},
	   		  {"^[)]",")"},
	   		  {"^[|]","|"},
	   		  {"^[.]","."},
			  {"^-?[0-9.]+f?d?","NUMBER"},
	   		  {"^[\\w\\d\\$]+","IDENTIFIER"},
	   		  {"^\"[^\"]*\"","Literal"},
	   		  {"^[^;.|]+","Literal"}
	   		  });
	}
}
