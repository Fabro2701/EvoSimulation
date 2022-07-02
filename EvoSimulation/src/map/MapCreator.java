package map;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * MapCreator gives the posibility to create a map with 3 features (temperature, radiation, food) that correspond
 * with the RGB values of a image
 * The image can be saved into a .jpg and load one from the same format
 * You can set the canvas size, stroke size and stroke intensity 
 * 
 * @author Fabrizio Ortega
 *
 */
public class MapCreator extends JFrame implements ActionListener{
	
	//3 canvas, one per each map attribute
	Canvas temperatureCanvas;
	Canvas radiationCanvas;
	Canvas foodCanvas;
	
	public MapCreator() {
		super("Map Creator");
		initGUI();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	/**
	 * init and set the listeners of buttons, spinners, and menus
	 */
	public void initGUI() {
		//main panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setMaximumSize(new Dimension(1500, 1500));
		mainPanel.setMinimumSize(new Dimension(500, 500));
		mainPanel.setPreferredSize(new Dimension(700, 700));
		setContentPane(mainPanel);
		
		//menu
		JMenuBar jmb = new JMenuBar();
		
		JMenu jmFile = new JMenu("File");
		JMenuItem jmiOpen = new JMenuItem("Open");
		jmiOpen.addActionListener((ae) ->openMapDialog());
		
		JMenuItem jmiSave = new JMenuItem("Save");
		jmiSave.addActionListener((ae) ->saveMapDialog());
		
		jmFile.add(jmiOpen);
		jmFile.add(jmiSave);
		jmb.add(jmFile);
		
		this.setJMenuBar(jmb);
		
		
		//canvas
		temperatureCanvas = new Canvas(500,500,Color.RED);
		mainPanel.add(temperatureCanvas, BorderLayout.CENTER);
		temperatureCanvas.activate();//temperature is the default canvas
		
		radiationCanvas = new Canvas(500,500,Color.GREEN);
		mainPanel.add(radiationCanvas, BorderLayout.CENTER);
		
		foodCanvas = new Canvas(500,500,Color.BLUE);
		mainPanel.add(foodCanvas, BorderLayout.CENTER);
		
		
		//switch canvas control 
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.LINE_AXIS));
		
