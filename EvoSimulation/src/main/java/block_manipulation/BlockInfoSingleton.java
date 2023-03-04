package block_manipulation;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.parsing.BlockParser;
import genome_editing.model.RandomSingleton;

public class BlockInfoSingleton {
	static int alpha = 100;
	static Map<String, BlockInfoSupplier>fileSuppliers = new LinkedHashMap<>();
	
	public static BlockInfoSupplier fromFile(String filename) {
		if(fileSuppliers.containsKey(filename))return fileSuppliers.get(filename);
		else {
			LinkedHashMap<String, JSONArray> descs = new LinkedHashMap<>();
			LinkedHashMap<String, Color> colors = new LinkedHashMap<>();
			BlockParser parser = new BlockParser();
			JSONObject description = parser.parseFile(filename);
			JSONArray arr = description.getJSONArray("blocks");
			for(int i=0;i<arr.length();i++) {
				JSONObject desc = arr.getJSONObject(i);
				
				descs.put(desc.getJSONObject("name").getString("id"), desc.getJSONArray("alignment"));
				
				String sc = desc.getString("color");
				if(!sc.equals("r")) {
					String[] c = sc.substring(1, sc.length()-1).split("[,]");
					colors.put(desc.getJSONObject("name").getString("id"), new Color(Integer.valueOf(c[0]),Integer.valueOf(c[1]),Integer.valueOf(c[2]),alpha));
				}
				else {
					colors.put(desc.getJSONObject("name").getString("id"), new Color(RandomSingleton.nextInt(256),RandomSingleton.nextInt(256),RandomSingleton.nextInt(256),alpha));
				}
			}
			fileSuppliers.put(filename, new BlockInfoSupplier(descs, colors));
			return fileSuppliers.get(filename);
		}
	}
	
	public static class BlockInfoSupplier{
		Map<String, JSONArray>descs;
		Map<String, Color>colors;
		public BlockInfoSupplier(Map<String, JSONArray>descs, Map<String, Color>colors) {
			this.descs = descs;
			this.colors = colors;
		}
		public JSONArray getDesc(String symbol) {
			return descs.get(symbol);
		}
		public Color getColor(String symbol) {
			return colors.get(symbol);
		}
		public Set<String> getKeys(){
			return descs.keySet();
		}
	}
	private BlockInfoSingleton() {}
}
