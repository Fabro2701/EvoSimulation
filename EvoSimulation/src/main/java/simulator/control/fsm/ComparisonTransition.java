package simulator.control.fsm;

public class ComparisonTransition<I> extends Transition<I>{
	I value;
	public ComparisonTransition(State<?> target, I value) {
		super(target);
		this.value = value;
	}

	@Override
	public boolean evaluate(I input) {
		return value.equals(input);
	}
	
	

}
