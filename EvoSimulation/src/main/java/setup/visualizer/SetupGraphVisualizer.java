package setup.visualizer;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;

import simulator.control.InitController;
import simulator.control.InteractionsController;
import simulator.control.SetupController;
import simulator.control.UpdatesController;
import simulator.model.EvoSimulator;
import simulator.model.InteractionI;
import simulator.model.entity.Entity;
import util.Pair;

public class SetupGraphVisualizer {
	Graph graph;
	UpdatesController updatesController;
	InitController initController;
	InteractionsController interactionsController;
	
	public SetupGraphVisualizer() {
		System.setProperty("org.graphstream.ui", "swing");
		 graph = new MultiGraph("Setup");
		 graph.setAttribute("ui.stylesheet", styleSheet);
	        graph.setAutoCreate(true);
	        graph.setStrict(false);
	        graph.display();
	}
	static protected String styleSheet =
			"node{"
			+ "text-background-mode: plain;"
			+ "}"
			+ "edge{"
			+ " shape: angle;"
			+ " size: 10px;"
			+ " arrow-shape: none;"
			+ " fill-color: cyan,magenta;"
			+ " fill-mode: gradient-horizontal;"
			+ "}"
			+"node.clazz {"
	        + "	size: 45px;"
	        + " fill-color: white; "
	        + " stroke-mode: plain; "
	        + "	stroke-color: black;"
	        +"}"
	        + "node.inter{"
	        + "	size: 15px;"
	        + " fill-color: black; "
	        + " stroke-mode: plain; "
	        + "	stroke-color: black;"
	        + "}"
	        + "node.ctrl{"
	        + " shape: box;"
	        + "	size: 50px;"
	        + " fill-mode: plain;"
	        + " fill-color: black,white;"
	        + "	stroke-color: black;"
	        + " stroke-width: 3px;"
	        + "}";
	public void load(SetupController setup) {
		this.updatesController = (UpdatesController) setup.getModule("UpdatesController");
		this.initController = (InitController) setup.getModule("InitController");
		this.interactionsController = (InteractionsController) setup.getModule("InteractionsController");
		
		
		
	}
	public void paint() {
		paintInit();
		paintUpdates();
		paintInteractions();
	}
	public void paintInteractions() {
		Map<String, InteractionI>init = interactionsController.getInteractions();
		
		for(String id:init.keySet()) {
			Node inter = graph.addNode("init"+id);
			inter.setAttribute("ui.class", "inter");
			inter.setAttribute("ui.label", id);
			Pair<List<Class<?>>, List<Class<?>>>clazzs = interactionsController.getClasses(id);
			for(Class<?>clazz1:clazzs.first) {
				Node node1 = graph.addNode(clazz1.toString());
				node1.setAttribute("ui.class", "clazz");
				node1.setAttribute("ui.label", clazz1.getSimpleName());
				for(Class<?>clazz2:clazzs.second) {
					Node node2 = graph.addNode(clazz2.toString());
					node2.setAttribute("ui.class", "clazz");
					node2.setAttribute("ui.label", clazz2.getSimpleName());
					
					graph.addEdge("interaction1"+id, node1, inter, true);
					graph.addEdge("interaction2"+id, inter, node2, true);
				}
			}
		}
	}
	public void paintInit() {
		Map<String, Consumer<Entity>>init = initController.getStatements();
		
		Node ctrl = graph.addNode("Init");
		ctrl.setAttribute("ui.class", "ctrl");
		ctrl.setAttribute("ui.label", "Init");
		
		for(String id:init.keySet()) {
			List<Class<?>>clazzs = initController.getClasses(id);
			for(Class<?>clazz:clazzs) {
				Node node = graph.addNode(clazz.toString());
				node.setAttribute("ui.class", "clazz");
				node.setAttribute("ui.label", clazz.getSimpleName());
				Node inter = graph.addNode("init"+id);
				inter.setAttribute("ui.class", "inter");
				inter.setAttribute("ui.label", id);
				graph.addEdge(id, node, inter, true);
				graph.addEdge("init"+id, inter, ctrl, true);
			}
		}
	}
	public void paintUpdates() {
		Map<String, BiConsumer<Entity, EvoSimulator>>updates = updatesController.getUpdates();
		
		Node ctrl = graph.addNode("Updates");
		ctrl.setAttribute("ui.class", "ctrl");
		ctrl.setAttribute("ui.label", "Updates");
		
		for(String id:updates.keySet()) {
			List<Class<?>>clazzs = updatesController.getClasses(id);
			for(Class<?>clazz:clazzs) {
				Node node = graph.addNode(clazz.toString());
				node.setAttribute("ui.class", "clazz");
				node.setAttribute("ui.label", clazz.getSimpleName());
				Node inter = graph.addNode("updates"+id);
				inter.setAttribute("ui.class", "inter");
				inter.setAttribute("ui.label", id);
				graph.addEdge(id, node, inter, true);
				graph.addEdge("updates"+id, inter, ctrl, true);
			}
		}
	}
	public static void main(String args[]) {
		SetupController setup = SetupController.from("resources/setup/default.stp");
		SetupGraphVisualizer gv = new SetupGraphVisualizer();
		gv.load(setup);
		gv.paint();
	}
}
