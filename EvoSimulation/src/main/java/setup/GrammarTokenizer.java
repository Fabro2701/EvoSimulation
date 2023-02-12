package setup;

import grammar.bnf.BNFTokenizer;

public class GrammarTokenizer extends ChildTokenizer{
	static {
		Specs.put(GrammarTokenizer.class, new String[][]{
			{"^\\s+",null},
	   		  {"^:=",":="},
	   		  {"^;",";"},
	   		  {"^[|]","|"},
	   		  {"^[.]","."},
	   		  {"^\"[^\"]*\"","Literal"},
	   		  {"^[^;.|]+","Literal"}
	   		  });
	}
}
