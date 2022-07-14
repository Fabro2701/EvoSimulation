package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import simulator.view.viewer.Viewer3D.Point3D;


//credits to https://www.softwaredeveloperzone.com/simple-3d-graphics-using-java/
public class MapElevationViewPanel extends JPanel{
	private float xSkew=1.0f;
	private float ySkew=1.0f;
	int originX=0;
	int originY=0;
	int xs = 10;
	int ys = xs;
	private BufferedImage image;
	private BufferedImage rootImage;
	private MouseAdapter mouseAdapter;
	private KeyListener keyListener;
	
	public MapElevationViewPanel(MapElevationModifierPanel map) {
		map.addObserver(this);
		setInitProperties();
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
				repaint();
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
    				repaint();
    			}
    			//System.out.println(e.getWheelRotation()); 
    		}
    		@Override
			public void mouseClicked(MouseEvent e) {

    	        //requestFocus();
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
					//System.out.println(xSkew+" "+ySkew);
					repaint();
				}
			}
    	};
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
	}
	public void setInitProperties() {
		xSkew=1.0f;
		ySkew=1.0f;
		originX=0;
		originY=0;
		xs = 10;
		ys = xs;

        this.requestFocusInWindow();
		repaint();
	}
	public void config() {
		image = new BufferedImage(this.getWidth(), this.getHeight(),BufferedImage.TYPE_INT_RGB);
	
		createNewCanvas();
	}
	public void update(BufferedImage inImage) {
		rootImage=inImage;

        this.requestFocusInWindow();
		repaint();
	}
	private BufferedImage reduceImage(BufferedImage img) {
		int size=50;
		Image imgS = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
		BufferedImage image2 = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)image2.createGraphics();
		g2d.drawImage(imgS, 0, 0, size, size, null);
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
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.green);
		
		//originX;
		//originY;
		Point2D point1;
		Point2D point2;
		
		float inskew = 2.0f;
		
		for(int i=0;i<reducedImg.getHeight();i++) {
			for(int j=0;j<reducedImg.getWidth();j++) {
				point1 = transform3D(
			             new Point3D(j * xs, 
						 (int) (new Color(reducedImg.getRGB(j, i),false).getGreen() * inskew), 
						  i * ys));
				if(j<reducedImg.getWidth()-1) {
					
					point2 = transform3D(
				             new Point3D((j+1) * xs, 
							 (int) (new Color(reducedImg.getRGB(j+1, i),false).getGreen() * inskew), 
							  i * ys));
					//g2d.fillOval(point1.x, point1.y, 20, 20);
					
					//System.out.println(point1.x + originX+" "+point1.y + originY+"  "+point2.x + originX+" "+point2.y + originY);
					//try {
					g2d.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
					
				
				}
				if(i<reducedImg.getHeight()-1) { 

					point2 = transform3D(
							new Point3D(j * xs,
											 (int) (new Color(reducedImg.getRGB(j, i+1),false).getGreen() * inskew), 
											 (i + 1) * ys));
					
					g2d.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
				}
			}
		}
		g2dd.drawImage(image, 0, 0, null);
	}
	public void changeSkew(float change) {
		this.xSkew+=change;
		repaint();
	}
	public Point2D transform3D(Point3D point3D) {
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
