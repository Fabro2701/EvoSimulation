package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import state_diagram.elements.InitState;
import state_diagram.elements.Transition;

public class InitStateProduct extends Product{
	InitState state;
	TransitionProduct t;
	public InitStateProduct(FlowController ctrl, InitState state) {
		super(ctrl);
		ctrl.products.put(state, this);
		
		this.state = state;
		Transition te = state.getFromTs().get(0);
		if(ctrl.products.containsKey(te)) {
			this.t = (TransitionProduct) ctrl.products.get(te);
		}
		else{
			this.t = new TransitionProduct(ctrl, te);
		}
	}
	@Override
	public Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		t.execute(map);
		ctrl.step(map);
		return null;
	}
}
