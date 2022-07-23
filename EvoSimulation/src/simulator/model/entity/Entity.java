package simulator.model.entity;

import static simulator.Constants.MOVEMENT_ENERGY_COST_CONSTANT;
import static simulator.Constants.jsonView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.json.JSONObject;

import simulator.Constants.MOVE;
import simulator.control.Controller;
import simulator.model.EvoSimulator;
import simulator.model.map.Node;
import simulator.view.viewer.Viewer;
import statistics.StatsManager;

public abstract class Entity implements IInteract {
	protected String type;
	protected String id;
	protected Image img;
	public Node node;
	protected boolean alive;
	protected float energy;
	protected float weight;
	protected int age;
	protected int currentTime;

	public Entity(String id, Node n) {
		type = ".";
		this.id = id;
		node = n;
		alive = true;
		energy = 100.0f;
		weight = 1.f;
		age=0;
	}

	public void update(EvoSimulator evoSimulator) {
		currentTime=evoSimulator.getTime();
	}

	public final MOVE getMove(HashMap<String,Integer>observations) {
		MOVE m = getTheMove(observations);
		if (m != MOVE.NEUTRAL)
			energy -= weight * MOVEMENT_ENERGY_COST_CONSTANT;

		return m;
	}
	public final int getAge() {return age;}
	public abstract MOVE getTheMove(HashMap<String,Integer>observations);
	public abstract boolean shouldInteract();
	@Override
	public void interact(Entity e) {
		if (!this.alive || !e.isAlive())
			return;
		else
			myInteract(e);
	}

	protected abstract void getFood(FoodEntity foodEntity);

	public void vanish() {
		StatsManager.getInstance().onEntityVanished(-1);
		StatsManager.getInstance().onEntityDead(-1,this);
		alive = false;
	}

	public JSONObject toJSON() {
		return new JSONObject().put("type", type).put("data",
				new JSONObject().put("id", id).put("x", node.x).put("y", node.y)
		// .put("image", JSONObject.NULL)
		);
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
				if (img != null) {
					g.drawImage(img, 0, 0, null);
				}
			}
		};
		dia.setContentPane(panel);
		panel.repaint();

		dia.setPreferredSize(new Dimension(200, 200));
		dia.setMinimumSize(new Dimension(200, 200));
		dia.setLocationRelativeTo(viewer);
		dia.setLocation(new Point(node.x, node.y));
		dia.setVisible(true);

	}

	public boolean isAlive() {
		return alive;
	}

	public final Image getImage() {
		return img;
	}

	public void setNewNode(Node n) {
		node = n;
	}
	public String getId() {return id;}
}
