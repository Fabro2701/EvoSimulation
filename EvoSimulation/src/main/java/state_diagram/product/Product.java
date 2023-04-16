package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class Product {
	protected FlowController ctrl;
	public Product(FlowController ctrl) {
		this.ctrl = ctrl;
	}
	public abstract Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException ;
}