		ButtonGroup bg = new ButtonGroup();
		JRadioButton jrTemperature = new JRadioButton("Temperature",true);jrTemperature.addActionListener(this);
		JRadioButton jrRadiation = new JRadioButton("Radiation");jrRadiation.addActionListener(this);
		JRadioButton jrFood = new JRadioButton("Food");jrFood.addActionListener(this);
		bg.add(jrTemperature);
		bg.add(jrRadiation);
		bg.add(jrFood);
		boxPanel.add(jrTemperature);
		boxPanel.add(jrRadiation);
		boxPanel.add(jrFood);
		mainPanel.add(boxPanel, BorderLayout.SOUTH);
		
		
		//Spinner Stroke
		JSpinner jsStroke = new JSpinner(new SpinnerNumberModel(10,1,100,1));
		jsStroke.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				temperatureCanvas.g2d.setStroke(new BasicStroke((int)((JSpinner)e.getSource()).getValue()));
				radiationCanvas.g2d.setStroke(new BasicStroke((int)((JSpinner)e.getSource()).getValue()));
				foodCanvas.g2d.setStroke(new BasicStroke((int)((JSpinner)e.getSource()).getValue()));
			}
		});
		jsStroke.setMaximumSize(new Dimension(50, 30));
		jsStroke.setMinimumSize(new Dimension(50, 30));
		jsStroke.setPreferredSize(new Dimension(50, 30));
		
		JLabel jlStroke = new JLabel("Stroke Size");
		boxPanel.add(new JSeparator(SwingConstants.VERTICAL));
		boxPanel.add(jlStroke);
		boxPanel.add(jsStroke);
		
		
		//Intensity Spinner
		JSpinner jsIntensity = new JSpinner(new SpinnerNumberModel(100,1,100,1));
		jsIntensity.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				temperatureCanvas.setIntensity((int)((JSpinner)e.getSource()).getValue());
				radiationCanvas.setIntensity((int)((JSpinner)e.getSource()).getValue());
				foodCanvas.setIntensity((int)((JSpinner)e.getSource()).getValue());
			}
		});
		jsIntensity.setMaximumSize(new Dimension(50, 30));
		jsIntensity.setMinimumSize(new Dimension(50, 30));
		jsIntensity.setPreferredSize(new Dimension(50, 30));
		
		JLabel jlIntensity = new JLabel(" Intensity");
		boxPanel.add(jlIntensity);
		boxPanel.add(jsIntensity);
		
		
		//Canvas Size Spinner
		JSpinner jsSize = new JSpinner(new SpinnerNumberModel(500,100,500,10));
		jsSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				temperatureCanvas.changeSize((int)((JSpinner)e.getSource()).getValue());
				radiationCanvas.changeSize((int)((JSpinner)e.getSource()).getValue());
				foodCanvas.changeSize((int)((JSpinner)e.getSource()).getValue());
			}
		});
		jsSize.setMaximumSize(new Dimension(50, 30));
		jsSize.setMinimumSize(new Dimension(50, 30));
		jsSize.setPreferredSize(new Dimension(50, 30));
		
		JLabel jlSize = new JLabel(" Canvas Size");
		boxPanel.add(jlSize);
		boxPanel.add(jsSize);
		

		
	}
	/**
	 * This jframe is the listener of the canvas switchers
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		temperatureCanvas.deactivate();
		radiationCanvas.deactivate();
		foodCanvas.deactivate();
		if(e.getActionCommand().equals("Temperature"))temperatureCanvas.activate();
		if(e.getActionCommand().equals("Radiation"))radiationCanvas.activate();
		if(e.getActionCommand().equals("Food"))foodCanvas.activate();
		repaint();
	}
	/**
	 * Save Dialog invokes a JDialog with the possibility to set a custon filename in resources/map/
	 * the save procedure is delegated to saveMap
	 */
	public void saveMapDialog() {
		JDialog jd = new JDialog(this);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setMaximumSize(new Dimension(100, 50));
		mainPanel.setMinimumSize(new Dimension(100, 50));
		mainPanel.setPreferredSize(new Dimension(100, 50));
		jd.setContentPane(mainPanel);
		final JTextField jtxt = new JTextField("filename");
		JButton jbSave = new JButton("Save");
		jbSave.addActionListener((ae) -> {saveMap(jtxt.getText());jd.dispose();});
		JButton jbCancel = new JButton("Cancel");
		jbCancel.addActionListener((ae) -> jd.dispose());
		mainPanel.add(jtxt,BorderLayout.CENTER);
		
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.LINE_AXIS));
		boxPanel.add(jbSave);
		boxPanel.add(jbCancel);
		mainPanel.add(boxPanel,BorderLayout.SOUTH);
		
		
		jd.pack();
		jd.setLocationRelativeTo(null);
		jd.setVisible(true);
	}
	/**
	 * Save the final canvas combinig the 3 by suming their values
	 * @param filepath resources/maps/"+filepath+".jpg
	 */
	public void saveMap(String filepath){
		BufferedImage nImage= new BufferedImage(temperatureCanvas.image.getWidth(),temperatureCanvas.image.getHeight(),BufferedImage.TYPE_INT_RGB);
		//construct img
		for (int y = 0; y < nImage.getHeight(); y++) {
	         for (int x = 0; x < nImage.getWidth(); x++) {
	            
	            int pixel1 = temperatureCanvas.image.getRGB(x,y);
	            int pixel2 = radiationCanvas.image.getRGB(x,y);
	            int pixel3 = foodCanvas.image.getRGB(x,y);
	
	            Color color1 = new Color(pixel1, true);
	            Color color2 = new Color(pixel2, true);
	            Color color3 = new Color(pixel3, true);
	            
	            //the final color is the sum of the 3
	            Color nColor = new Color(color1.getRed()+color2.getRed()+color3.getRed(),
	            						 color1.getGreen()+color2.getGreen()+color3.getGreen(),
	            						 color1.getBlue()+color2.getBlue()+color3.getBlue());
	            
	            nImage.setRGB(x, y, nColor.getRGB());
	         }
		}
		
		File outputfile = new File("resources/maps/"+filepath+".jpg");
		try {
			ImageIO.write(nImage, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Invoke a JFileChooser at resources/maps/ and set new images to each canvas by the setNewCanvas method
	 */
	public void openMapDialog() {
		JFileChooser jfc = new JFileChooser("resources/maps/");
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			//System.out.println(selectedFile.getAbsolutePath());
			try {
				BufferedImage image = ImageIO.read(selectedFile);
				temperatureCanvas.setNewCanvas(image, Color.RED);
				radiationCanvas.setNewCanvas(image, Color.GREEN);
				foodCanvas.setNewCanvas(image, Color.BLUE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	/**
	 * Canvas is a JComponent where you can visualize and draw lines with the color and stroke size given
	 * the image size is also changeable
	 * @author Fabrizio Ortega
	 *
	 */
	private class Canvas extends JComponent{
		private boolean active;//only one Canvas can be active (drawable and visible)
		private BufferedImage image;
		private Graphics2D g2d;
		private MouseAdapter mouseHandler;
		private Color lastColor;//each Canvas has a RGB color
		
		/**
		 * Canvas basic constructor with defined size and unique color
		 * @param height
		 * @param width
		 * @param c color
		 */
		public Canvas(int height, int width, Color c) {
			setSize(height, width);
			active = false;//by default
			image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			g2d = image.createGraphics();
			g2d.setStroke(new BasicStroke(10));//by default
			g2d.setColor(c);
			lastColor=c;
			
			mouseHandler = new MouseAdapter() {
				private Point current = new Point();
				@Override
				public void mousePressed(MouseEvent e) {
					current.setLocation(e.getPoint());
				}
				@Override
				public void mouseDragged(MouseEvent e) {
					g2d.drawLine(current.x,current.y,e.getX(),e.getY());
					current.setLocation(e.getPoint());
					repaint();
				}
			};
			//the listener are added and removed by the methods activate and deactivate
			//this.addMouseListener(mouseHandler);
			//this.addMouseMotionListener(mouseHandler);
		}
		/**
		 * Set a new image applying a mask to the input image
		 * where mask is an array of zeros with a one in the position of the unique RGB value of the canvas
		 * @param inImage
		 * @param c
		 * @param active
		 */
		public void setNewCanvas(BufferedImage inImage, Color c) {
			setSize(inImage.getHeight(), inImage.getWidth());
			
			//the mask has a one in the position of the attribute-RGB
			int mask[] = {c.getRed()>0?1:0,c.getGreen()>0?1:0,c.getBlue()>0?1:0};
			image = new BufferedImage(inImage.getHeight(),inImage.getWidth(),BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < image.getHeight(); y++) {
		         for (int x = 0; x < image.getWidth(); x++) {         
		            int pixel = inImage.getRGB(x,y);
		            Color color = new Color(pixel, true);
		            //multiply by the mask
		            Color nColor = new Color(color.getRed()*mask[0],color.getGreen()*mask[1],color.getBlue()*mask[2]);
		            image.setRGB(x, y, nColor.getRGB());
		         }
			}
			g2d = (Graphics2D)image.getGraphics();
			g2d.setStroke(new BasicStroke(10));
			g2d.setColor(c);
			lastColor=c;
			repaint();
		}
		/**
		 * Change the intensity of the stroke
		 * @param intensity (1-100)
		 */
		public void setIntensity(int intensity) {
			Color oldColor = g2d.getColor();
			Color nColor = new Color(oldColor.getRed()>0?intensity*255/100:0,oldColor.getGreen()>0?intensity*255/100:0,oldColor.getBlue()>0?intensity*255/100:0);
			g2d.setColor(nColor);
		}
		/**
		 * Change the size of the canvas
		 * @param size
		 */
		public void changeSize(int size) {
			image = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
			g2d = image.createGraphics();
			g2d.setStroke(new BasicStroke(10));
			g2d.setColor(lastColor);//we need lastColor because we reassigned image
			repaint();
		}
		/**
		 * Activate the listeners and set active to true
		 */
		public void activate() {
			this.addMouseListener(mouseHandler);
			this.addMouseMotionListener(mouseHandler);
			active = true;
		}
		/**
		 * Deactivate the listeners and set active to false
		 */
		public void deactivate() {
			this.removeMouseListener(mouseHandler);
			this.removeMouseMotionListener(mouseHandler);
			active = false;
		}
		/**
		 * Only paint if active==true
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(active)g.drawImage(image,0,0,null);
		}
	}
	public static void main(String args[]){
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new MapCreator();
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
