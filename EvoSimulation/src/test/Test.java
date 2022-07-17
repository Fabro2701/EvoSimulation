package test;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

public class Test extends JFrame {  
	  
	public Test() {  
		  
		
		Img i1 = new Img("elevation1test");
		Img i2 = new Img("test1");

		JScrollPane pictureScrollPane = new JScrollPane();
		pictureScrollPane.setViewportView(i1);
		pictureScrollPane.setViewportView(i2);
		//pictureScrollPane.remove(i1);
		//pictureScrollPane.add(i1);
        pictureScrollPane.setPreferredSize(new Dimension(300, 250));
		
        this.add(pictureScrollPane);
        
		this.setPreferredSize(new Dimension(300,300));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}  
	public class Img extends JLabel implements Scrollable,MouseListener{
		public ImageIcon img;
		public Img(String filename) {
			super(new ImageIcon("resources/maps/"+filename+".jpg"));
			img = (ImageIcon) this.getIcon();
			this.addMouseListener(this);
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
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(img.toString()+e.getPoint());
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	  }
	  
	  public static void main(String[] args) {  
	    SwingUtilities.invokeLater(() -> {  
	    	new Test().setVisible(true);
	    });  
	  }  
	}  