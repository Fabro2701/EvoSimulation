package genome_editing.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import genome_editing.model.elements.Matrix;
import genome_editing.model.elements.Vector3D;
import genome_editing.model.genome.BaseSet;
import simulator.Constants;
import simulator.model.entity.individuals.genome.Genotype;

public class GenomeVisualizer extends JPanel{
	Matrix projectionMatrix;
	int WIDTH=800, HEIGHT=800;
	float zfar=1000f,znear=0.1f;
	float a=(float)WIDTH/(float)HEIGHT,f=90f,q=zfar/(zfar-znear);
	float frad = (float) (1f/ (float)Math.tan(f*0.5f/180f*3.14159f));
	float offset = 8f;
	float time = 0.0f;
	Vector3D camera;
	Vector3D lookDir;
	float fYaw,fXaw;
	List<BaseSet>bases;
	float baseShift=1f;
	float rotZ=0f,rotY=0f,rotX=0f;
	
	Genotype geno;
	public GenomeVisualizer(int width, int height, Genotype geno) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		bases = new ArrayList<BaseSet>();
		for(int i=0;i<20;i++) {
			bases.add(new BaseSet(i));
		}
		
		this.setupObservers();
		
		//projectionMatrix = Util.createProjectionMatrix(zfar, znear, a, f, q, frad);
		projectionMatrix = Matrix.Matrix_MakeProjection(f, a, znear, zfar);
		
		camera = new Vector3D(0f,0f,0f);
		lookDir = new Vector3D(0f,0f,1f);
		
