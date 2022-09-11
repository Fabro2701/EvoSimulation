package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Chromosome {
	List<Codon>codons;
	int length;
	int usedCodons;

	
	public Chromosome(int l) {
		length = l;
		codons = new ArrayList<Codon>(length);
		for(int i=0; i<length; i++) {
			codons.add(new Codon());
		}
		usedCodons = 0;
	}
	public Chromosome(Chromosome copy) {
		length = copy.length;
		codons = new ArrayList<Codon>(length);
		for(int i=0; i<length; i++) {
			codons.add(new Codon(copy.codons.get(i)));
		}
		usedCodons = copy.usedCodons;
	}
	public Chromosome(JSONObject o) {
		length = o.getInt("length");
		codons = new ArrayList<Codon>(length);
		JSONArray arr = o.getJSONArray("codons");
		for(int i=0; i<length; i++) {
			codons.add(new Codon(arr.getJSONObject(i).getInt("intValue")));
		}
	}
	public void setIntToCodon(int i, int v) {
		codons.set(i, new Codon(v));
	}
	public void setModToCodon(int i, int v) {
		codons.get(i).modValue=v;
	}
	public void setArrayIntToCodon(int ...v) {
		for(int i=0;i<v.length;i++) {
			codons.set(i, new Codon(v[i]));
		}
	}
	
	public int getUsedCodons() {
		return usedCodons;
	}
	public void setUsedCodons(int usedCodons) {
		this.usedCodons = usedCodons;
	}
	public class Codon{
		BitSet bits;
		int intValue;
		int modValue;
		public Codon() {
			bits = new BitSet(8);
			intValue = RandomSingleton.nextInt(256);
		}
		public Codon(int n) {
			bits = new BitSet(8);
			intValue = n;
		}
		public Codon(JSONObject o) {
			bits = new BitSet(8);
			intValue = o.getInt("intValue");
		}
		public Codon(Codon copy) {
			bits = new BitSet(8);
			intValue = copy.intValue;
		}
		public void setInt(int v) {
			intValue=v;
		}
		public int getIntValue() {return this.intValue;}
		public int getModValue() {return this.modValue;}
		public JSONObject toJSON() {
			return new JSONObject().put("intValue", intValue);
		}
	}
	public int getLength() {return this.length;}
	public Codon getCodon(int i) {return this.codons.get(i);}
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(Codon c:codons) {
			arr.put(c.toJSON());
		}
		return new JSONObject().put("length", this.length)
							   .put("codons", arr);
	}
}
