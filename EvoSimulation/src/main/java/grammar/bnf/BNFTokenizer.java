package grammar.bnf;


import parsing.AbstractTokenizer;

public class BNFTokenizer extends AbstractTokenizer{

	static {
		Specs.put(BNFTokenizer.class, new String[][]
			   {{"^\\s+",null},
	   		   {"^[.]","."},//end of rule
	   		   {"^[|]","|"},//productions separator
	   		   {"^->","->"},//rule declaration
	   		   {"^<[\\d]+>","MERIT"},
	   		   {"^<[\\w]+>","NTSYMBOL"},
	   		   {"^'[^']+'","TSYMBOL"},
	   		   {"^[^|.<\\s]+","TSYMBOL"}});
	}
	

}