		this.geno = geno;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("look: "+lookDir);
		//System.out.println("camera: "+camera);
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.WIDTH, this.HEIGHT);
		
		
		
		//time+=0.1f;
		Matrix zrotation = Matrix.Matrix_MakeRotationZ(this.rotZ);
		Matrix xrotation = Matrix.Matrix_MakeRotationX(this.rotX);
		Matrix yrotation = Matrix.Matrix_MakeRotationY(this.rotY);
		
		Matrix matTrans = Matrix.translate(0f, 0f, 8f);
		
		Matrix matWorld = Matrix.identity();
		matWorld = matWorld.multiply(xrotation);
		matWorld = matWorld.multiply(yrotation);
		matWorld = matWorld.multiply(zrotation);
		matWorld = matWorld.multiply(matTrans);
		
		Vector3D vUp = new Vector3D(0f,1f,0f);
		//Vector3D vTarget = camera.add(lookDir);
		Vector3D vTarget = new Vector3D(0f,0f,1f);
		Matrix matCameraRot = Matrix.Matrix_MakeRotationY(fYaw);
		matCameraRot = matCameraRot.multiply(Matrix.Matrix_MakeRotationX(fXaw));
		lookDir = Vector3D.multiplyMatrix(vTarget, matCameraRot);
		vTarget = Vector3D.add(camera, lookDir);
		
		Matrix matCamera = Matrix.Matrix_PointAt(camera, vTarget, vUp);
		Matrix matView = Matrix.Matrix_QuickInverse(matCamera);
		
		//strands
		List<Vector3D>strand1 = Triangulate.createStrand(0f);
		List<Vector3D>strand2 = Triangulate.createStrand(3.14159f);
		
		int alpha;
		
		alpha = 200;
		g2.setStroke(new BasicStroke(5f));
		Color ploidyColors[] = new Color[]{new Color(255,0,0,alpha),new Color(0,255,0,alpha),new Color(0,0,255,alpha)};
		this.drawBases(g2, matWorld, matView, bases, strand1, strand2, ploidyColors);
		
		
		alpha = 255;
		g2.setStroke(new BasicStroke(10f));
		g2.setColor(new Color(70,70,70,alpha));
		this.drawStrands(g2, matWorld, matView, strand1);
		g2.setColor(new Color(70,70,70,alpha));
		this.drawStrands(g2, matWorld, matView, strand2);
		
	
		
		
	}
	protected void drawBases(Graphics2D g2, Matrix matWorld, Matrix matView, List<BaseSet> bases, List<Vector3D>strand1, List<Vector3D>strand2, Color ploidyColors[]) {
		
		float innerShift = (float)strand1.size()/bases.size();
		for(BaseSet baseSet:bases) {
			Vector3D s1 = strand1.get((int)(baseSet.position * innerShift));
			Vector3D s2 = strand2.get((int)(baseSet.position * innerShift));
			float ploidyShift = Math.abs((s1.x-s2.x)/(float)Constants.PLOIDY);
			
			float x1 = s1.x;
			float x2 = s2.x;
			float y1 = s1.y;
			float y2 = s2.y;
			float z1 = s1.z;
			float z2 = s2.z;


			for(float i=0f;i<Constants.PLOIDY;i+=1f) {
				
				Vector3D v1 = new Vector3D(x1,y1,z1);
				Vector3D v2 = new Vector3D(x2,y2,z2);
				Vector3D mostLeftV = v1.x>v2.x?v1:v2;
				Vector3D mostRightV = v1.x<v2.x?v1:v2;
				
				Vector3D sub = Vector3D.sub(mostLeftV, mostRightV);
				float dist = Vector3D.length(sub);
				Vector3D normal = Vector3D.normal(sub);
				ploidyShift = Math.abs(dist/(float)Constants.PLOIDY);
				
				mostRightV = Vector3D.add(mostRightV, Vector3D.mul(normal, ploidyShift*i));
				mostLeftV = Vector3D.add(mostLeftV, Vector3D.mul(normal, ploidyShift*(i-(float)Constants.PLOIDY+1f)));

				float roundf=10000f;
				mostLeftV.x = Math.round(mostLeftV.x*roundf)/roundf;
				mostRightV.x = Math.round(mostRightV.x*roundf)/roundf;
				mostLeftV.z = Math.round(mostLeftV.z*roundf)/roundf;
				mostRightV.z = Math.round(mostRightV.z*roundf)/roundf;

						
				Vector3D p1 = this.vectorProjection(matWorld, matView, mostLeftV);
				Vector3D p2 = this.vectorProjection(matWorld, matView, mostRightV);

				if(v1.x>v2.x) {
					g2.setColor(ploidyColors[Constants.PLOIDY-1-(int)i]);
				}
				else {
					g2.setColor(ploidyColors[(int)i]);
				}
				g2.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
			
			}
		}

	}
	protected void drawStrands(Graphics2D g2, Matrix matWorld, Matrix matView, List<Vector3D>points) {
		List<Vector3D>todraw = new ArrayList<Vector3D>();
		for(Vector3D tri:points) {
			todraw.add(vectorProjection(matWorld,matView,tri));
		}
		for(int i=0;i<todraw.size();i++) {
			//g2.fillOval((int)todraw.get(i).x, (int)todraw.get(i).y, 5, 5);
			if(i!=0) {
				g2.drawLine((int)todraw.get(i).x, (int)todraw.get(i).y, (int)todraw.get(i-1).x, (int)todraw.get(i-1).y);
			}
		}
	}
	private Vector3D vectorProjection(Matrix matWorld, Matrix matView, Vector3D v) {
		Vector3D triTransformed = Vector3D.multiplyMatrix(v, matWorld);
		Vector3D triViewed = Vector3D.multiplyMatrix(triTransformed, matView);
		Vector3D triProjected = Vector3D.multiplyMatrix(triViewed, projectionMatrix);
		triProjected = Vector3D.div(triProjected, triProjected.w);


		triProjected.x *= -1.0f;
		triProjected.y *= -1.0f;
		Vector3D offsetView = new Vector3D(1f,1f,0f);
		triProjected = Vector3D.add(triProjected, offsetView);
		triProjected.x *= 0.5f * (float)this.WIDTH;
		triProjected.y *= 0.5f * (float)this.HEIGHT;

		return triProjected;
	}
	private void setupObservers() {
		MouseAdapter mouseA = new MouseAdapter() {
			boolean pressed = false;
    		Point current = null;
    		@Override
			public void mouseClicked(MouseEvent e) {
    			GenomeVisualizer.this.requestFocusInWindow();
			}
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
					if(Math.abs(dx) >10)GenomeVisualizer.this.fYaw +=(float)dx/decreaseFactor;
					if(Math.abs(dy) >10)GenomeVisualizer.this.fXaw +=(float)dy/decreaseFactor;
							
					repaint();
				}
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Vector3D vForward = Vector3D.mul(GenomeVisualizer.this.lookDir, -((float)e.getWheelRotation())*0.3f);
				GenomeVisualizer.this.camera = Vector3D.add(GenomeVisualizer.this.camera, vForward);
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
					  GenomeVisualizer.this.camera.y-=0.8f;
				    break;
				  case KeyEvent.VK_W:
					  GenomeVisualizer.this.camera.y+=0.8f;
				    break;
				  case KeyEvent.VK_D:
					  GenomeVisualizer.this.camera.x-=0.8f;
				    break;
				  case KeyEvent.VK_A:
					  GenomeVisualizer.this.camera.x+=0.8f;
				    break;
				  case KeyEvent.VK_R:
					  GenomeVisualizer.this.camera.z-=0.8f;
				    break;
				  case KeyEvent.VK_F:
					  GenomeVisualizer.this.camera.z+=0.8f;
				    break;

				  case KeyEvent.VK_Z:
					  rotZ+=0.05f;
				    break;
				  case KeyEvent.VK_X:
					  rotX+=0.05f;
				    break;
				  case KeyEvent.VK_Y:
					  rotY+=0.05f;
				    break;

				  default:				    
				}
				repaint();
			}
			@Override
			public void keyReleased(KeyEvent ke) {}
			@Override
			public void keyTyped(KeyEvent ke) {}
		};
		this.addKeyListener(keyListener);
		this.setFocusable(true);
        this.requestFocusInWindow();
	}
	public void advance(float t) {
		
		this.time += t;
		this.repaint();
		SwingUtilities.invokeLater(()->{
			advance(t);
		});
	}
}
