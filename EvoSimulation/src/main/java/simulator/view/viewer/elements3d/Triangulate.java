package simulator.view.viewer.elements3d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import simulator.view.viewer.elements3d.elements.Triangle;

public class Triangulate {
	public static List<Triangle>convert(String filename, float factor){
		List<Triangle> tris = new ArrayList<Triangle>();
		BufferedImage elevation = null;
		BufferedImage attributes = null;
		
		try {
			elevation = ImageIO.read(new File("resources/maps/" + filename +"/elevation"+ ".png"));
			attributes = ImageIO.read(new File("resources/maps/" + filename +"/attributes"+ ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int reducedS = 20;
		//float ratio = img.getHeight()/reducedS;
		elevation = Triangulate.reduceImage(reducedS, elevation);
		attributes = Triangulate.reduceImage(reducedS, attributes);

		for(int y=0;y<elevation.getHeight();y++) {
			for(int x=0;x<elevation.getWidth();x++) {
				if(x<elevation.getWidth()-1 && y<elevation.getHeight()-1) {
					//System.out.println(new Color(img.getRGB(x, y)).getGreen());
					Vector3D p0 = new Vector3D(x,y,factor * new Color(elevation.getRGB(x, y)).getGreen()/255f);
					Vector3D p1 = new Vector3D(x+1,y,factor * new Color(elevation.getRGB(x+1, y)).getGreen()/255f);
					Vector3D p2 = new Vector3D(x,y+1,factor * new Color(elevation.getRGB(x, y+1)).getGreen()/255f);
					Triangle t1 = new Triangle(p2,p1,p0);
					t1.col = new Color(new Color(attributes.getRGB(x, y)).getRed(),0,0,200);
					tris.add(t1);
					
					Vector3D p10 = new Vector3D(x+1,y,factor * new Color(elevation.getRGB(x+1, y)).getGreen()/255f);
					Vector3D p11 = new Vector3D(x,y+1,factor * new Color(elevation.getRGB(x, y+1)).getGreen()/255f);
					Vector3D p12 = new Vector3D(x+1,y+1,factor * new Color(elevation.getRGB(x+1, y+1)).getGreen()/255f);
					Triangle t2 = new Triangle(p10,p11,p12);
					t2.col = new Color(new Color(attributes.getRGB(x, y)).getRed(),0,0,200);
					tris.add(t2);
				}
			}
		}
		
		
		
		
		return tris;
	}
	private static BufferedImage reduceImage(int size, Image img) {
		Image imgS = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
		BufferedImage image2 = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image2.createGraphics();
		g2d.drawImage(imgS, 0, 0, size, size, null);
		return image2;
	}
	public static void main2(String args[]) {
		Triangulate.convert("elevation", 5f);
	}
}
