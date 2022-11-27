package test;

import java.util.Iterator;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class GraphTest {
	public static void main(String args[]) {
		System.setProperty("org.graphstream.ui", "swing");
        new GraphTest();
        
    }

    public GraphTest() {
        Graph graph = new MultiGraph("tutorial 1");

        graph.setAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.display();

        graph.addEdge("AB", "A", "B");
        graph.addEdge("AB2", "A", "B");
        graph.addEdge("AB3", "A", "B");
        graph.addEdge("AB4", "A", "B");
        graph.addEdge("AB5", "A", "B");
        graph.addEdge("AC", "A", "C");
        graph.addEdge("AC2", "A", "C");
        graph.addEdge("AC3", "A", "C");
        graph.addEdge("AC4", "A", "C");
        graph.addEdge("AC5", "A", "C");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("AD2", "A", "D");
        graph.addEdge("AD3", "A", "D");
        graph.addEdge("AD4", "A", "D");
        graph.addEdge("AD5", "A", "D");
        graph.addEdge("AE", "A", "E");
        graph.addEdge("AE2", "A", "E");
        graph.addEdge("AE3", "A", "E");
        graph.addEdge("AE4", "A", "E");
        graph.addEdge("AE5", "A", "E");

        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
        }

        explore(graph.getNode("A"));
    }

    public void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
            sleep();
        }
    }

    protected void sleep() {
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    protected String styleSheet =
        "node {" +
        "	fill-color: black;" +
        "}" +
        "node.marked {" +
        "	fill-color: red;" +
        "}";
}
