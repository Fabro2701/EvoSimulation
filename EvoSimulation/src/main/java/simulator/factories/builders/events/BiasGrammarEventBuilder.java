package simulator.factories.builders.events;

import org.json.JSONObject;

import simulator.Constants;
import simulator.control.Controller;
import simulator.events.models.AddRandomEntitiesConditionGeneratorEvent;
import simulator.events.models.AddRandomEntitiesGeneratorEvent;
import simulator.events.models.BiasGrammarEvent;

public class BiasGrammarEventBuilder extends EventBuilder{
	public BiasGrammarEventBuilder() {
		type = Constants.BiasGrammarEventBuilder_TYPE;
	}
	@Override
	public BiasGrammarEvent createTheInstance(JSONObject o, Controller ctrl) {
	
		return new BiasGrammarEvent(o.getInt("time"),o.getInt("times"),o.getInt("interval"));
	}

}
