package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import state_diagram.Util;
import state_diagram.elements.EndState;
import state_diagram.elements.SimpleState;
import state_diagram.elements.Splitter;
import state_diagram.elements.Transition;
import state_diagram.elements.TransitionableElement;

public class TransitionProduct extends Product{
	Transition t;
	Product to;
	String params="";
	static Pattern p = Pattern.compile("^\\([^\\(]*\\)");
	public TransitionProduct(FlowController ctrl, Transition t) {
		super(ctrl);
		ctrl.products.put(t, this);
		
		this.t = t;
		TransitionableElement sto = t.getTo();
		if(ctrl.products.containsKey(sto)) {
			this.to = ctrl.products.get(sto);
		}
		else {
			if(sto instanceof SimpleState) {
				this.to = new SimpleStateProduct(ctrl, (SimpleState) sto);
			}
			else if(sto instanceof EndState) {
				this.to = new EndStateProduct(ctrl, (EndState) sto);
			}
			else if(sto instanceof Splitter) {
				this.to = new SplitterProduct(ctrl, (Splitter) sto);
			}
		}
		Matcher m = p.matcher(t.getCode());
		if(m.find()) this.params = m.group();
	}
	@Override
	public Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		boolean b = (boolean) ctrl.invoke("transition"+t.ID, Util.extractParams(map,params));
		if(b) {
			advance();
			if(ctrl.getCurrent() instanceof InitStateProduct) {
				ctrl.step(null);
			}
		}
		
		return null;
	}
	public void advance() {
		ctrl.setCurrent(to);
	}


}
