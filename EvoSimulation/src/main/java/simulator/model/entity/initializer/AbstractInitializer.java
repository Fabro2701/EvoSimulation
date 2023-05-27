package simulator.model.entity.initializer;

import java.util.List;

import org.json.JSONObject;

import grammar.AbstractGrammar;
import simulator.control.Controller;
import simulator.model.entity.Entity;

public abstract class AbstractInitializer {
	public abstract List<Entity>createPopulation(AbstractGrammar grammar, Controller ctrl, int num, JSONObject properties, String clazz);
}
