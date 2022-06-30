package simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LauncherGUI extends JFrame{
	public LauncherGUI() {
		super("Launcher");
		initGUI();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		mainPanel.setMaximumSize(new Dimension(600, 600));
		mainPanel.setMinimumSize(new Dimension(600, 600));
		mainPanel.setPreferredSize(new Dimension(600, 600));
		setContentPane(mainPanel);
		
		//config panel
		JPanel configPanel = new JPanel();
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
		
		//map config
		JPanel mapPanel = new JPanel();
		mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.X_AXIS));
		JButton mapButton = new JButton("Select Map");
		JLabel mapLabel = new JLabel("default map");
		mapLabel.setPreferredSize(new Dimension(100,10));
		mapPanel.add(mapLabel);
		mapPanel.add(Box.createRigidArea(new Dimension(5,0)));
		mapPanel.add(mapButton);
		
		//
		
		
		configPanel.add(mapPanel);
		mainPanel.add(configPanel,BorderLayout.WEST);
	}
	public static void main(String args[]){
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new LauncherGUI();
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
