package simulator.control.fsm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class State<R> {
	List<Consumer<?>>jobs;
	public State() {
		jobs = new ArrayList<>();
	}
	public abstract R execute();
	public void doJobs(Object o) {
		for(Consumer job:jobs)job.accept(o);
	}
	public State<R> addJob(Consumer<?>job){
		this.jobs.add(job);
		return this;
	}
}
