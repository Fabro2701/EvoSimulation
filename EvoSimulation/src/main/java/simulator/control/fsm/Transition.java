package simulator.control.fsm;

public abstract class Transition<I> {
	State<?> target;
	int rest;
	
	public Transition(State<?>target) {
		this.target = target;
	}
	public abstract boolean evaluate(I input);
	public State<?>getTarget(){
		return target;
	}
	public Transition<I>setRest(int rest){
		this.rest = rest;
		return this;
	}
}
