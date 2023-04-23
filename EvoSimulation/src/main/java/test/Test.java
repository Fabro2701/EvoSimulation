package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


class Test extends JPanel{
	
	public Test() {
		this.setPreferredSize(new Dimension(300,100));
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		float[][]cols = new float[][] {{1,1,0},{1,0,0}};
		for(int i=0;i<=300;i++) {
			System.out.println(i);
			Color c = util.Util.getGradient(cols, i/300f,1);
			System.out.println(c);
			g2.setColor(c);
			//g2.setColor(new Color(255,0,0,255*i/300));
			g2.fillRect(i, 0, 1, 100);
		}
	}
	
    public static void main(String args[]) {
    	
    	SwingUtilities.invokeLater(()->{
    		JFrame f = new JFrame();
    		f.setContentPane(new Test());
    		f.pack();
    		f.setLocationRelativeTo(null);
    		f.setVisible(true);
    	});
    }
  }
 