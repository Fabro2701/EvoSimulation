package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import state_diagram.elements.EndState;

public class EndStateProduct extends Product{

	public EndStateProduct(FlowController ctrl, EndState state) {
		super(ctrl);
		ctrl.products.put(state, this);

	}
	@Override
	public Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		return null;
	}
}
