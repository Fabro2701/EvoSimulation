package test;

import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

public class EdgeTest {
	  private static void graphWithUiLabel() {
		MultiGraph g = new MultiGraph("labels");
	    g.addNode("N1");
	    g.addNode("N2");
	    Edge e = g.addEdge("E1", "N1", "N2");
	    e.setAttribute("ui.label", "edge1");
	    e = g.addEdge("E2", "N1", "N2");
	    e.setAttribute("ui.label", "edge2");
	    
	    g.display();
	  }
	  
	  private static void graphWithSprite() {
		  MultiGraph g = new MultiGraph("labels");
	    g.addNode("N1");
	    g.addNode("N2");
	    g.addEdge("E1", "N1", "N2");
	    g.addEdge("E2", "N1", "N2");
	    SpriteManager sm = new SpriteManager(g);
	    
	    Sprite s = sm.addSprite("E1");
	    s.setAttribute("ui.label", "edge1");
	    s.attachToEdge("E1");
	    s.setPosition(0.5);
	    
	    s = sm.addSprite("E2");    
	    s.setAttribute("ui.label", "edge2");
	    s.attachToEdge("E2");
	    s.setPosition(0.5);
	    
	    g.display();
	  }
	  
	  public static void main(String[] args)  {
	    // fx is kind of buggy or I'm to stupid, last message
	    // in the mailing list said to activate debug, this
	    // works for the first graph but not the second.
	    
	    // System.setProperty("org.graphstream.ui", "javafx");
	    // System.setProperty("org.graphstream.debug","true");
	    
	    System.setProperty("org.graphstream.ui", "swing");
	    graphWithUiLabel();
	    graphWithSprite();    
	  }
	}
