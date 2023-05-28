package simulator.model.entity;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.json.JSONException;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.control.ImageController;
import simulator.control.InitController;
import simulator.model.EvoSimulator;
import simulator.model.evaluation.EvaluationException;
import simulator.model.map.Map;
import simulator.model.map.Node;

public class PasiveEntity extends AliveEntity {

	public PasiveEntity(String id, Node n, Controller ctrl, String code) throws JSONException, EvaluationException {
		super(id, n, ctrl, code);
		pheromone = new Pheromone();
		pheromone.init(this);
		if(this.img == null)this.img = ImageController.getImage(this.getClass());
		this.attributes.put("pasive", true);
		
		init();

		this.setImg(ImageController.getImage(this.getAttribute("info")));
	}

	
	@Override
	public void perform(List<Entity>entities, Map map) {
		
	}
	@Override
	public void update(EvoSimulator evoSimulator) throws IllegalArgumentException, EvaluationException {
		super.update(evoSimulator);
	}
	@Override
	public boolean shouldInteract() {return false;}

	@Override
	public void myInteract(Entity e2) {
		System.err.println("pasive entities dont interact");
	}

}
