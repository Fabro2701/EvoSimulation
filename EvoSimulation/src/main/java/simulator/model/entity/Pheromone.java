package simulator.model.entity;

import java.util.ArrayList;

import simulator.Constants;

/**
 * Pheromone class codifies a entity as a vector
 * @author fabrizioortega
 *
 */
public class Pheromone extends ArrayList<Float>{
	public Pheromone() {
		super(Constants.PHEROMONE_LENGTH);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
	}
	public void init(Entity e) {
		this.set(0,e instanceof ActiveEntity?1.f:0.f);
		this.set(3,0.f);
		this.set(4,0.f);
		this.set(5,0.f);
		this.set(6,0.f);
		this.set(7,0.f);
	}
	@Override
	public String toString() {
		return this.get(0).toString();
	}
	public static void main(String args[]) {
		Pheromone p = new Pheromone();
		for(int i=0; i<p.size();i++) {
			System.out.println(p.get(i));
		}
	}
}
