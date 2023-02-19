package simulator.model.entity.individuals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import simulator.Constants;
import simulator.RandomSingleton;

public class PolymorphismController implements Mapper{
	protected static Map<String,List<VARIATION>>variations;
	protected static Map<String,Integer>positions;
	protected static Map<String,List<Float>>probs;
	
	
	@Override
	public Object mapChromosome(Chromosome<?> c) {
		return mapPolymorphisms((Chromosome<Float>) c);
	}
	@Override
	public Map<String,VARIATION> mapPolymorphisms(Chromosome<Float> c) {
		Map<String,VARIATION> result = new LinkedHashMap<>();
		for(String gen:variations.keySet()) {
			//int m = c.getCodon(positions.get(gen)) % variations.get(gen).size();
			int m = getVariation(variations.get(gen), probs.get(gen), c.getCodon(positions.get(gen)));
			result.put(gen, variations.get(gen).get(m));
		}
		return result;
	}
	public static int getVariation(List<VARIATION>vars, List<Float>prob, float e) {
		float sum = 0f;
		int idx = 0;
		for(int i=0;i<vars.size();i++) {
			sum += prob.get(i);
			if(sum >= e) {
				idx=i;
				break;
			}
		}
		return idx;
	}
	public static void loadFromFile(String path) throws FileNotFoundException, IOException {

		variations = new LinkedHashMap<>();
		positions = new LinkedHashMap<>();
		probs = new LinkedHashMap<>();
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(path));
		
		try {
			for(Object key:properties.keySet()) {
				//key == name(position)
				String s[] = ((String)key).split("[(]");
				String name = s[0];
				String position = s[1].substring(0, s[1].length()-1);
				
				int p;
				if(position.equals("r"))p=RandomSingleton.nextInt(Constants.CHROMOSOME_LENGTH);
				else p = Integer.valueOf(position);

				positions.put(name, p);
				
				String[]vs = properties.getProperty((String) key).replaceAll("\\s","").split(",");
				variations.put(name, Arrays.stream(vs).map((String v)->VARIATION.valueOf(v.split("[(]")[0])).collect(Collectors.toList()));
				
				Pattern pattern = Pattern.compile("[0-9.]+");
				probs.put(name, Arrays.stream(vs).map((String v)->{Matcher m = pattern.matcher(v);m.find(); return Float.valueOf(v.substring(m.start(), m.end()));}).collect(Collectors.toList()));
			}
		}catch(NumberFormatException ex) {
			System.err.println("Syntax error");
			ex.printStackTrace();
		}
		
		
	}
	public enum VARIATION{
		AA,AC,AG,AT,
		CA,CC,CG,CT,
		GA,GC,GG,GT,
		TA,TC,TG,TT
	}
	public static void main(String args[]) {
		try {
			PolymorphismController.loadFromFile("resources/scenarios/obesidad/poly.poly");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
