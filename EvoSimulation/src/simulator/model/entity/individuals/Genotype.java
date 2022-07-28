package simulator.model.entity.individuals;

import java.util.ArrayList;

import org.json.JSONObject;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Genotype extends ArrayList<Chromosome>{
	public Genotype(Chromosome c) {
		super();
		this.add(c);
	}
	public Genotype(JSONObject c) {
		super();
		this.add(new Chromosome(c.getJSONObject("chromosome")));
	}
	public Chromosome getChromosome() {
		return this.get(0);
	}
	public JSONObject toJSON() {
		return new JSONObject().put("chromosome", this.get(0).toJSON());
	}
}
