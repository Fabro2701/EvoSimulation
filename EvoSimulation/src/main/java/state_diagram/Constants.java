package state_diagram;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class Constants {
	public static final Color SIMPLE_STATE_COLOR = new Color(178, 178, 178);
	public static final Color INIT_COLOR = Color.black;
	public static final Color SPLITTER_COLOR = Color.black;
	public static final Color CORNER_COLOR = Color.black;
	public static final Color SHADOW_COLOR = new Color(178, 178, 178);
	
	public static final int SIMPLE_STATE_W = 20;
	public static final int SIMPLE_STATE_H = 40;
	public static final int INIT_STATE_W = 20;
	public static final int INIT_STATE_H = 20;
	public static final int SPLITTER_W = 20;
	public static final int SPLITTER_H = 20;
	public static final int COMPOUND_STATE_W = 100;
	public static final int COMPOUND_STATE_H = 80;
	public static final int COMPOUND_STATE_HEADER = 10;
	
	public static final int CORNER_W = 7;
	public static final int CORNER_H = 7;
	public static final int SHADOW_MARGIN = 6;

	public static final int ARROW_W = 10;
	public static final int ARROW_H = 5;
	
	public static final int ARC_W = 15;
	public static final int ARC_H = 15;
	
	public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
	public static final Cursor CROSSHAIR_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
	public static final Cursor E_RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static final Cursor S_RESIZE_CURSOR = new Cursor(Cursor.S_RESIZE_CURSOR);
	

	public static final Font TEXT_FONT = new Font("Monospaced", Font.PLAIN, 12);
}
