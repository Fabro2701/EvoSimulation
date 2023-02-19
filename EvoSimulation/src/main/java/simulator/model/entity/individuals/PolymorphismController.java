package simulator.model.entity.individuals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import simulator.Constants;
import simulator.RandomSingleton;
import simulator.model.entity.individuals.PolymorphismController.VARIATION;

public class PolymorphismController implements Mapper{
	protected static Map<String,List<VARIATION>>variations;
	protected static Map<String,Integer>positions;
	
	
	@Override
	public Object mapChromosome(Chromosome<?> c) {
		return mapPolymorphisms((Chromosome<Integer>) c);
	}
	@Override
	public Map<String,VARIATION> mapPolymorphisms(Chromosome<Integer> c) {
		Map<String,VARIATION> result = new LinkedHashMap<>();
		for(String gen:variations.keySet()) {
			int m = c.getCodon(positions.get(gen)) % variations.get(gen).size();
			result.put(gen, variations.get(gen).get(m));
		}
		return result;
	}
	public static void loadFromFile(String path) throws FileNotFoundException, IOException {

		variations = new LinkedHashMap<>();
		positions = new LinkedHashMap<>();
		
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
				
				String[]vs = properties.getProperty((String) key).split(",");
				variations.put(name, Arrays.stream(vs).map((String v)->VARIATION.valueOf(v)).collect(Collectors.toList()));
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
