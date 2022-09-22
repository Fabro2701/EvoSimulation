package simulator.control.fsm;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import simulator.Constants.MOVE;

public class FSM<I, R> {
	HashMap<State<R>,ArrayList<Transition<I>>>states;
	State<R>current;
	
	public FSM() {
		states = new HashMap<State<R>,ArrayList<Transition<I>>>();
	}
	public R run(I input) {
		for(Transition<I> t:states.get(current)) {
			if(t.evaluate(input)) {
				current = (State<R>) t.getTarget();
			}
		}
		return current.execute();
	}
	public State<R> run(State<R>state, I input) {
		if(state==null)return current;//init
		for(Transition<I> t:states.get(state)) {
			if(t.evaluate(input)) {
				return (State<R>) t.getTarget();
			}
		}
		return state;
	}
	public void addTransition(State<R> state, Transition<I> transition) {
		if(!states.containsKey(state)) {
			ArrayList<Transition<I>>aux = new ArrayList<Transition<I>>();
			states.put(state, aux);
		}
		states.get(state).add(transition);
	}
	public void setCurrent(State<R>c) {
		current = c;
	}
	
	public static void main(String args[]) {
		FSM<Input, Image> fsm = new FSM<Input, Image>();
		
		Image img1 = new ImageIcon("resources/entities/myentity.png").getImage();
		Image img2 = new ImageIcon("resources/entities/myentity2.png").getImage();
		Image img3 = new ImageIcon("resources/entities/food.png").getImage();
		
		State<Image> s1 = new SimpleState<Image>(img1);
		State<Image> s2 = new SimpleState<Image>(img2);
		State<Image> s3 = new SimpleState<Image>(img3);
		
		Transition<Input>t12 = new TrueTransition<Input>(s2);
		Transition<Input>t23 = new TrueTransition<Input>(s3);
		Transition<Input>t31 = new TrueTransition<Input>(s1);
		
		fsm.addTransition(s1, t12);
		fsm.addTransition(s2, t23);
		fsm.addTransition(s3, t31);
		
		fsm.setCurrent(s1);
		
		
		for(int i=0;i<10;i++) {
			Image img = fsm.run(null);
			System.out.println(img.toString());
		}
		
		
	}
}
