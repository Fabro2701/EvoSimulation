package state_diagram.product;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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
	List<TransitionProduct> ts;
	private String params="";
	static Pattern p = Pattern.compile("^\\([^\\(]*\\)");
	public SimpleStateProduct(FlowController ctrl, SimpleState state) {
		super(ctrl);
		ctrl.products.put(state, this);
		
		this.state = state;
		this.rest = state.getRest();
		this.continuous = state.isContinuous();
		
		this.ts = new ArrayList<>();
		for(var t:state.getFromTs()) {
			if(ctrl.products.containsKey(t)) {
				this.ts.add((TransitionProduct) ctrl.products.get(t));
			}
			this.ts.add(new TransitionProduct(ctrl, t));
		}
		
		cont = rest;

		Matcher m = p.matcher(state.getAction());
		if(m.find()) this.params = m.group();
	}
	@Override
	public Object execute(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		//System.out.println("call");
		if(rest==0) {
			action(map);
			for(var t:ts) {
				if((boolean) t.execute(map))return null;
			}
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
				for(var t:ts)if((boolean) t.execute(map))return null;
			}
		}

		return null;
	}
	public void action(Map<String, Object>map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		ctrl.invoke(state.getId()+"Action"+String.valueOf(state.ID), Util.extractParams(map,params));
	}
}
