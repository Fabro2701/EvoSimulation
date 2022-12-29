package simulator.control.fsm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import simulator.RandomSingleton;
import simulator.model.entity.Entity;

public class StochasticFunctionComparisonTransition<I> extends ComparisonTransition<I>{
	BiFunction<StochasticFunctionComparisonTransition<I>,Entity,State<?>>function;
	
	public StochasticFunctionComparisonTransition(BiFunction<StochasticFunctionComparisonTransition<I>, Entity,State<?>>function, I value) {
		super(null, value);
		this.function = function;
	}

	@Override
	public boolean evaluate(I input) {
		return value.equals(input);
	}
	
	@Override
	public State<?> getTarget(){
		return function.apply(this, null);
	}
}
