package simulator.view.viewer;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import simulator.Constants.NODE_TYPE;
import simulator.control.Controller;
import simulator.model.entity.Entity;
import simulator.model.map.Map;
import simulator.view.viewer.elements3d.Matrix;
import simulator.view.viewer.elements3d.Triangulate;
import simulator.view.viewer.elements3d.Vector3D;
import simulator.view.viewer.elements3d.elements.Mesh;
import simulator.view.viewer.elements3d.elements.Triangle;


/**
 * Viewer3D 3D view of the simulation
 * special thanks to https://www.onelonecoder.com
 * @author fabrizioortega
 *
 */
public class Viewer3D extends AbstractViewer{
	Mesh mesh;
	Matrix projectionMatrix;
	int width=700,height=700;
	float zfar=1000f,znear=0.1f;
	float a=(float)width/(float)height,f=90f,q=zfar/(zfar-znear);
	float frad = (float) (1f/ (float)Math.tan(f*0.5f/180f*3.14159f));
	float offset = 20f;
	float time = 0.0f;
	Vector3D camera;
	Vector3D lookDir;
	float fYaw,fXaw;
	
	public Viewer3D(Controller ctrl, int width, int height) {
		super(ctrl, width, height);
	}
	public Viewer3D(Controller ctrl) {
		this(ctrl,700,700);
		
		this.mesh = new Mesh();
		this.mesh.setTriangles(Triangulate.convert("test1000void2", 5f, 100,100));
		this.projectionMatrix = Matrix.Matrix_MakeProjection(f, a, znear, zfar);
		
		camera = new Vector3D(0f,0f,0f);
		lookDir = new Vector3D(0f,0f,1f);
		
		MouseAdapter mouseA = new MouseAdapter() {
			
			boolean pressed = false;
    		Point current = null;
    		@Override
			public void mousePressed(MouseEvent e) {
    			//advance(0.0005f);
    			if(!pressed) {
    				pressed = true;
        			current = e.getPoint();
    			}
			}
    		@Override
			public void mouseReleased(MouseEvent e) {pressed = false;}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(pressed) {
					Point dir = e.getPoint();
					if(dir.equals(current))return;
					int dx = dir.x-current.x;
					int dy = dir.y-current.y;
					
					float decreaseFactor = 10000.0f;
					if(Math.abs(dx) >10)Viewer3D.this.fYaw +=(float)dx/decreaseFactor;
					if(Math.abs(dy) >10)Viewer3D.this.fXaw +=(float)dy/decreaseFactor;
							
					repaint();
				}
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Vector3D vForward = Vector3D.mul(Viewer3D.this.lookDir, -((float)e.getWheelRotation())*0.3f);
				Viewer3D.this.camera = Vector3D.add(Viewer3D.this.camera, vForward);
				repaint();
			}
		};
		this.addMouseListener(mouseA);
		this.addMouseMotionListener(mouseA);;
		this.addMouseWheelListener(mouseA);
		
		
		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent ke) {
				int c = ke.getKeyCode();
				switch(c) {
				  case KeyEvent.VK_S:
					  Viewer3D.this.camera.y-=0.8f;
				    break;
				  case KeyEvent.VK_W:
					  Viewer3D.this.camera.y+=0.8f;
				    break;
				  case KeyEvent.VK_D:
					  Viewer3D.this.camera.x-=0.8f;
				    break;
				  case KeyEvent.VK_A:
					  Viewer3D.this.camera.x+=0.8f;
				    break;
				  case KeyEvent.VK_R:
					  Viewer3D.this.camera.z-=0.8f;
				    break;
				  case KeyEvent.VK_F:
					  Viewer3D.this.camera.z+=0.8f;
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
	}

	@Override
	public void onRegister(List<Entity> entities, Map map, int time) {
		this.mapImg = map.getElevationImage();
		this.entities = entities;
		
		//updateImage();
	}

	@Override
	public void onUpdate(List<Entity> entities, Map map, int time) {
		if(active) {
			this.mapImg = map.getElevationImage();		
			this.entities=entities;
			updateImage();

	        this.requestFocusInWindow();
		}
	}
	private BufferedImage reduceImage(Image img, int width, int height) {
		Image imgS = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage image2 = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image2.createGraphics();
		g2d.drawImage(imgS, 0, 0, width, height, null);
		return image2;
	}
	/**
	 * Render the 3d image
	 */
	public void updateImage() {
		//System.out.println(this.mesh.triangles.size()+" triangles");
		//System.out.println("look: "+lookDir);
		//System.out.println("camera: "+camera);
		//rescale the image otherwise will be too big
		BufferedImage reducedImg = reduceImage(mapImg, 100, 100);
		
		bufferGraphics.setColor(Color.black);
		bufferGraphics.fillRect(0, 0, bufferImage.getWidth(), bufferImage.getHeight());
		
		
		Map map = this.ctrl.getMap();
		
		int factory = mapImg.getHeight(null)/reducedImg.getHeight();
		int factorx = mapImg.getWidth(null)/reducedImg.getWidth();
		
		Matrix zrotation = Matrix.Matrix_MakeRotationZ(time);
		Matrix xrotation = Matrix.Matrix_MakeRotationX(time);
		
		Matrix matTrans = Matrix.translate(0f, 0f, offset);
		
		Matrix matWorld = Matrix.identity();
		matWorld = zrotation.multiply(xrotation);
		matWorld = matWorld.multiply(matTrans);
		
		Vector3D vUp = new Vector3D(0f,1f,0f);
		//Vector3D vTarget = camera.add(lookDir);
		Vector3D vTarget = new Vector3D(0f,0f,1f);
		Matrix matCameraRot = Matrix.Matrix_MakeRotationY(fYaw);
		matCameraRot = matCameraRot.multiply(Matrix.Matrix_MakeRotationX(fXaw));
		lookDir = Vector3D.multiplyMatrix(vTarget, matCameraRot);
		//lookDir = new Vector3D(0.6082864f,0.7925648f,-0.04276373f);
		vTarget = Vector3D.add(camera, lookDir);
		
		Matrix matCamera = Matrix.Matrix_PointAt(camera, vTarget, vUp);
		Matrix matView = Matrix.Matrix_QuickInverse(matCamera);

		List<Triangle>trianglesToRaster = new ArrayList<Triangle>();
		
		for(Triangle tri:mesh.getTriangles()) {
			if(map.getNodeAt((int)tri.points[0].x*factorx, (int)tri.points[0].y*factory).type == NODE_TYPE.VOID)continue;
			Triangle triTransformed = new Triangle(Vector3D.multiplyMatrix(tri.points[0], matWorld),
												   Vector3D.multiplyMatrix(tri.points[1], matWorld),
												   Vector3D.multiplyMatrix(tri.points[2], matWorld));
			
			triTransformed.col = tri.col;
			
			Vector3D l1 = Vector3D.sub(triTransformed.points[1], triTransformed.points[0]);
			Vector3D l2 = Vector3D.sub(triTransformed.points[2], triTransformed.points[0]);

			Vector3D normal = Vector3D.crossProduct(l1, l2);
			normal = Vector3D.normal(normal);
			
			Vector3D cameraRay = Vector3D.sub(triTransformed.points[0], camera);

			if(Vector3D.dotProduct(normal, cameraRay) < 0f) {
				Triangle triViewed = new Triangle(Vector3D.multiplyMatrix(triTransformed.points[0], matView),
												  Vector3D.multiplyMatrix(triTransformed.points[1], matView),
												  Vector3D.multiplyMatrix(triTransformed.points[2], matView));
				triViewed.col = triTransformed.col;
				
				
				int nClippedTriangles = 0;
				Triangle clipped[] = new Triangle[] {new Triangle(),new Triangle()};
				nClippedTriangles = Triangle.Triangle_ClipAgainstPlane(new Vector3D(0.0f, 0.0f, 0.1f), 
																	   new Vector3D(0.0f, 0.0f, 1.0f), 
																	   triViewed, 
																	   clipped);

				for (int n = 0; n < nClippedTriangles; n++){
					//project
					Triangle triProjected = new Triangle(Vector3D.multiplyMatrix(clipped[n].points[0], projectionMatrix),
														 Vector3D.multiplyMatrix(clipped[n].points[1], projectionMatrix),
														 Vector3D.multiplyMatrix(clipped[n].points[2], projectionMatrix));
					triProjected.col = clipped[n].col;
					
					triProjected.points[0] = Vector3D.div(triProjected.points[0], triProjected.points[0].w);
					triProjected.points[1] = Vector3D.div(triProjected.points[1], triProjected.points[1].w);
					triProjected.points[2] = Vector3D.div(triProjected.points[2], triProjected.points[2].w);
					
					triProjected.points[0].x *= -1.0f;
					triProjected.points[1].x *= -1.0f;
					triProjected.points[2].x *= -1.0f;
					triProjected.points[0].y *= -1.0f;
					triProjected.points[1].y *= -1.0f;
					triProjected.points[2].y *= -1.0f;
					
					Vector3D offsetView = new Vector3D(1f,1f,0f);
					triProjected.points[0] = Vector3D.add(triProjected.points[0], offsetView);
					triProjected.points[1] = Vector3D.add(triProjected.points[1], offsetView);
					triProjected.points[2] = Vector3D.add(triProjected.points[2], offsetView);
					
					triProjected.points[0].x *= 0.5f * (float)this.getWidth();
					triProjected.points[0].y *= 0.5f * (float)this.getHeight();
					triProjected.points[1].x *= 0.5f * (float)this.getWidth();
					triProjected.points[1].y *= 0.5f * (float)this.getHeight();
					triProjected.points[2].x *= 0.5f * (float)this.getWidth();
					triProjected.points[2].y *= 0.5f * (float)this.getHeight();
					
					
					trianglesToRaster.add(triProjected);
//					Triangle t = triProjected;
//					bufferGraphics.setColor(Color.white);
//					bufferGraphics.fillPolygon(new int[] {(int) t.points[0].x,(int)t.points[1].x,(int)t.points[2].x}, new int[] {(int)t.points[0].y,(int)t.points[1].y,(int)t.points[2].y}, 3);
//					bufferGraphics.setColor(Color.black);
//					bufferGraphics.drawPolygon(new int[] {(int) t.points[0].x,(int)t.points[1].x,(int)t.points[2].x}, new int[] {(int)t.points[0].y,(int)t.points[1].y,(int)t.points[2].y}, 3);
//				
				}
			}
			//if(true)return;
//			Collections.sort(trianglesToRaster, new Comparator<Triangle>(){
//				@Override
//				public int compare(Triangle t1, Triangle t2) {
//					float z1 = (t1.points[0].z + t1.points[1].z + t1.points[2].z) / 3.0f;
//					float z2 = (t2.points[0].z + t2.points[1].z + t2.points[2].z) / 3.0f;
//					return z1<z2?1:z1>z2?-1:0;
//				}
//			});
			for (Triangle triToRaster : trianglesToRaster)
			{
				
				// Clip triangles against all four screen edges, this could yield
				// a bunch of triangles, so create a queue that we traverse to 
				//  ensure we only test new triangles generated against planes
				Triangle clipped[] = new Triangle[] {new Triangle(),new Triangle()};
				Queue<Triangle> listTriangles = new LinkedList<Triangle>();
				
				// Add initial triangle
				listTriangles.add(triToRaster);
				int nNewTriangles = 1;

				for (int p = 0; p < 4; p++)
				{
					int nTrisToAdd = 0;
					while (nNewTriangles > 0)
					{
						// Take triangle from front of queue
						Triangle test = listTriangles.poll();
						nNewTriangles--;
						// Clip it against a plane. We only need to test each 
						// subsequent plane, against subsequent new triangles
						// as all triangles after a plane clip are guaranteed
						// to lie on the inside of the plane. I like how this
						// comment is almost completely and utterly justified
						switch (p)
						{
						case 0:	nTrisToAdd = Triangle.Triangle_ClipAgainstPlane(new Vector3D(0.0f, 0.0f, 0.0f), new Vector3D(0.0f, 1.0f, 0.0f), test, clipped); break;
						case 1:	nTrisToAdd = Triangle.Triangle_ClipAgainstPlane(new Vector3D(0.0f, (float)this.getHeight() - 1, 0.0f), new Vector3D(0.0f, -1.0f, 0.0f), test, clipped); break;
						case 2:	nTrisToAdd = Triangle.Triangle_ClipAgainstPlane(new Vector3D(0.0f, 0.0f, 0.0f), new Vector3D(1.0f, 0.0f, 0.0f), test, clipped); break;
						case 3:	nTrisToAdd = Triangle.Triangle_ClipAgainstPlane(new Vector3D((float)this.getWidth() - 1, 0.0f, 0.0f), new Vector3D(-1.0f, 0.0f, 0.0f), test, clipped); break;
						}

						// Clipping may yield a variable number of triangles, so
						// add these new ones to the back of the queue for subsequent
						// clipping against next planes
						for (int w = 0; w < nTrisToAdd; w++) {
							listTriangles.add(clipped[w]);
						}
					}
					nNewTriangles = listTriangles.size();
				}
				for(Triangle t:listTriangles) {
					//bufferGraphics.setColor(t.col);
					//bufferGraphics.fillPolygon(new int[] {(int) t.points[0].x,(int)t.points[1].x,(int)t.points[2].x}, new int[] {(int)t.points[0].y,(int)t.points[1].y,(int)t.points[2].y}, 3);
					bufferGraphics.setColor(Color.white);
					bufferGraphics.drawPolygon(new int[] {(int) t.points[0].x,(int)t.points[1].x,(int)t.points[2].x}, new int[] {(int)t.points[0].y,(int)t.points[1].y,(int)t.points[2].y}, 3);

				}
			}
			
		}
		//map render
//		for(int j=0;j<reducedImg.getWidth();j++) {
//			for(int i=0;i<reducedImg.getHeight();i++) {
//				if(map.getNodeAt(j*factorx, i*factory).type == NODE_TYPE.VOID)continue;
//				
//			}
//		}
		
		//entities render
//		for(Entity e:entities) {
//			if(e.node.type == NODE_TYPE.VOID)continue;
//			Point2D scaledCoord = new Point2D(e.node.x/factorx, e.node.y/factory);
//			Point2D p1 = transform3D(
//		             new Point3D(scaledCoord.x * xs, 
//		            		 	(int) (new Color(reducedImg.getRGB(scaledCoord.x, scaledCoord.y),false).getGreen() * inskew), 
//		            		 	 scaledCoord.y * ys));
//			bufferGraphics.drawImage(e.getImage(), p1.x+originX, p1.y+originY, null);
//		}
//		
		for(Entity e:entities) {
			Vector3D env = new Vector3D(e.node.x/factorx, e.node.y/factory, e.node.elevation*5f);
			Vector3D triTransformed = Vector3D.multiplyMatrix(env, matWorld);
			Vector3D triViewed = Vector3D.multiplyMatrix(triTransformed, matView);
			Vector3D triProjected = Vector3D.multiplyMatrix(triViewed, projectionMatrix);
			
			triProjected = Vector3D.div(triProjected, triProjected.w);
			triProjected.x *= -1.0f;
			triProjected.y *= -1.0f;
			Vector3D offsetView = new Vector3D(1f,1f,0f);
			triProjected = Vector3D.add(triProjected, offsetView);
			triProjected.x *= 0.5f * (float)this.getWidth();
			triProjected.y *= 0.5f * (float)this.getHeight();
			
	
			bufferGraphics.drawImage(e.getImage(), (int)triProjected.x, (int)triProjected.y, null);
		}
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
	
}
//public class Viewer3D extends AbstractViewer{
//	
//	//variables to control the render
//	private float xSkew;
//	private float ySkew;
//	int originX;
//	int originY;
//	int xs;//square size
//	int ys;
//	private MouseAdapter mouseAdapter;
//	private KeyListener keyListener;
//	
//	public Viewer3D(Controller ctrl) {
//		super(ctrl,700,700);
//		
//		xSkew=1.0f;
//		ySkew=1.0f;
//		originX=0;
//		originY=0;
//		xs = 10;
//		ys = xs;
//		//map.addObserver(this);
//		
//	    keyListener = new KeyListener() {
//
//			@Override
//			public void keyPressed(KeyEvent ke) {
//				int c = ke.getKeyCode();
//				switch(c) {
//				  case KeyEvent.VK_W:
//				    originY-=10;
//				    break;
//				  case KeyEvent.VK_S:
//					originY+=10;
//				    break;
//				  case KeyEvent.VK_A:
//					originX-=10;
//				    break;
//				  case KeyEvent.VK_D:
//					originX+=10;
//				    break;
//				  default:				    
//				}
//				updateImage();
//			}
//
//			@Override
//			public void keyReleased(KeyEvent ke) {
//				
//			}
//
//			@Override
//			public void keyTyped(KeyEvent ke) {
//			}
//			
//		};
//		this.addKeyListener(keyListener);
//		this.setFocusable(true);
//        this.requestFocusInWindow();
//		
//		mouseAdapter = new MouseAdapter() {
//    		boolean pressed = false;
//    		Point current = null;
//
//    		
//    		@Override
//    		public void mouseWheelMoved(MouseWheelEvent e) {
//    			
//    			if(e.getWheelRotation()!=0) {
//    				xs+=e.getWheelRotation();
//    				ys+=e.getWheelRotation();
//    				updateImage();
//    			}
//    			//System.out.println(e.getWheelRotation()); 
//    		}
//    		@Override
//			public void mouseClicked(MouseEvent e) {
//    			requestFocusInWindow();
//    			//pressed = true;
//    			//current = e.getPoint();
//    			//System.out.println("clicked");
//			}
//    		@Override
//			public void mousePressed(MouseEvent e) {
//    			if(!pressed) {
//    				pressed = true;
//        			current = e.getPoint();
//    			}
//    			//
//			}
//    		@Override
//			public void mouseReleased(MouseEvent e) {
//    			pressed = false;
//    			
//			}
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				if(pressed) {
//					Point dir = e.getPoint();
//					if(dir.equals(current))return;
//					int dx = dir.x-current.x;
//					int dy = dir.y-current.y;
//					
//					float decreaseFactor = 1000.0f;
//	    			xSkew+=(float)dx/decreaseFactor;
//					ySkew+=(float)dy/decreaseFactor;
//					
//					updateImage();
//				}
//			}
//    	};
//		this.addMouseListener(mouseAdapter);
//		this.addMouseMotionListener(mouseAdapter);
//		this.addMouseWheelListener(mouseAdapter);
//		
//	}
//	
//	
//	@Override
//	public void onRegister(List<Entity> entities, Map map, int time) {
//		this.mapImg = map.getElevationImage();
//		this.entities = entities;
//		updateImage();
//	}
//
//	/**
//	 * Update only if it is active to optimize
//	 */
//	@Override
//	public void onUpdate(List<Entity> entities, Map map, int time) {
//		if(active) {
//			this.mapImg = map.getElevationImage();		
//			this.entities=entities;
//			updateImage();
//			
//		}
//		
//	}
//	private BufferedImage reduceImage(Image img) {
//		int size=50;
//		Image imgS = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
//		BufferedImage image2 = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d = (Graphics2D)image2.createGraphics();
//		g2d.drawImage(imgS, 0, 0, size, size, null);
//		return image2;
//	}
//	/**
//	 * Render the 3d image
//	 */
//	public void updateImage() {
//		//rescale the image otherwise will be too big
//		BufferedImage reducedImg = reduceImage(mapImg);
//		
//		bufferGraphics.setColor(new Color(0,0,0,255));
//		bufferGraphics.fillRect(0, 0, bufferImage.getWidth(), bufferImage.getHeight());
//		bufferGraphics.setColor(new Color(0,255,0,150));
//		
//		float inskew = 2.0f;//factor to make more viewable the hills
//
//		Point2D point1=null;
//		Point2D point2=null;
//		Point2D point3=null;
//		
//		Map map = this.ctrl.getMap();
//		
//		int factorx = mapImg.getHeight(null)/reducedImg.getHeight();
//		int factory = mapImg.getWidth(null)/reducedImg.getWidth();
//		//map render
//		for(int j=0;j<reducedImg.getWidth();j++) {
//			for(int i=0;i<reducedImg.getHeight();i++) {
//				if(map.getNodeAt(j*factorx, i*factory).type == NODE_TYPE.VOID)continue;
//				//System.out.println(j+" "+i);
//				point1 = transform3D(new Point3D(j * xs, 
//						 						(int) (new Color(reducedImg.getRGB(j, i),true).getGreen() * inskew), 
//						 						 i * ys));
//				
//				
//				if(j<reducedImg.getWidth()-1) {
//					
//					point2 = transform3D(
//				             new Point3D((j+1) * xs, 
//							 (int) (new Color(reducedImg.getRGB(j+1, i),false).getGreen() * inskew), 
//							  i * ys));
//					bufferGraphics.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
//
//
//				
//				}
//				if(i<reducedImg.getHeight()-1) { 
//
//					point3 = transform3D(
//							new Point3D(j * xs,
//											 (int) (new Color(reducedImg.getRGB(j, i+1),false).getGreen() * inskew), 
//											 (i + 1) * ys));
//					bufferGraphics.drawLine(point1.x + originX, point1.y + originY, point3.x + originX, point3.y + originY);
//				}
//			}
//		}
//		
//		//entities render
//		for(Entity e:entities) {
//			if(e.node.type == NODE_TYPE.VOID)continue;
//			Point2D scaledCoord = new Point2D(e.node.x/factorx, e.node.y/factory);
//			Point2D p1 = transform3D(
//		             new Point3D(scaledCoord.x * xs, 
//		            		 	(int) (new Color(reducedImg.getRGB(scaledCoord.x, scaledCoord.y),false).getGreen() * inskew), 
//		            		 	 scaledCoord.y * ys));
//			bufferGraphics.drawImage(e.getImage(), p1.x+originX, p1.y+originY, null);
//		}
//		
//		repaint();
//	}
//	@Override
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//
//	}
//	public void changeSkew(float change) {
//		this.xSkew+=change;
//		updateImage();
//	}
//	/**
//	 * Transform to 2d 
//	 * @param point3D
//	 * @return
//	 */
//	public Point2D transform3D(Point3D point3D) {
//		//different angles 
//		return new Point2D((int)((point3D.x + point3D.z)*xSkew), (int) (((-point3D.y) + point3D.z - point3D.x) * ySkew));
//		//return new Point2D((int)((point3D.x + point3D.z)*xSkew), (int) (((-point3D.y) + point3D.z) * ySkew));
//	}
//	
//	public class Point2D {
//		public Point2D(int x, int y) {
//			this.x = x;
//			this.y = y;
//		}
//		int x = 0;
//		int y = 0;
//	}
//
//	public class Point3D {
//		public Point3D(int x, int y, int z) {
//			this.x = x;
//			this.y = y;
//			this.z = z;
//		}
//		int x = 0;
//		int y = 0;
//		int z = 0;
//	}
//
//	
//}
