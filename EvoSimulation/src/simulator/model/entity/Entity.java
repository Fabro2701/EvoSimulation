package simulator.model.entity;

import java.awt.Image;

import simulator.Constants.MOVE;
import simulator.model.map.Node;

public abstract class Entity {
	protected Image img=null;
	protected Node node=null;
	public int x=0;
	public int y=0;
	public abstract MOVE getMove();
	public abstract Image getImage();
	public int getX() {return x;}
	public int getY() {return y;}
	public void setNewPos(int x, int y) {
		this.x=x;
		this.y=y;
	}
}
