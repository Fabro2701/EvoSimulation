package simulator.view.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.List;


import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.map.Map;

import util.Util;


/**
 * Viewer3D 3D view of the simulation
 * @author fabrizioortega
 *
 */
public class Viewer3D extends AbstractViewer{
	
	//variables to control the render
	private float xSkew;
	private float ySkew;
	int originX;
	int originY;
	int xs;//square size
	int ys;
	private MouseAdapter mouseAdapter;
	private KeyListener keyListener;
	
	public Viewer3D(Controller ctrl) {
		super(ctrl,700,700);
		
		xSkew=1.0f;
		ySkew=1.0f;
		originX=0;
		originY=0;
		xs = 10;
		ys = xs;
		//map.addObserver(this);
		
	    keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent ke) {
				int c = ke.getKeyCode();
				switch(c) {
				  case KeyEvent.VK_W:
				    originY-=10;
				    break;
				  case KeyEvent.VK_S:
					originY+=10;
				    break;
				  case KeyEvent.VK_A:
					originX-=10;
				    break;
				  case KeyEvent.VK_D:
					originX+=10;
				    break;
				  default:				    
				}
				updateImage();
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				
			}

			@Override
			public void keyTyped(KeyEvent ke) {
			}
			
		};
		this.addKeyListener(keyListener);
		this.setFocusable(true);
        this.requestFocusInWindow();
		
		mouseAdapter = new MouseAdapter() {
    		boolean pressed = false;
    		Point current = null;

    		
    		@Override
    		public void mouseWheelMoved(MouseWheelEvent e) {
    			
    			if(e.getWheelRotation()!=0) {
    				xs+=e.getWheelRotation();
    				ys+=e.getWheelRotation();
    				updateImage();
    			}
    			//System.out.println(e.getWheelRotation()); 
    		}
    		@Override
			public void mouseClicked(MouseEvent e) {
    			requestFocusInWindow();
    			//pressed = true;
    			//current = e.getPoint();
    			//System.out.println("clicked");
			}
    		@Override
			public void mousePressed(MouseEvent e) {
    			if(!pressed) {
    				pressed = true;
        			current = e.getPoint();
    			}
    			//
			}
    		@Override
			public void mouseReleased(MouseEvent e) {
    			pressed = false;
    			
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(pressed) {
					Point dir = e.getPoint();
					if(dir.equals(current))return;
					int dx = dir.x-current.x;
					int dy = dir.y-current.y;
					
					float decreaseFactor = 1000.0f;
	    			xSkew+=(float)dx/decreaseFactor;
					ySkew+=(float)dy/decreaseFactor;
					
					updateImage();
				}
			}
    	};
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
	}
	
	
	@Override
	public void onRegister(List<Entity> entities, Map map, int time) {
		this.mapImg = map.getElevationImage();
		this.entities = entities;
		updateImage();
	}

	/**
	 * Update only if it is active to optimize
	 */
	@Override
	public void onUpdate(List<Entity> entities, Map map, int time) {
		if(active) {
			this.mapImg = map.getElevationImage();		
			this.entities=entities;
			updateImage();
			
		}
		
	}
	private BufferedImage reduceImage(Image img) {
		int size=50;
		Image imgS = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
		BufferedImage image2 = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image2.createGraphics();
		g2d.drawImage(imgS, 0, 0, size, size, null);
		return image2;
	}
	/**
	 * Render the 3d image
	 */
	public void updateImage() {
		//rescale the image otherwise will be too big
		BufferedImage reducedImg = reduceImage(mapImg);
		
		bufferGraphics.setColor(new Color(0,0,0,255));
		bufferGraphics.fillRect(0, 0, bufferImage.getWidth(), bufferImage.getHeight());
		bufferGraphics.setColor(new Color(0,255,0,150));
		
		float inskew = 2.0f;//factor to make more viewable the hills

		Point2D point1=null;
		Point2D point2=null;
		Point2D point3=null;
		
		//map render
		for(int i=0;i<reducedImg.getHeight();i++) {
			for(int j=0;j<reducedImg.getWidth();j++) {
				
				point1 = transform3D(new Point3D(j * xs, 
						 						(int) (new Color(reducedImg.getRGB(j, i),true).getGreen() * inskew), 
						 						 i * ys));
				
				
				if(j<reducedImg.getWidth()-1) {
					
					point2 = transform3D(
				             new Point3D((j+1) * xs, 
							 (int) (new Color(reducedImg.getRGB(j+1, i),false).getGreen() * inskew), 
							  i * ys));
					bufferGraphics.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);


				
				}
				if(i<reducedImg.getHeight()-1) { 

					point3 = transform3D(
							new Point3D(j * xs,
											 (int) (new Color(reducedImg.getRGB(j, i+1),false).getGreen() * inskew), 
											 (i + 1) * ys));
					bufferGraphics.drawLine(point1.x + originX, point1.y + originY, point3.x + originX, point3.y + originY);
				}
			}
		}
		
		//entities render
		int factorx = mapImg.getHeight(null)/reducedImg.getHeight();
		int factory = mapImg.getWidth(null)/reducedImg.getWidth();
		for(Entity e:entities) {
			Point2D scaledCoord = new Point2D(e.node.x/factorx, e.node.y/factory);
			Point2D p1 = transform3D(
		             new Point3D(scaledCoord.x * xs, 
		            		 	(int) (new Color(reducedImg.getRGB(scaledCoord.x, scaledCoord.y),false).getGreen() * inskew), 
		            		 	 scaledCoord.y * ys));
			bufferGraphics.drawImage(e.getImage(), p1.x+originX, p1.y+originY, null);
		}
		
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
	public void changeSkew(float change) {
		this.xSkew+=change;
		updateImage();
	}
	/**
	 * Transform to 2d 
	 * @param point3D
	 * @return
	 */
	public Point2D transform3D(Point3D point3D) {
		//different angles 
		return new Point2D((int)((point3D.x + point3D.z)*xSkew), (int) (((-point3D.y) + point3D.z - point3D.x) * ySkew));
		//return new Point2D((int)((point3D.x + point3D.z)*xSkew), (int) (((-point3D.y) + point3D.z) * ySkew));
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
