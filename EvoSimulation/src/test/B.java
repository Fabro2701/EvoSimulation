package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class B extends JFrame{
	public B() {
		
		JPanel main = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.green);
				g.fillRect(0, 0, 300, 300);
			}
		};
		main.setMinimumSize(new Dimension(300,300));
		main.setPreferredSize(new Dimension(300,300));
		JPanel op1 = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.blue);
				g.fillRect(0, 0, 300, 300);
			}
		};
		op1.setMinimumSize(new Dimension(300,300));
		op1.setPreferredSize(new Dimension(300,300));
		JPanel op2 = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.red);
				g.fillRect(0, 0, 300, 300);
			}
		};
		op2.setMinimumSize(new Dimension(100,100));
		op2.setPreferredSize(new Dimension(100,100));
		
		this.setContentPane(main);
		
		main.add(op1);op1.add(op2);
		//main.add(op1);
		
		this.setMinimumSize(new Dimension(300,300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//op1=op2;
		
		//op1.repaint();
		//main=op1;
		
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(()->{
			new B().setVisible(true);
		});
	}
}
