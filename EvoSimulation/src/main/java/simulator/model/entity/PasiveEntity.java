package simulator.model.entity;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.InitController;
import simulator.model.EvoSimulator;
import simulator.model.map.Map;
import simulator.model.map.Node;

public class PasiveEntity extends InteractiveEntity {

	public PasiveEntity(String id, Node n, Controller ctrl) {
		super(id, n, ctrl);
		pheromone = new Pheromone();
		pheromone.init(this);
		this.img = ImageController.getImage(this.getClass());
		this.alive = true;
		this.attributes.put("pasive", true);
	}

	@Override
	protected void init() {
		java.util.Map<String, Consumer<Entity>>inits_l = inits.getStatements(); 
		for(String id:inits_l.keySet()) {
			if(inits.match(id, this.getClass())) {
				inits_l.get(id).accept(this);
			}
		}
		this.alive = true;
	}
	@Override
	public void perform(List<Entity>entities, Map map) {
		
	}
	@Override
	public void update(EvoSimulator evoSimulator) {
		super.update(evoSimulator);
	}
	@Override
	public boolean shouldInteract() {return false;}

	@Override
	public void myInteract(Entity e2) {
		System.err.println("pasive entities dont interact");
	}

}
