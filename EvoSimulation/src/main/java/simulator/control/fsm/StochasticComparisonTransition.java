package simulator.control.fsm;

import java.util.ArrayList;
import java.util.List;

import simulator.RandomSingleton;

public class StochasticComparisonTransition<I> extends ComparisonTransition<I>{
	List<State<?>> targets;
	List<Float>probs;
	public StochasticComparisonTransition(List<State<?>> targets, List<Float>probs, I value) {
		super(null, value);
		this.targets = targets;
		this.probs = probs;
		this.value = value;
	}

	@Override
	public boolean evaluate(I input) {
		return value.equals(input);
	}
	
	@Override
	public State<?> getTarget(){
		float s = 0.0f;
		float r = RandomSingleton.nextFloat();
		
		for(int i=0;i<targets.size();i++) {
			s += probs.get(i);
			if(r<=s) return targets.get(i);
		}
		System.err.println("StochasticComparisonTransition error");
		return null;
	}
}
