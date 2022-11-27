package setup.preprocessing;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentPreprocessing implements Preprocessing{
	public static String apply(String input) {
		StringBuilder out = new StringBuilder();
		int i=0;
		
		//System.out.println("from: "+input);
		
		Pattern p = Pattern.compile("(?s)/\\*(.)*?\\*/");
		Matcher m = p.matcher(input); 
		while(m.find()) {
			int ini = m.start();
			int end = m.end();
			
			out.append(input.substring(i, ini));
			i=end+1;
			//String path = input.substring(ini, end);
			//System.out.println("pattern: "+path);
		}
		out.append(input.substring(i, input.length()));

		//System.out.println("to: "+out.toString());
		return out.toString();
	}
	public static void main(String args[]) {
		String t = CommentPreprocessing.apply("global := a{\r\n"
				+ "			let mu = 0.0004578;\r\n"
				+ "			/*let beta = 0.001121;*/\r\n"
				+ "			let gamma = 0.0003226;\r\n"
				+ "			file(resources/setup/test2.stp)\r\n"
				+ "			let p1 = 0.020;\r\n"
				+ "			let p2 = 0.042;\r\n"
				+ "			let p = 1/7;\r\n"
				+ "			file(resources/setup/test2.stp)\r\n"
				+ "			let S0 = 0.25;\r\n"
				+ "			let O0 = 0.046;\r\n"
				+ "		  }.	");
		
	}
}
