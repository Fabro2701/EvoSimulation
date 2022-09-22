package simulator.control.fsm;

public class TrueTransition<I> extends Transition<I>{

	public TrueTransition(State<?> target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean evaluate(I input) {
		// TODO Auto-generated method stub
		return true;
	}

}
