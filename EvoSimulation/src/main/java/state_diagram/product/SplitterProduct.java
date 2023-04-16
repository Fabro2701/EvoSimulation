package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import state_diagram.elements.Splitter;
import state_diagram.elements.Transition;

public class SplitterProduct extends Product{
	Splitter state;
	List<TransitionProduct> ts;
	List<Double> probs;

	Random rnd = new Random();
	public SplitterProduct(FlowController ctrl, Splitter state) {
		super(ctrl);
		ctrl.products.put(state, this);
		
		this.state = state;
		this.ts = new ArrayList<>();
		for(Transition t:state.getFromTs()) {
			if(ctrl.products.containsKey(t)) {
				this.ts.add((TransitionProduct) ctrl.products.get(t));
			}
			else{
				this.ts.add(new TransitionProduct(ctrl, t));
			}
		}
		this.probs = new ArrayList<>();
		normalize();
	}
	@Override
	public Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		//stoch type
		double sum = 0d;
		double p = rnd.nextDouble();
		for(int i=0;i<probs.size();i++) {
			if(probs.get(i)+sum>=p) {
				ts.get(i).execute(map);
				ctrl.step(map);
				break;
			}
			sum += probs.get(i);
		}
		return null;
	}
	private void normalize() {
		List<Double> sprobs = state.getProbs();
		double sum = 0d;
		for(Double d:sprobs) {
			sum += d;
		}
		for(int i=0;i<sprobs.size();i++) {
			this.probs.add(sprobs.get(i)/sum);
		}
	}
}
