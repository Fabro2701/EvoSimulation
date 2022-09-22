package simulator.control.fsm;

public class SimpleState<R> extends State<R>{
	R data;
	
	public SimpleState(R data) {
		this.data = data;
	}
	public R execute() {
		return data;
	}

}
