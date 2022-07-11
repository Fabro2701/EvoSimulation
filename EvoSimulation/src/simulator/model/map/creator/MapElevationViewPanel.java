package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;



public class MapElevationViewPanel extends JPanel{
	private float ySkew=1f;
	private BufferedImage image;
	private BufferedImage rootImage;
	public MapElevationViewPanel(MapElevationModifierPanel map) {
		map.addObserver(this);
		image = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
	}
	public void config() {
		createNewCanvas();
	}
	public void update(BufferedImage inImage) {
		rootImage=inImage;
		
		repaint();
	}
	private BufferedImage reduceImage(BufferedImage img) {
		int size=50;
		Image imgS = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
		BufferedImage image2 = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)image2.createGraphics();
		g2d.drawImage(imgS, 0, 0, size, size, null);
		/*for (int y = 0; y < image2.getHeight(); y++) {
	         for (int x = 0; x < image2.getWidth(); x++) {   
	        	 int pixel = image2.getRGB(x,y);
	        	 Color color = new Color(pixel, false);
	        	 //elevation[y][x]=color.getRed();
	         }
	    }*/
		return image2;
	}
	private void createNewCanvas() {
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.getHeight(), this.getWidth());
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2dd = (Graphics2D)g;
		BufferedImage reducedImg = reduceImage(rootImage);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.getHeight(), this.getWidth());
		g2d.setColor(Color.green);
		int xs = 10;
		int ys = xs;
		int originX = -200;
		int originY = 200;
		Point2D point1;
		Point2D point2;
		
		float inskew = 2.0f;
		
		for(int i=0;i<reducedImg.getHeight();i++) {
			for(int j=0;j<reducedImg.getWidth();j++) {
				if(i<reducedImg.getHeight()-1) {
					point1 = transform3D(
				             new Point3D(i * xs, 
							 (int) (new Color(reducedImg.getRGB(j, i),false).getGreen() * inskew), 
							  j * ys));
					point2 = transform3D(
				             new Point3D((i+1) * xs, 
							 (int) (new Color(reducedImg.getRGB(j, i+1),false).getGreen() * inskew), 
							  j * ys));
					//g2d.fillOval(point1.x, point1.y, 20, 20);
					g2d.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
					//System.out.println(point1.x);
					//System.out.println(point1.y);
				}
				if(j<reducedImg.getWidth()-1) {
					point1 = transform3D(
							new Point3D(i * xs, 
											 (int) (new Color(reducedImg.getRGB(j, i),false).getGreen() * inskew), 
											  j * ys));
					point2 = transform3D(
							new Point3D(i * xs,
											 (int) (new Color(reducedImg.getRGB(j+1, i),false).getGreen() * inskew), 
											 (j + 1) * ys));
					
					g2d.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
				
				}
			}
		}
		g2dd.drawImage(image, 0, 0, null);
	}
	public void changeSkew(float change) {
		this.ySkew+=change;
		repaint();
	}
	public Point2D transform3D(Point3D point3D) {
		//return new Point2D(point3D.x + point3D.z, (int) (((-point3D.y) + point3D.z - point3D.x) * ySkew));
		return new Point2D(point3D.x + point3D.z, (int) (((-point3D.y) + point3D.z) * ySkew));
	}
	
	public class Point2D {
		public Point2D(int x, int y) {
			this.x = x;
			this.y = y;
		}
		int x = 0;
		int y = 0;
	}

	public class Point3D {
		public Point3D(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		int x = 0;
		int y = 0;
		int z = 0;
	}
}
