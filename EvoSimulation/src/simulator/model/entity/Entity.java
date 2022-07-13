package simulator.model.entity;

import static simulator.Constants.jsonView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.model.map.Node;
import simulator.view.viewer.Viewer;
import statistics.StatsManager;

public abstract class Entity implements IInteract{
	protected String type;
	protected String id;
	protected Image img=null;
	public Node node=null;
	protected boolean alive;
	
	public Entity(String id, Node n) {
		type = ".";
		this.id = id;
		node=n;
		alive=true;
	}
	public abstract MOVE getMove();
	public Image getImage() {
		return img;
	}
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
	public void openDialog(Viewer viewer) {
		JDialog dia = new JDialog();
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				if(img!=null) {
					g.drawImage(img, 0, 0, null);
				}
			}
		};
		dia.setContentPane(panel);
		panel.repaint();
		
		
		dia.setPreferredSize(new Dimension(200,200));
		dia.setMinimumSize(new Dimension(200,200));
		dia.setLocationRelativeTo(viewer);
		dia.setLocation(new Point(node.x,node.y));
		dia.setVisible(true);
		
	}
	protected abstract void getFood(FoodEntity foodEntity);
	public void vanish() {
		StatsManager.getInstance().onEntityVanished(-1);
		alive=false;
	}
	public boolean isAlive(){return alive;}
	
	@Override
	public void interact(Entity e) {
		if(!this.alive||!e.isAlive())return;
		else myInteract(e);
	}
}
