package block_manipulation.block;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import block_manipulation.block.DrawElement.Shape;

public interface BlockRenderer {
	public void paint(List<Shape> shapes);
	public float getHeight();
	public float getWidth();
}
