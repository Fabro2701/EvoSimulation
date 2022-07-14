package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MapElevationModifierPanel extends JPanel{
	
	private MouseAdapter mouseListener;
	private BufferedImage image;
	int sizeStroke;
	int dx[];
	int dy[];
	int intensity[];
	int intensityFactor;
	private MapElevationViewPanel viewer;
	
	public MapElevationModifierPanel() {	
		sizeStroke=50;
		intensityFactor=10;
	}
	public void config() {
		mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseDragged(MouseEvent e) {
		
				
				if(SwingUtilities.isLeftMouseButton(e)) {
					increaseElevation(e.getPoint());
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					decreaseElevation(e.getPoint());
				}
			}
			
			
		};
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
		
		setDrawingProperties();
		
		
		image = new BufferedImage(this.getHeight(), this.getWidth(), BufferedImage.TYPE_INT_RGB);
		createNewCanvas();
		if(viewer!=null)viewer.update(image);
	}
	private void setDrawingProperties() {
		dx = new int[(1+2*sizeStroke)*(1+2*sizeStroke)];// {0,0,1,1,1,0,-1,-1,-1};
		dy = new int[dx.length];// {0,-1,-1,0,1,1,1,0,-1};
		
		for(int i=0;i<dx.length;i++) {
			dx[i]=(i%(1+2*sizeStroke))-sizeStroke;
			dy[i]=(i/(1+2*sizeStroke))-sizeStroke;
		}
		
		intensity = new int[dx.length];
		for(int i=0;i<dx.length;i++) {
			/*intensity[i]=2*(Math.abs(dx[i])*Math.abs(dy[i]));
			intensity[i]=intensity[i]/(Math.abs(dx[i])+Math.abs(dy[i])+1);
			intensity[i]=(100-intensity[i])/10;*/
			intensity[i]=(int)(((200.0-(Math.abs(dx[i])+Math.abs(dy[i])))/20.0)*(intensityFactor/10.0));
			//intensity[i]= ((float)(Math.abs(dx[i])+Math.abs(dy[i]))*(-50.0f/(2.0f*(float)sizeStroke))+50.0f);
			//System.out.println(intensity[i]);
		}
	}
	private void increaseElevation(Point point) {
		
		
		
		for(int i=0;i<dx.length;i++) {
			
			int xp = point.x+dx[i];
			int yp = point.y+dy[i];
			if(xp>=0&&xp<image.getWidth()&&yp>=0&&yp<image.getHeight()) {
				int pixel = image.getRGB(xp, yp);
				Color color = new Color(pixel,false);
				int newGreen = (int) (color.getGreen()-intensity[i]);
				newGreen = newGreen>=color.getGreen()?color.getGreen():newGreen;
				Color newColor = new Color(255,
										   newGreen<=0?0:newGreen,
										   0);
				image.setRGB(xp, yp, newColor.getRGB());
			}
			
			
			
		}
		repaint();
		if(viewer!=null)viewer.update(image);
	}
	private void decreaseElevation(Point point) {
		for(int i=0;i<dx.length;i++) {
			
			int xp = point.x+dx[i];
			int yp = point.y+dy[i];
			if(xp>=0&&xp<image.getWidth()&&yp>=0&&yp<image.getHeight()) {
				int pixel = image.getRGB(xp, yp);
				Color color = new Color(pixel,false);
				int newGreen = (int) (color.getGreen()+intensity[i]);
				newGreen = newGreen<=color.getGreen()?color.getGreen():newGreen;
				Color newColor = new Color(255,
										   newGreen>=255?255:newGreen,
										   0);
				image.setRGB(xp, yp, newColor.getRGB());
			}
			
		}
		repaint();
		if(viewer!=null)viewer.update(image);
	}
	private void createNewCanvas() {
		Color initColor = new Color(255,(int)255/2,0);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(initColor);
		g2d.fillRect(0, 0, this.getHeight(), this.getWidth());
		repaint();
		if(viewer!=null)viewer.update(image);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, 0, 0, null);
	}
	
	public BufferedImage getImage() {return image;}
	public void addObserver(MapElevationViewPanel mapElevationViewPanel) {
		viewer=mapElevationViewPanel;
	}
	public void setImage(BufferedImage image2) {
		image=image2;
		repaint();
		if(viewer!=null)viewer.update(image);
	}
	public void changeSizeStroke(int value) {
		sizeStroke=value;
		setDrawingProperties();
		viewer.update(image);
	}
	public void changeIntensity(int value) {
		intensityFactor=value;
		setDrawingProperties();
		viewer.update(image);
		
	}
	

	
}
