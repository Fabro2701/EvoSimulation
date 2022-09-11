package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import test.Iso3D.Point2D;
import test.Iso3D.Point3D;




public class IsoMapTest extends JFrame{
	
	public IsoMapTest() {
		this.setPreferredSize(new Dimension(800,800));
		
		
		
		initGUI();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	private void initGUI() {
		JPanel mainPanel = new JPanel();
		this.setContentPane(mainPanel);
		
		JPanel viewPanel = new ViewPanel();
		mainPanel.add(viewPanel);
	}
	
	public  class ViewPanel extends JPanel{
		private int elevation[][];
		private int HEIGHT;
		private int WIDTH;
		public  int ySkew;
		public ViewPanel() {
			ySkew=1;
			HEIGHT=100;
			WIDTH=HEIGHT;
			elevation = new int[HEIGHT][WIDTH];
			for(int i=0;i<HEIGHT;i++) {
				for(int j=0;j<WIDTH;j++) {
					//elevation[i][j]=i+j;
					//elevation[i][j]=(i+j-HEIGHT/2)*(i+j-HEIGHT/2);
				}
			}
			setElevation();
			this.setPreferredSize(new Dimension(800,800));
			this.repaint();
		}
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.black);
			g2.fillRect(0, 0, this.getHeight(), this.getWidth());
			g2.setColor(Color.green);
			
			int xs = 5;
			int ys = xs;
			int originX = -100;
			int originY = 300;
			Point2D point1;
			Point2D point2;
			
			
			for(int i=0;i<HEIGHT;i++) {
				for(int j=0;j<WIDTH;j++) {
					if(i<HEIGHT-1) {
						point1 = transform3D(
					             new Point3D(i * xs, 
								 (int) (elevation[i][j] * ySkew), 
								  j * ys));
						point2 = transform3D(
					             new Point3D((i+1) * xs, 
								 (int) (elevation[i+1][j] * ySkew), 
								  j * ys));
						//g2.fillOval(point1.x, point1.y, 2, 2);
						g.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
						//System.out.println(point1.x);
						//System.out.println(point1.y);
					}
					if(j<WIDTH-1) {
						point1 = transform3D(
								new Point3D(i * xs, 
												 (int) (elevation[i][j] * ySkew), 
												  j * ys));
						point2 = transform3D(
								new Point3D(i * xs,
												 (int) (elevation[i][j+1] * ySkew), 
												 (j + 1) * ys));
						
						g.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
					
					}
				}
			}
			
		}
		public void setElevation() {
			try {
				BufferedImage image = ImageIO.read(new File("resources/maps/test1.jpg"));
				Image img = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				BufferedImage image2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D)image2.createGraphics();
				g2d.drawImage(img, 0, 0, 100, 100, null);
				for (int y = 0; y < image2.getHeight(); y++) {
			         for (int x = 0; x < image2.getWidth(); x++) {   
			        	 int pixel = image2.getRGB(x,y);
			        	 Color color = new Color(pixel, false);
			        	 elevation[y][x]=color.getRed();
			         }
			    }
				ImageIO.write(image2,"jpg",new File("resources/maps/test2.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->{
			new IsoMapTest();
		});
	
	}
}
