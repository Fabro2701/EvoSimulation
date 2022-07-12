package simulator.model.entity;

import java.awt.Image;

import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.entity.Entity.Builder;
import simulator.model.map.Node;

import static simulator.Constants.jsonView;

public abstract class Entity {
	
	protected String id;
	protected Image img=null;
	protected Node node=null;
	public int x=0;
	public int y=0;
	
	public Entity(String id, Node n) {
		this.id = id;
		node=n;
	}
	public abstract MOVE getMove();
	public abstract Image getImage();
	public int getX() {return x;}
	public int getY() {return y;}
	public void setNewPos(int x, int y) {
		this.x=x;
		this.y=y;
	}
	public JSONObject toJSON() {
		Builder b =new Builder();
	
		return .toJSON(this);
		
	}
	@Override
	public String toString() {
		return toJSON().toString(jsonView);
	}
	
	public static abstract class Builder{
		public String type = ".";
		public Builder() {}
		public Entity createInstance(JSONObject info, Controller ctrl) {
			if(type.equals(info.getString("type")))
				return createTheInstance(info.getJSONObject("data"),ctrl);
			else return null;
		}
		public abstract Entity createTheInstance(JSONObject o, Controller ctrl);
		public JSONObject toJSON(Entity e) {
			return new JSONObject().put("type", type)
								   .put("data", new JSONObject().put("id", e.id)
																.put("x", e.x)
															    .put("y", e.y)
															    //.put("image", JSONObject.NULL)
									    )
									;
		}
	}
}
