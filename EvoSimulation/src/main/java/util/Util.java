package util;

import static simulator.Constants.INTERACTION_DISTANCE;
import static simulator.Constants.MOVE.DOWN;
import static simulator.Constants.MOVE.LEFT;
import static simulator.Constants.MOVE.RIGHT;
import static simulator.Constants.MOVE.UP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import simulator.Constants.MOVE;
import simulator.model.entity.Entity;
import simulator.model.entity.individuals.genome.Chromosome;
import simulator.model.entity.individuals.genome.Genotype;
import simulator.model.map.Map;
import simulator.model.map.Node;

public class Util {
	public static java.util.Map<String, Object>createMap(){
		return new HashMap<>();
	}
	public static void printlnString(String s) {
		System.out.println(s);
	}
	public static void printlnDouble(double s) {
		System.out.println(s);
	}
	public static void printlnInt(int s) {
		System.out.println(s);
	}
	public static Color getGradient(float[][]colors, float value, float alpha) {
		value -= 0.001f;
		int ncolors = colors.length-1;
		float idx = (float) (value*ncolors);
		int idx0=(int) Math.floor(idx),idx1=idx0+1;
		
		if(idx<0f) {
			idx=0;
			idx0=0;
			idx1=1;
		}
		
		float prop = idx-idx0;
		prop = prop<=0f?0:prop;
		
		Color c=null;
		try{
			c=new Color((colors[idx1][0]-colors[idx0][0])*prop+colors[idx0][0],
				(colors[idx1][1]-colors[idx0][1])*prop+colors[idx0][1],
				(colors[idx1][2]-colors[idx0][2])*prop+colors[idx0][2],alpha);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	public static int test() {
		return 4;
	}
	public static int test2(int i) {
		return 4+i;
	}
//pending
//	public static MOVE getNextNodeToTo(Node node, Node node2, Map map) {
//		double minDist = 100000d;
//		MOVE move = MOVE.NEUTRAL;
//		for(MOVE m:MOVE.values()) {
//			if(!m.isPseudo()) {
//				double dist = nodeDistance(map.getValidMove(node, m), node2);
//				if(dist<minDist) {
//					minDist = dist;
//					move = m;
//				}
//			}
//		}
//		return move;
//	}
	public static MOVE getNextMoveTo(Node node, Node node2, Map map) {
		double minDist = 100000d;
		MOVE move = MOVE.NEUTRAL;
		for(MOVE m:MOVE.values()) {
			double dist = nodeDistance(map.getValidMove(node, m), node2);
			if(dist<minDist) {
				minDist = dist;
				move = m;
			}
			
		}
		return move;
	}
	public static MOVE getNextMoveAwayFrom(Node node, Node node2, Map map) {
		double maxDist = -1d;
		MOVE move = MOVE.NEUTRAL;
		for(MOVE m:MOVE.values()) {
			double dist = nodeDistance(map.getValidMove(node, m), node2);
			if(dist>maxDist) {
				maxDist = dist;
				move = m;
			} 
		}
		return move;
	}
	public static boolean isInQuadrant(MOVE m, int x, int y, int l) {
		switch(m) {
			case DOWN:
				return y>=x && y+x>=l-1;
			case LEFT:
				return y>=x && y+x<l;
			case RIGHT:
				return y<=x && y+x>=l-1;
			case UP:
				return y<=x && y+x<l;
			default:
				break;
		}

		return false;
	}
	public static double nodeDistance(Node n1, Node n2) {
		return Math.sqrt(Math.pow(n1.x-n2.x, 2)+Math.pow(n1.y-n2.y, 2));
	}
	public static ArrayList<ArrayList<Float>> genotypeSimilarityMatrix(List<Genotype>genos) {
		ArrayList<ArrayList<Float>> matrix = new ArrayList<ArrayList<Float>>(genos.size());
		for(int i=0;i<genos.size();i++) {
			ArrayList<Float>aux = new ArrayList<Float>(genos.size());
			for(int j=0;j<genos.size();j++) {
				if(i==j)aux.add(j, 0.f);
				else aux.add(j, genotypeSimilarity(genos.get(i), genos.get(j)) );
			}
			matrix.add(i, aux);
		}
		return matrix;
	}
	public static float genotypeSimilarity(Genotype g1, Genotype g2) {
		float similarity=0.0f;
		for(int i=0;i<g1.size();i++) {
			Chromosome<Chromosome.Codon> c1 = g1.getChromosome(i);
			Chromosome<Chromosome.Codon> c2 = g2.getChromosome(i);
			
			for(int j=0;j<c1.getLength() && j<c1.getUsedCodons() && j<c2.getUsedCodons();j++) {
				similarity+=c1.getCodon(j).getIntValue()==c2.getCodon(j).getIntValue()?1.f:0.f;
			}
			similarity /= (float)Math.min(Math.min(c1.getUsedCodons(),c2.getUsedCodons()),c1.getLength());
			
		}
		return similarity/2f;
	}
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
	public static void maina(String args[]) {
		int l=15;
		String arr[][] = new String [l][l];
		for(int y=0;y<l;y++) {
			for(int x=0;x<l;x++) {
				arr[y][x]=" ";
			}
		}
		for(int y=0;y<l;y++) {
			for(int x=0;x<l;x++) {
				if(Util.isInQuadrant(LEFT, x, y, l))arr[y][x]="-";
			}
		}
		for(int y=0;y<l;y++) {
			for(int x=0;x<l;x++) {
				System.out.print(arr[y][x]);
			}System.out.println();
		}
		System.out.println(Util.isInQuadrant(UP, 0, -1, 20));
		System.out.println(Util.isInQuadrant(DOWN, 15, 15, 20));
		System.out.println(Util.isInQuadrant(RIGHT, 2, 1, 20));
		System.out.println(Util.isInQuadrant(LEFT, -3, -1, 20));
	
	}
	public static float cosf(float v) {
		return (float)Math.cos(v);
	}
	public static float sinf(float v) {
		return (float)Math.sin(v);
	}
	public static float tanf(float v) {
		return (float)Math.tan(v);
	}
}
