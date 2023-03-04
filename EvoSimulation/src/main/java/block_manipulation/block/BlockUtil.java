package block_manipulation.block;

import java.awt.Color;

public class BlockUtil {
	public static int ilumination = 100;
	public static Color iluminate(Color color) {
		return new Color(Math.min(color.getRed()+ilumination, 255),
						 Math.min(color.getGreen()+ilumination, 255),
						 Math.min(color.getBlue()+ilumination, 255),
						 color.getAlpha());
	}
}
