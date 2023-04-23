package simulator.view.viewer;

import java.awt.Graphics2D;

@FunctionalInterface
public abstract interface ViewElement {
	public abstract void paint(Graphics2D g2);
}
