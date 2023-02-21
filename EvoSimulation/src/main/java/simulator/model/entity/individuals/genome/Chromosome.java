package simulator.model.entity.individuals.genome;

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
	
	Supplier<T>supplier;
	
	/**
	 * Generate the list of codons using a supplier
	 * @param length
	 * @param s supplier
	 */
	public Chromosome(int length, Supplier<T>s) {
		super();
		this.length = length;
		codons = (List<T>) Stream.generate(s).limit(length).collect(Collectors.toList());
		usedCodons = 0;
		this.supplier = s;
	} 
	public Chromosome(Chromosome<T> copy) {
		super();
		this.length = copy.length;
		this.supplier = copy.supplier;
		this.codons = new ArrayList<>();
		for(int i=0;i<copy.codons.size();i++) {
			Object o = copy.codons.get(i);
			if(o instanceof Integer) this.codons.add(i, (T)Integer.valueOf(((Integer)o).intValue()));
			if(o instanceof Float) this.codons.add(i, (T)Float.valueOf(((Float)o).floatValue()));
			if(o instanceof Boolean) this.codons.add(i, (T)Boolean.valueOf(((Boolean)o).booleanValue()));
			if(o instanceof Codon) this.codons.add(i, (T)new Codon((Codon)o));
		}
		usedCodons = 0;
	}
	public void flip(int i) {
		this.codons.set(i, this.supplier.get());
	}
	public void setCodon(int i, T c) {
		this.codons.set(i, c);
	}
	public T getCodon(int i) {
		return codons.get(i);
	}
	
	public int getUsedCodons() {
		return usedCodons;
	}
	public void setUsedCodons(int usedCodons) {
		this.usedCodons = usedCodons;
	}
	public List<T> getCodons() {
		return codons;
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
		@Override 
		public String toString() {
			return String.valueOf(intValue);
		}
	}
	public int getLength() {return this.length;}
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(T e:this.codons) {
			sb.append(e).append(" ");
		}
		return sb.toString();
	}
	
	
}
