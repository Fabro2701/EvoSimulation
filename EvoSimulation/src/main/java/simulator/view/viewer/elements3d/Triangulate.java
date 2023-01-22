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
	public static List<Triangle>convert(String filename, float factor, int width, int height){
		List<Triangle> tris = new ArrayList<Triangle>();
		BufferedImage elevation = null;
		BufferedImage attributes = null;
		BufferedImage terrain = null;
		
		try {
			elevation = ImageIO.read(new File(filename +"/elevation.png"));
			attributes = ImageIO.read(new File(filename +"/attributes.png"));
			terrain = ImageIO.read(new File(filename +"/terrain.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float factorx = (float)elevation.getHeight(null)/width;
		float factory = (float)elevation.getWidth(null)/height;
		
		//float ratio = img.getHeight()/reducedS;
		elevation = Triangulate.reduceImage(elevation, width, height);
		attributes = Triangulate.reduceImage(attributes, width, height);
		
		int space = 4;
		for(int y=0;y<elevation.getHeight();y+=space) {
			for(int x=0;x<elevation.getWidth();x+=space) {
				//System.out.println(new Color(terrain.getRGB((int)(x*factorx), (int)(y*factory))).getRed());
				if(new Color(terrain.getRGB((int)(x*factorx), (int)(y*factory))).getRed()==0)continue;
				if(x<elevation.getWidth()-space && y<elevation.getHeight()-space) {
					//System.out.println(new Color(img.getRGB(x, y)).getGreen());
					Vector3D p0 = new Vector3D(x,y,factor * new Color(elevation.getRGB(x, y)).getGreen()/255f);
					Vector3D p1 = new Vector3D(x+space,y,factor * new Color(elevation.getRGB(x+space, y)).getGreen()/255f);
					Vector3D p2 = new Vector3D(x,y+space,factor * new Color(elevation.getRGB(x, y+space)).getGreen()/255f);
					Triangle t1 = new Triangle(p2,p1,p0);
					t1.col = new Color(new Color(attributes.getRGB(x, y)).getRed(),0,0,200);
					tris.add(t1);
					
					Vector3D p10 = new Vector3D(x+space,y,factor * new Color(elevation.getRGB(x+space, y)).getGreen()/255f);
					Vector3D p11 = new Vector3D(x,y+space,factor * new Color(elevation.getRGB(x, y+space)).getGreen()/255f);
					Vector3D p12 = new Vector3D(x+space,y+space,factor * new Color(elevation.getRGB(x+space, y+space)).getGreen()/255f);
					Triangle t2 = new Triangle(p10,p11,p12);
					t2.col = new Color(new Color(attributes.getRGB(x, y)).getRed(),0,0,200);
					tris.add(t2);
				}
			}
		}
		
		
		
		
		return tris;
	}
	private static BufferedImage reduceImage(Image img, int width, int height) {
		Image imgS = img.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage image2 = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image2.createGraphics();
		g2d.drawImage(imgS, 0, 0, width, height, null);
		return image2;
	}
	public static void main2(String args[]) {
		//Triangulate.convert("elevation", 5f);
	}
}
