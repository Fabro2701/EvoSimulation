package test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.Constants.ACTION;

public class B<T> extends JFrame {
	String s;
	T a;
	public B(String s) {
		this.s=s;
	}
	public B() {
		JPanel mainpanel = new JPanel();
		this.setContentPane(mainpanel);
		String s = "asd";
		JLabel b1 = new JLabel(s);
		s = "erer";
		b1.repaint();
		
		
		mainpanel.add(b1);
		//mainpanel.add(b2);
		this.pack();
	}
	public String toString()
	{
		return s;
	}
	public static void main(String args[]) {
		
		//System.out.println(new Color(new Color(123,255,200,100).getRGB()&2147483647));
		System.out.println(Integer.MAX_VALUE>>1);
		B<ACTION> b1 = new B("a1");Object o=null;
		B b2 = b1;
		B b3 = b2;
		System.out.println(b3);
		B b4 = new B("a4");
		b2.s="a5";
		System.out.println(b3);
		System.out.println(b1);
		/*int l = 5;
		int left = 0;
		int right = 0;
		int up = 0;
		int down = 0;
		for(int y=0;y<l;y++) {//y
			for(int x=0;x<l;x++) {//x
				if(y>=x && y+x>=l-1) {
					down++;
					System.out.print("b");//down 0, 1
				}
				if(y>=x && y+x<l) {
					left++;
					System.out.print("c");//left -1, 0
				}
				if(y<=x && y+x>=l-1) {
					right++;
					System.out.print("a");//right 1, 0
				}
				if(y<=x && y+x<l) {
					up++;
					System.out.print("d");//up	  0, -1
				}
			}
			System.out.println();
		}
		System.out.println("down: "+down);
		System.out.println("left: "+left);
		System.out.println("right: "+right);
		System.out.println("up: "+up);
		SwingUtilities.invokeLater(() -> {
			new B().setVisible(true);
		});*/
	}
}
