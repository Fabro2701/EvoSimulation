package test;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class B extends JFrame {
	public B() {
		JPanel mainpanel = new JPanel();
		this.setContentPane(mainpanel);
		
		JButton b1 = new JButton("1");
		JButton b2 = new JButton("2");
		b1.addActionListener((evt)-> {

			mainpanel.add(b2);
			B.this.pack();
			B.this.repaint();
		});
		
		
		mainpanel.add(b1);
		//mainpanel.add(b2);
		this.pack();
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(() -> {
			new B().setVisible(true);
		});
	}
}
