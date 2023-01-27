package setup.visualizer;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swing_viewer.DefaultView;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

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
	Viewer viewer;
	View view;
	ViewerPipe fromViewer;
	protected boolean loop = true;
	
	private InitController initController;
	private UpdatesController updatesController;
	private InteractionsController interactionsController;
	private SetupController setup;
	
	
	
	public SetupGraphVisualizer() {
		System.setProperty("org.graphstream.ui", "swing");
		graph = new MultiGraph("Setup");
		graph.setAttribute("ui.stylesheet", styleSheet);
	    graph.setAutoCreate(true);
	    graph.setStrict(false);
	    //viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
	    viewer = graph.display();
	    view =  viewer.getDefaultView();
	    //view = viewer.addDefaultView(false);
	    fromViewer = viewer.newViewerPipe();
	    ClicksListener cl = new ClicksListener();
		fromViewer.addSink(graph);
		
	}
	class ClicksListener implements ViewerListener{
		public ClicksListener() {
			SetupGraphVisualizer.this.fromViewer.addViewerListener(this);
		}
		@Override
		public void viewClosed(String viewName) {
			loop = false;
		}
		@Override
		public void buttonPushed(String id) {
			Node node;
			if((node=graph.getNode(id)).hasAttribute("module")) {
				System.out.println(node.getAttribute("module"));
				String module;
				if(!(module=(String) node.getAttribute("module")).equals("GrammarController")) {
					new CodeEditorDialog(node, SetupGraphVisualizer.this.setup.getModule(module));
				}
			}
		}
		@Override public void buttonReleased(String id) {}
		@Override public void mouseOver(String id) {}
		@Override public void mouseLeft(String id) {}
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
		this.setup = setup;
		this.updatesController = (UpdatesController) setup.getModule("UpdatesController");
		this.initController = (InitController) setup.getModule("InitController");
		this.interactionsController = (InteractionsController) setup.getModule("InteractionsController");
	}
	public void paint() {
		paintInit();
		paintUpdates();
		paintInteractions();
		while(loop) {
			fromViewer.pump();
		}
	}
	public void paintInit() {
		Map<String, Consumer<Entity>>init = initController.getStatements();
		
		Node ctrl = graph.addNode("InitController");
		ctrl.setAttribute("ui.class", "ctrl");
		ctrl.setAttribute("ui.label", "InitController");
		
		for(String id:init.keySet()) {
			List<Class<?>>clazzs = initController.getClasses(id);
			for(Class<?>clazz:clazzs) {
				Node node = graph.addNode(clazz.getName());
				node.setAttribute("ui.class", "clazz");
				node.setAttribute("ui.label", clazz.getSimpleName());
				Node inter = graph.addNode(id);
				inter.setAttribute("ui.class", "inter");
				inter.setAttribute("ui.label", id);
				inter.setAttribute("module", ctrl.getId());
				
				graph.addEdge("init1"+id, node, inter, true);
				graph.addEdge("init2"+id, inter, ctrl, true);
			}
		}
	}
	public void paintUpdates() {
		Map<String, BiConsumer<Entity, EvoSimulator>>updates = updatesController.getUpdates();
		
		Node ctrl = graph.addNode("UpdatesController");
		ctrl.setAttribute("ui.class", "ctrl");
		ctrl.setAttribute("ui.label", "UpdatesController");
		
		for(String id:updates.keySet()) {
			List<Class<?>>clazzs = updatesController.getClasses(id);
			for(Class<?>clazz:clazzs) {
				Node node = graph.addNode(clazz.getName());
				node.setAttribute("ui.class", "clazz");
				node.setAttribute("ui.label", clazz.getSimpleName());
				Node inter = graph.addNode(id);
				inter.setAttribute("ui.class", "inter");
				inter.setAttribute("ui.label", id);
				inter.setAttribute("module", ctrl.getId());
				
				graph.addEdge("updates1"+id, node, inter, true);
				graph.addEdge("updates2"+id, inter, ctrl, true);
			}
		}
	}
	public void paintInteractions() {
		Map<String, InteractionI>init = interactionsController.getInteractions();
		
		for(String id:init.keySet()) {
			Node inter = graph.addNode(id);
			inter.setAttribute("ui.class", "inter");
			inter.setAttribute("ui.label", id);
			inter.setAttribute("module", "InteractionsController");
			Pair<List<Class<?>>, List<Class<?>>>clazzs = interactionsController.getClasses(id);
			for(Class<?>clazz1:clazzs.first) {
				Node node1 = graph.addNode(clazz1.getName());
				node1.setAttribute("ui.class", "clazz");
				node1.setAttribute("ui.label", clazz1.getSimpleName());
				for(Class<?>clazz2:clazzs.second) {
					Node node2 = graph.addNode(clazz2.getName());
					node2.setAttribute("ui.class", "clazz");
					node2.setAttribute("ui.label", clazz2.getSimpleName());
					
					graph.addEdge("interactions1"+id, node1, inter, true);
					graph.addEdge("interactions2"+id, inter, node2, true);
				}
			}
		}
	}
	public static void main(String args[]) {
		SetupController setup = SetupController.from("resources/setup/obesidad.stp");
		SetupGraphVisualizer gv = new SetupGraphVisualizer();
		gv.load(setup);
		gv.paint();
	}
}
