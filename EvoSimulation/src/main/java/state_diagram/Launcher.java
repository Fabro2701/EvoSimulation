package state_diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Launcher extends JFrame{
	public static void main(String args[]) {;
		SwingUtilities.invokeLater(()->{
			Launcher launcher = new Launcher();
			
			
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			launcher.setContentPane(panel);
			
			
			
			Diagram diagram = new Diagram();
			diagram.setPreferredSize(new Dimension(800,600));
			panel.add(diagram, BorderLayout.CENTER);
			
			
			ToolBar toolBar = new ToolBar(diagram);
			toolBar.setPreferredSize(new Dimension(800,70));
			toolBar.setBorder(BorderFactory.createMatteBorder(
                    1, 1, 1, 1, Color.gray));
			panel.add(toolBar, BorderLayout.PAGE_START);
			
			JPanel buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new FlowLayout());
			JButton saveButton = new JButton("SAVE");
			saveButton.addActionListener(ae->diagram.save());
			JButton loadButton = new JButton("LOAD");
			loadButton.addActionListener(ae->{
				try {
					diagram.load();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
			buttonsPanel.add(saveButton);
			buttonsPanel.add(loadButton);
			//panel.add(buttonsPanel, BorderLayout.PAGE_END);
			
			

			JScrollPane props = new JScrollPane();
			props.setPreferredSize(new Dimension(800,200));
			props.setBorder(BorderFactory.createMatteBorder(
                    1, 1, 1, 1, Color.gray));
			diagram.setPropertiesPanel(props);
			panel.add(props, BorderLayout.PAGE_END);
			
			diagram.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.isControlDown()) {
						if (e.getKeyCode() == KeyEvent.VK_S) {
					      diagram.save();
						}
						else if (e.getKeyCode() == KeyEvent.VK_O) {
							try {
								diagram.load();
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchMethodException | SecurityException| ClassNotFoundException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				@Override public void keyReleased(KeyEvent e) {}
				@Override public void keyTyped(KeyEvent e) {}
			});
			
			launcher.setLocation(500, 100);
			launcher.pack();
			launcher.setDefaultCloseOperation(EXIT_ON_CLOSE);
			launcher.setVisible(true);
			
		});
	}
}
