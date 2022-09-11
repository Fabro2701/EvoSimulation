package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class IsoTest extends JFrame {
	IsoTest() {
		JFrame aFrame = new JFrame("Isometric 3D Test");
		int step = 50;
		IsoPanel isoPanel = new IsoPanel();
		JButton mode0Button = new JButton("Mode Left");
		mode0Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.iso3D.mode = 0;
				isoPanel.repaint();
			}
		});
		isoPanel.add(mode0Button);
		JButton mode1Button = new JButton("Mode Center");
		mode1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.iso3D.mode = 1;
				isoPanel.repaint();
			}
		});
		isoPanel.add(mode1Button);
		JButton mode2Button = new JButton("Mode Right");
		mode2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.iso3D.mode = 2;
				isoPanel.repaint();
			}
		});
		isoPanel.add(mode2Button);

		JButton leftButton = new JButton("Left");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.originX -= step;
				isoPanel.repaint();
			}
		});
		isoPanel.add(leftButton);
		JButton rightButton = new JButton("Right");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.originX += step;
				isoPanel.repaint();
			}
		});
		isoPanel.add(rightButton);
		JButton upButton = new JButton("Up");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.originY -= step;
				isoPanel.repaint();
			}
		});
		isoPanel.add(upButton);
		JButton downButton = new JButton("Down");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.originY += step;
				isoPanel.repaint();
			}
		});
		isoPanel.add(downButton);
		JButton yIncButton = new JButton("Y+");
		yIncButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.ySkew -= 0.1f;
				if (isoPanel.ySkew == 0)
					isoPanel.ySkew = -0.1f;
				isoPanel.repaint();
			}
		});
		isoPanel.add(yIncButton);
		JButton yDecButton = new JButton("Y-");
		yDecButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.ySkew += 0.1f;
				if (isoPanel.ySkew == 0)
					isoPanel.ySkew = 0.1f;
				isoPanel.repaint();
			}
		});
		isoPanel.add(yDecButton);

		JButton yInc2DButton = new JButton("2D Y+");
		yInc2DButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.iso3D.ySkew -= .1f;
				if (isoPanel.ySkew == 0.0f)
					isoPanel.iso3D.ySkew = -0.1f;
				isoPanel.repaint();
			}
		});
		isoPanel.add(yInc2DButton);
		JButton yDec2DButton = new JButton("2D Y-");
		yDec2DButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.iso3D.ySkew += .1f;
				if (isoPanel.ySkew == 0.0f)
					isoPanel.iso3D.ySkew = 0.1f;
				isoPanel.repaint();
			}
		});
		isoPanel.add(yDec2DButton);
		JButton zoomInButton = new JButton("Zoom+");
		zoomInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.surface.xSquareSize -= 1;
				isoPanel.surface.ySquareSize -= 1;
				if (isoPanel.surface.xSquareSize <= 0)
					isoPanel.surface.xSquareSize = 1;
				if (isoPanel.surface.ySquareSize <= 0)
					isoPanel.surface.ySquareSize = 1;
				isoPanel.repaint();
			}
		});
		isoPanel.add(zoomInButton);
		JButton zoomOutButton = new JButton("Zoom-");
		zoomOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isoPanel.surface.xSquareSize += 1;
				isoPanel.surface.ySquareSize += 1;
				isoPanel.repaint();
			}
		});
		isoPanel.add(zoomOutButton);
		aFrame.add(isoPanel);
		aFrame.setSize(1000, 800);
		aFrame.setDefaultCloseOperation(aFrame.EXIT_ON_CLOSE);
		aFrame.show();
	}

	public static void main(String args[]) {
		new IsoTest();
	}
}
