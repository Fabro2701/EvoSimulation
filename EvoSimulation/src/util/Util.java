package util;

import static simulator.Constants.INTERACTION_DISTANCE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.BitSet;

import simulator.model.entity.Entity;

public class Util {
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	public static boolean areCloseEnough(Entity e1, Entity e2) {
		return Math.abs(e1.node.x-e2.node.x)+(Math.abs(e1.node.y-e2.node.y))<=INTERACTION_DISTANCE;
	}
	public static int log2(int N)
    {
 
        // calculate log2 N indirectly
        // using log() method
        int result = (int)(Math.log(N) / Math.log(2));
 
        return result;
    }
	public static int toInt(BitSet bitSet) {
        int intValue = 0;
        for (int bit = 0; bit < bitSet.length(); bit++) {
            if (bitSet.get(bit)) {
                intValue |= (1 << bit);
            }
        }
        return intValue;
    }
	public static void main(String args[]) {
		System.out.println(log2(1));
		System.out.println(log2(2));
		System.out.println(log2(4));
		System.out.println(log2(8));
		System.out.println(log2(16));
	}
}
