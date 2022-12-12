package simulator.control.fsm;

public class SimpleState<R> extends State<R>{
	R data;
	
	public SimpleState(R data) {
		this.data = data;
	}
	public R execute() {
		return data;
	}
	public R getData() {
		return data;
	}
	public void setData(R data) {
		this.data = data;
	}

}
