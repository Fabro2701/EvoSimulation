package simulator.control;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

import simulator.model.entity.PasiveEntity;
import simulator.model.entity.individuals.MyIndividual;

public class ImageController {
	//HashMap<Class<?>,FSM<STATE, Image>> images;
	static Map<Object, Image>imgs;
	static {
		imgs = new HashMap<Object, Image>();
		/*imgs.put("O", new ImageIcon("resources/entities/myindividual_resting.png").getImage());
		imgs.put("S", new ImageIcon("resources/entities/myindividual_moving.png").getImage());
		imgs.put("N", new ImageIcon("resources/entities/myindividual_eating.png").getImage());
		imgs.put("supermarket", new ImageIcon("resources/entities/supermarket.png").getImage());
		imgs.put("house", new ImageIcon("resources/entities/house.png").getImage());
		imgs.put("bar", new ImageIcon("resources/entities/bar.png").getImage());
		*/
		imgs.put(PasiveEntity.class, new ImageIcon("resources/entities/pasiveentity.png").getImage());
		imgs.put(MyIndividual.class, new ImageIcon("resources/entities/entity1.png").getImage());
		
	}
	public ImageController() {
		//images = new HashMap<Class<?>,FSM<STATE, Image>>();
		//images.put(MyIndividual.class, ImageController.createMyIndividualFSM());
	}
	/**
	 * Loads all the imgs contained in the directory of name filename
	 * @param filename
	 */
	public static void loadFromDirectory(String filename) {
		if(imgs==null) imgs = new HashMap<Object, Image>();
		Stream.of(new File(filename).listFiles())
	      .filter(file -> !file.isDirectory())
	      .forEach(f->imgs.put(f.getName().substring(0, f.getName().indexOf('.')), new ImageIcon(f.getAbsolutePath()).getImage()));
	}
//	public State<Image> getNextImage(Class<?>clazz, State<Image>current, STATE state) {
//		return images.get(clazz).run(current, state);
//	}
	public static Image getImage(Object key) {
		return imgs.get(key);
	}
//	private static FSM<STATE, Image> createMyIndividualFSM() {
//		FSM<STATE, Image> fsm = new FSM<STATE, Image>();
//		
//		Image restImg = new ImageIcon("resources/entities/myindividual_resting.png").getImage();
//		Image moveImg = new ImageIcon("resources/entities/myindividual_moving.png").getImage();
//		Image eatImg = new ImageIcon("resources/entities/myindividual_eating.png").getImage();
//		
//		State<Image> restState = new SimpleState<Image>(restImg);
//		State<Image> moveState = new SimpleState<Image>(moveImg);
//		State<Image> eatState = new SimpleState<Image>(eatImg);
//		State<Image> eatState2 = new SimpleState<Image>(eatImg);
//		State<Image> eatState3 = new SimpleState<Image>(eatImg);
//		State<Image> eatState4 = new SimpleState<Image>(eatImg);
//		
//		Transition<STATE>toRestTranstition = new ComparisonTransition<STATE>(restState, STATE.REST);
//		Transition<STATE>toMoveTranstition = new ComparisonTransition<STATE>(moveState, STATE.MOVE);
//		Transition<STATE>toEatTranstition = new ComparisonTransition<STATE>(eatState, STATE.EAT);
//		
//		Transition<STATE>toEatTranstitionT1 = new TrueTransition<STATE>(eatState2);
//		Transition<STATE>toEatTranstitionT2 = new TrueTransition<STATE>(eatState3);
//		Transition<STATE>toEatTranstitionT3 = new TrueTransition<STATE>(eatState4);
//		Transition<STATE>toEatTranstitionT4 = new TrueTransition<STATE>(restState);//change with compound state
//
//		
//		fsm.addTransition(restState, toRestTranstition);
//		fsm.addTransition(restState, toMoveTranstition);
//		fsm.addTransition(restState, toEatTranstition);
//
//		fsm.addTransition(moveState, toRestTranstition);
//		fsm.addTransition(moveState, toMoveTranstition);
//		fsm.addTransition(moveState, toEatTranstition);
//
//		fsm.addTransition(eatState, toEatTranstitionT1);
//		fsm.addTransition(eatState2, toEatTranstitionT2);
//		fsm.addTransition(eatState3, toEatTranstitionT3);
//		fsm.addTransition(eatState4, toEatTranstitionT4);
//		fsm.addTransition(eatState, toEatTranstition);
//		fsm.addTransition(eatState, toRestTranstition);
//		fsm.addTransition(eatState, toMoveTranstition);
//		
//		fsm.setCurrent(restState);
//		return fsm;
//	}
//	private static FSM<STATE, Image> createFoodFSM() {
//		FSM<STATE, Image> fsm = new FSM<STATE, Image>();
//		
//		Image img = new ImageIcon("resources/entities/food.png").getImage();
//		
//		State<Image> state = new SimpleState<Image>(img);		
//		Transition<STATE>transtition = new TrueTransition<STATE>(state);
//
//		fsm.addTransition(state, transtition);
//		fsm.setCurrent(state);
//		return fsm;
//	}
	public static void main(String args[]) {
		ImageController.loadFromDirectory("resources/entities/");
	}
}
