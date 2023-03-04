package block_manipulation.block;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class BlockCreator {
	public static LinkedHashMap<String, JSONArray> loadBlocks(JSONObject description) {
		LinkedHashMap<String, JSONArray> map = new LinkedHashMap<String, JSONArray>();
		JSONArray arr = description.getJSONArray("blocks");
		for(int i=0;i<arr.length();i++) {
			JSONObject desc = arr.getJSONObject(i);
			map.put(desc.getJSONObject("name").getString("id"), desc.getJSONArray("alignment"));
		}
		return map;
	}
}
