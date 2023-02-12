package setup;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import grammar.bnf.BNFTokenizer;
import parsing.AbstractTokenizer;

public class MainTokenizer extends AbstractTokenizer{

	static {
		Specs.put(MainTokenizer.class, new String[][]{
					   {"^\\s+",null},
			   		   {"^grammars","GrammarController"},
			   		   {"^actions","ActionsController"},
			   		   {"^interactions","InteractionsController"},
			   		   {"^updates","UpdatesController"},
			   		   {"^init","InitController"},
			   		   {"^global","GlobalController"},
			   		   {"^\"[^\"]*\"","STRING"}
			   		   });
	}
	
}
