package setup.visualizer;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;

import simulator.control.SetupController;
import simulator.control.UpdatesController;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;

public class SetupGraphVisualizer {
	Graph graph;
	UpdatesController updatesController;
	public SetupGraphVisualizer() {
		System.setProperty("org.graphstream.ui", "swing");
		 graph = new MultiGraph("Setup");
		 graph.setAttribute("ui.stylesheet", styleSheet);
	        graph.setAutoCreate(true);
	        graph.setStrict(false);
	        graph.display();
	}
	static protected String styleSheet =
	        "node.clazz {"
	        + "	size: 35px;"
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
	        + "	size: 40px;"
	        + " fill-color: white; "
	        + " stroke-mode: plain; "
	        + "	stroke-color: black;"
	        + " stroke-width: 3px;"
	        + "}";
	public void load(SetupController setup) {
		this.updatesController = (UpdatesController) setup.getModule("UpdatesController");
		
		
		
	}
	public void paint() {
		Map<String, BiConsumer<Entity, EvoSimulator>>updates = updatesController.getUpdates();
		
		Node update = graph.addNode("Updates");
		update.setAttribute("ui.class", "ctrl");
		update.setAttribute("ui.label", "Updates");
		
		for(String id:updates.keySet()) {
			List<Class<?>>clazzs = updatesController.getClasses(id);
			for(Class<?>clazz:clazzs) {
				Node node = graph.addNode(clazz.toString());
				node.setAttribute("ui.class", "clazz");
				node.setAttribute("ui.label", clazz.toString());
				Node inter = graph.addNode("updates"+id);
				inter.setAttribute("ui.class", "inter");
				inter.setAttribute("ui.label", id);
				graph.addEdge(id, node, inter);
				graph.addEdge("updates"+id, inter, update);
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
