package genome_editing.model.editor.block;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import genome_editing.model.editor.block.DrawElement.Shape;

public interface BlockRenderer {
	public void paint(List<Shape> shapes);
	public float getHeight();
	public float getWidth();
}
