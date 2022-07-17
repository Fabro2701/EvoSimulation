package simulator.model.map.creator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Scrollable;


/**
*
* @author fabrizioortega
*/
public class AttributesPanel extends javax.swing.JPanel {
	private Controller ctrl;
   /**
    * Creates new form TerrainPanel
    */
   public AttributesPanel() {
       initComponents();
       ctrl = new Controller();
       modificationPanel.setViewportView(ctrl.img);
   }

   public class Controller implements MouseListener,MouseMotionListener{
	   private HashMap<String,Attribute> models;
	   private Graphics2D bufferGraphics;
	   private BufferedImage bufferImage;
	   private int strokeSize;
	   private int intensity;
	   private Attribute selection;
	   private int width, height;
	   private ModificationImage img;
	   
	   private Point current;
	   public Controller() {
		   width = 800;
		   height = 800;
		   
		   strokeSize = 50;
		   intensity = 50;
		   
		   
		   bufferImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		   bufferGraphics = bufferImage.createGraphics();
		   bufferGraphics.setColor(new Color(0,0,0));
		   bufferGraphics.fillRect(0, 0, width, height);
		   
		   img = new ModificationImage(new ImageIcon(bufferImage), this);
		   //img = new ModificationImage(new ImageIcon("resources/maps/test1.jpg"), this);
		   //img.setIcon(new ImageIcon("resources/maps/elevation1test.jpg"));

		   
		   models = new HashMap<String,Attribute>();
		   models.put("Temperature", new Attribute(new Color(255,0,0,100),width, height));
		   models.put("Radiation", new Attribute(new Color(0,255,0,100),width, height));
		   selection = models.get("Temperature");
		   
		   current = new Point();
	   }
	   public void setSelection(String stringKey) {
		   //System.out.println(stringKey);
		   selection=models.get(stringKey);
	   }
	   public void setStrokeSize(int value) {
		   strokeSize = value;
	   }
	   public void setIntensity(int value) {
		   intensity = value;
	   }
	   public class Attribute{
		   private Color color;
		   private BufferedImage ownImage;
		   private Graphics2D ownGraphics;
		   public Attribute(Color c, int width, int height) {
			   color = c;
			   ownImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			   ownGraphics = ownImage.createGraphics();
		   }
		   private void paint(Point p1, Point p2, int strokeSize, int intensity, Graphics2D bufferGraphics) {
			   ownGraphics.setStroke(new BasicStroke(strokeSize));
			   ownGraphics.setColor(new Color(255,0,0,intensity));
			   ownGraphics.drawLine(p1.x,p1.y,p2.x,p2.y);

			   bufferGraphics.setStroke(new BasicStroke(strokeSize));
			   bufferGraphics.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),intensity));
			   bufferGraphics.drawLine(p1.x,p1.y,p2.x,p2.y);
		   }
	   }
	   @Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			current.setLocation(e.getPoint());
			//selection.paint(current,e.getPoint(),strokeSize,intensity,bufferGraphics);
			//img.setIcon(new ImageIcon(bufferImage));
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			selection.paint(current,e.getPoint(),strokeSize,intensity,bufferGraphics);
			img.setIcon(new ImageIcon(bufferImage));
			current.setLocation(e.getPoint());
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		
	   public class ModificationImage extends JLabel implements Scrollable{
		   public ModificationImage(ImageIcon icon, Controller ctrl) {
			   super(icon);
			   this.addMouseListener(ctrl);
			   this.addMouseMotionListener(ctrl);
			   //this.setIcon(icon);
		   }

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			// TODO Auto-generated method stub
			return false;
		}
		   
	   }
	
	
   }
   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
   private void initComponents() {

	   modificationPanel = new javax.swing.JScrollPane();
       jPanel1 = new javax.swing.JPanel();
       jComboBox1 = new javax.swing.JComboBox<>();
       jLabel1 = new javax.swing.JLabel();
       jLabel2 = new javax.swing.JLabel();
       jsStrokeSize = new javax.swing.JSlider();
       jLabel3 = new javax.swing.JLabel();
       jsIntensity = new javax.swing.JSlider();
       jScrollPane2 = new javax.swing.JScrollPane();
       jPanel2 = new javax.swing.JPanel();

       setMaximumSize(new java.awt.Dimension(1373, 682));
       setPreferredSize(new java.awt.Dimension(1373, 682));
       setSize(new java.awt.Dimension(1373, 682));

       modificationPanel.setBorder(new javax.swing.border.MatteBorder(null));
       modificationPanel.setMaximumSize(new java.awt.Dimension(600, 600));
       modificationPanel.setMinimumSize(new java.awt.Dimension(600, 600));
       modificationPanel.setPreferredSize(new java.awt.Dimension(600, 600));

       jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

       jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Temperature", "Radiation" }));
       jComboBox1.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               jComboBox1ActionPerformed(evt);
           }
       });

       jLabel1.setText("Type");

       jLabel2.setText("Stroke Size");

       jsStrokeSize.setMinimum(1);
       jsStrokeSize.addChangeListener(new javax.swing.event.ChangeListener() {
           public void stateChanged(javax.swing.event.ChangeEvent evt) {
               jsStrokeSizeStateChanged(evt);
           }
       });

       jLabel3.setText("Intensity");

       jsIntensity.setMinimum(1);
       jsIntensity.addChangeListener(new javax.swing.event.ChangeListener() {
           public void stateChanged(javax.swing.event.ChangeEvent evt) {
               jsIntensityStateChanged(evt);
           }
       });

       javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
       jPanel1.setLayout(jPanel1Layout);
       jPanel1Layout.setHorizontalGroup(
           jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(jPanel1Layout.createSequentialGroup()
               .addContainerGap()
               .addComponent(jLabel1)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGap(28, 28, 28)
               .addComponent(jLabel2)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jsStrokeSize, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jLabel3)
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addComponent(jsIntensity, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
       );
       jPanel1Layout.setVerticalGroup(
           jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
               .addContainerGap(19, Short.MAX_VALUE)
               .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                   .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                       .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                       .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                       .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                   .addComponent(jsStrokeSize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(jsIntensity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addGap(15, 15, 15))
       );

       jScrollPane2.setBorder(new javax.swing.border.MatteBorder(null));

       jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

       javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
       jPanel2.setLayout(jPanel2Layout);
       jPanel2Layout.setHorizontalGroup(
           jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGap(0, 0, Short.MAX_VALUE)
       );
       jPanel2Layout.setVerticalGroup(
           jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGap(0, 0, Short.MAX_VALUE)
       );

       javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
       this.setLayout(layout);
       layout.setHorizontalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                   .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addGroup(layout.createSequentialGroup()
                       .addGap(0, 0, Short.MAX_VALUE)
                       .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE))
                   .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
       );
       layout.setVerticalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
               .addContainerGap()
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                   .addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(jScrollPane2))
               .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                   .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
       );
   }// </editor-fold>                        

   private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
	   ctrl.setSelection((String)jComboBox1.getSelectedItem());
   }                                          

   private void jsStrokeSizeStateChanged(javax.swing.event.ChangeEvent evt) {                                          
       ctrl.setStrokeSize(jsStrokeSize.getValue());
   }                                         

   private void jsIntensityStateChanged(javax.swing.event.ChangeEvent evt) {                                             
       ctrl.setIntensity(jsIntensity.getValue());                                      
   }                                        


   // Variables declaration - do not modify                     
   private javax.swing.JComboBox<String> jComboBox1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JScrollPane modificationPanel;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JSlider jsIntensity;
   private javax.swing.JSlider jsStrokeSize;
   // End of variables declaration                   
}

