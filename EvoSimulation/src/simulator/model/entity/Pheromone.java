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
	}
	public void init(Entity e) {
		this.add(e.active?1.f:0.f);
		this.add(e.weight);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
		this.add(0.f);
	}
	@Override
	public String toString() {
		return this.get(0).toString();
	}
}
