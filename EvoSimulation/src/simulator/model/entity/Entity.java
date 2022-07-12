package simulator.model.entity;

import java.awt.Image;

import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.map.Node;

import static simulator.Constants.jsonView;

public abstract class Entity {
	protected String type;
	protected String id;
	protected Image img=null;
	public Node node=null;
	
	public Entity(String id, Node n) {
		type = ".";
		this.id = id;
		node=n;
	}
	public abstract MOVE getMove();
	public abstract Image getImage();
	public void setNewNode(Node n) {
		//if(id.equals("4")) System.out.println(n);
		
		node=n;
	}
	public JSONObject toJSON() {
		return new JSONObject().put("type", type)
							   .put("data", new JSONObject().put("id", id)
															.put("x", node.x)
														    .put("y", node.y)
														    //.put("image", JSONObject.NULL)
								    )
								;
	}
	@Override
	public String toString() {
		return toJSON().toString(jsonView);
	}
	
	
}
