package simulator.model.entity.individuals;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Genotype extends ArrayList<Chromosome>{
	public Genotype() {
		super();
	}
	public Genotype(JSONObject genotype) {
 		System.err.println("Genotype pending...");
	}
	//	public Genotype(Genotype copy) {
//		super();
//		for(Chromosome c:copy) {
//			this.add(new Chromosome(c));
//		}
//	}
//	public Genotype(JSONObject c) {
//		super();
//		this.add(new Chromosome(c.getJSONObject("chromosome")));
//	}
	public void addChromosome(Chromosome c) {
		this.add(c);
	}
	public Chromosome getChromosome(int i) {
		return this.get(i);
	}
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Chromosome c:this)arr.put(c);
		
		return new JSONObject().put("chromosomes", arr);
	}
	
	public String toString(int idx) {
		return this.get(idx).toString();
	}
}
