package setup.preprocessing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TreeClassifierPreprocessing 
 * @author Fabrizio Ortega
 *
 */
public class TreeClassifierPreprocessing implements Preprocessing{
	public static String apply(String input) {
		StringBuilder out = new StringBuilder(input);

		Pattern p = Pattern.compile("tree\\([^)]+\\)");
		Matcher m = p.matcher(input); 
		while(m.find()) {
			int ini = m.start();
			int end = m.end();
			
			String header = m.group();
			String headers[] = header.substring(5, header.length()-1).split(",");
			String file = headers[0];

			String clazzs[] = Arrays.copyOfRange(headers, 1, headers.length);
			String tree = null;
			try {
				tree = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			out.replace(ini, end, parseTree(tree, clazzs));
	
		}
		
		return out.toString();
	}

	private static String parseTree(String text, String[]clazzs) {
		StringBuilder sb = new StringBuilder();
		int sep=4;

		//parsing
		Pattern p = Pattern.compile("[|\\- ]+");
		String[] lines = text.split("\n");

		List<String>rules = new ArrayList<>();
		int maxdepth = 6;
		String nodes[] = new String[maxdepth];
		
		for(String line:lines) {
			Matcher m = p.matcher(line);
			m.find();
			int ns = m.end();
			int n = (ns-(sep+1))/sep;
			nodes[n] = line.substring(ns);
			if(line.contains("weights")) {
				StringJoiner aux = new StringJoiner(";");
				for(int i=0;i<=n;i++) {
					aux.add(nodes[i]);
				}
				rules.add(aux.toString().replace(" ", ""));
			}
		}
		
		
		//rules.stream().forEach(s->System.out.println(s));

		//vars
		//String[]clazzs = new String[] {"NO","SI"};
		sb.append("let ").append(clazzs[0]).append(" = 0;").append('\n');
		sb.append("let ").append(clazzs[1]).append(" = 0;").append('\n').append('\n');
		
		//translation
		List<String>atts = new ArrayList<>();
		StringBuilder ifsb = new StringBuilder();
		Pattern pid = Pattern.compile("[<>]");//id<=>\\d
		Pattern patt = Pattern.compile("\\[[^\\]]+\\]");//enclosed by []
		for(String rule:rules) {
			String[] sts = rule.split(";");
			StringJoiner aux = new StringJoiner("&&","if(",")");
			for(int i = 0;i<sts.length-1;i++) {
				aux.add(sts[i]);
				Matcher m = pid.matcher(sts[i]);
				m.find();
				String id = sts[i].substring(0,m.start());
				if(!atts.contains(id)) {
					String cond = translateAtt(id);
					atts.add(id);
					sb.append("let ").append(id).append(" = 0;\n");
					sb.append("if(").append(cond).append(")").append(id).append(" = 1;\n");
				}
				
			}
			ifsb.append(aux.toString());
			
			Matcher m = patt.matcher(sts[sts.length-1]);
			m.find();
			String resultexp = sts[sts.length-1].substring(m.start()+1,m.end()-1);
			String[] rs = resultexp.split(",");
			ifsb.append('{').append('\n');
			ifsb.append('\t').append(clazzs[0]).append('=').append(rs[0]).append(';').append('\n');
			ifsb.append('\t').append(clazzs[1]).append('=').append(rs[1]).append(';').append('\n');
			ifsb.append('}').append('\n');
		}
		sb.append('\n');
		sb.append(ifsb.toString());
		//System.out.println(sb.toString());
		return sb.toString();
	}
	public static String translateAtt(String id) {
		if(id.contains("IMC")) {
			String g = id.substring(5);
			return "(this.getAttribute(\"imc\")).equals(\""+g+"\")";
		}
		else {
			String p = id.substring(0, id.length()-3);
			String b = id.substring(id.length()-2);
			return "((this.getPhenotype()).getVariation(\""+p+"\")).equals(\""+b+"\")";
		}
	}
	public static void main(String args[]) {
		String text = "123tree(resources/others/tree.txt,NO,SI)123";
		String r = TreeClassifierPreprocessing.apply(text);
		System.out.println(r);

	}
}
