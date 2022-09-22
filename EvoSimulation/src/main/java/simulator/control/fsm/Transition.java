package simulator.control.fsm;

public abstract class Transition<I> {
	State<?> target;
	
	public Transition(State<?>target) {
		this.target = target;
	}
	public abstract boolean evaluate(I input);
	public State<?>getTarget(){
		return target;
	}
}
