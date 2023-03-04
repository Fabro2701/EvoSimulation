package block_manipulation.block;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextPane;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.block.DrawElement.Shape;
import block_manipulation.block.DrawElement.StringShape;


public class InputBlock extends PredefinedBlock{
	String text;
	int textSize;
	protected RecursiveBlock reference;
	public InputBlock(BlockManager manager, JSONArray parameters) {
		super(manager, parameters);
	}

	@Override
	protected void config() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void setParameter(String id, JSONObject value) {
		switch(id) {
			case "text":
				text = value.getString("value");
				textSize = manager.getGraphics().getFontMetrics().stringWidth(text);
				break;
			default:
				System.err.println("unsupported parameter: "+id);
		}
	}
	@Override
	public void paint(List<Shape> shapes) {
		bufferShapes.clear(); 
		String tmp = manager.getInputText(reference.getPosition());
		this.text = tmp==null?this.text:tmp;
		bufferShapes.add(new StringShape(text, 
								   this.base.x+2f, 
								   this.base.y+stringHeight-2f, 
								   Color.black,
								   manager.getGraphics().getFontMetrics().getStringBounds(text, manager.getGraphics())
								   )
				);
//		shapes.add(new DrawElement.Rectangle(this.base.x,     this.base.y, 
//				 1, 100, 
//				 color));
		shapes.addAll(bufferShapes);
	}

	@Override
	public float getHeight() {
		int nl = text.split("\n").length;
		return  manager.getGraphics().getFontMetrics().getHeight()*nl;
	}

	@Override
	public float getWidth() {
		int max=0;
		for (String line : text.split("\n")) {
			max = Math.max(max, manager.getGraphics().getFontMetrics().stringWidth(line));
		}
		return max+2f;
	}

	@Override
	public Block findRecursive(Point point) {
		return null;
	}
	@Override
	public Block findPredefined(Point point) {
		for(Shape shape:bufferShapes) {
			if(shape.contains(point)) {				
				return this;
			}
		}
		return null;
	}
	@Override
	public void rightClick(Point point, Component c) {
		if(bufferShapes.get(0).contains(point))this.openDialog(point, c);
	}

	private void openDialog(Point point, Component c) {
		JDialog dialog = new JDialog();
		dialog.setPreferredSize(new Dimension(700,200));
		dialog.setLayout(new BorderLayout());
		JTextPane editor = new JTextPane();
		String stext = manager.getInputText(reference.getPosition());
		if(stext==null) {
			manager.setInputText(this.text, reference.getPosition());
			editor.setText(this.text);
		}
		else {
			editor.setText(stext);
		}
		dialog.add(editor, BorderLayout.CENTER);
		JButton saveButton = new JButton("save");
		saveButton.addActionListener(e->save(editor.getText(), c));
		dialog.add(saveButton, BorderLayout.PAGE_END);
		dialog.setLocation(point.x, point.y);
		dialog.pack();
		dialog.setVisible(true);
	}

	private void save(String text, Component c) {
		this.manager.setInputText(text, reference.getPosition());
		c.repaint();
	}
	@Override
	public JSONObject toJSON() {
		String tmp = manager.getInputText(reference.getPosition());
		this.text = tmp==null?this.text:tmp;
		return new JSONObject().put("type", "InputBlock")
							   .put("text", this.text);
	}




}
