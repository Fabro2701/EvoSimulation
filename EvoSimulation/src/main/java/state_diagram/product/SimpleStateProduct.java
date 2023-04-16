package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import state_diagram.Util;
import state_diagram.elements.SimpleState;
import state_diagram.elements.Transition;

public class SimpleStateProduct extends Product{
	SimpleState state;
	int rest,cont;
	boolean continuous;
	TransitionProduct t;
	private String params="";
	static Pattern p = Pattern.compile("^\\([^\\(]*\\)");
	public SimpleStateProduct(FlowController ctrl, SimpleState state) {
		super(ctrl);
		ctrl.products.put(state, this);
		
		this.state = state;
		this.rest = state.getRest();
		this.continuous = state.isContinuous();
		
		Transition te = state.getFromTs().get(0);
		if(ctrl.products.containsKey(te)) {
			this.t = (TransitionProduct) ctrl.products.get(te);
		}
		this.t = new TransitionProduct(ctrl, state.getFromTs().get(0));
		cont = rest;

		Matcher m = p.matcher(state.getAction());
		if(m.find()) this.params = m.group();
	}
	@Override
	public Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		//System.out.println("call");
		if(rest==0) {
			action(map);
			t.execute(map);
		}
		else {
			if(cont == rest) {
				action(map);
				cont --;
			}
			else if(cont >= 0) {
				if(continuous)action(map);
				cont --;
			}
			else {
				if(continuous)action(map);
				cont = rest;
				t.execute(map);
			}
		}

		return null;
	}
	public void action(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		ctrl.invoke(state.getId()+"Action"+String.valueOf(state.ID), Util.extractParams(map,params));
	}
}
