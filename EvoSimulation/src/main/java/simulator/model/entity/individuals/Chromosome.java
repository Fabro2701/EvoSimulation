package simulator.model.entity.individuals;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.RandomSingleton;

/**
 * 
 * @author fabrizioortega
 *
 */
public class Chromosome <T>{

	List<T>codons;
	int length;
	int usedCodons;
	
	public Chromosome(int l, Supplier<?>s) {
		super();
		length = l;
		codons = (List<T>) Stream.generate(s).limit(l).collect(Collectors.toList());
		usedCodons = 0;
	}
	public T getCodon(int i) {
		return codons.get(i);
	}
//	public void setIntToCodon(int i, int v) {
//		codons.set(i, new Codon(v));
//	}
//	public void setModToCodon(int i, int v) {
//		codons.get(i).modValue=v;
//	}
//	public void setArrayIntToCodon(int ...v) {
//		for(int i=0;i<v.length;i++) {
//			codons.set(i, new Codon(v[i]));
//		}
//	}
	
	public int getUsedCodons() {
		return usedCodons;
	}
	public void setUsedCodons(int usedCodons) {
		this.usedCodons = usedCodons;
	}
	public static class Codon{
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
		public void setMod(int v) {
			modValue=v;
		}
		public int getIntValue() {return this.intValue;}
		public int getModValue() {return this.modValue;}
		public JSONObject toJSON() {
			return new JSONObject().put("intValue", intValue);
		}
	}
	public int getLength() {return this.length;}

	
	
}
