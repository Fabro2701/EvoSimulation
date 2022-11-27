package simulator.view;

import java.awt.Component;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import simulator.control.ConstantsController;

public class ConstantsViewer extends JFrame{
	MyPanel panel;
	JScrollPane spane;
	ConstantsController ctrl;
	public ConstantsViewer() {
		this.setSize(700, 400);
		//JPanel cpanel = new JPanel();
		//cpanel.setLayout(null);
        panel = new MyPanel();
		spane = new JScrollPane(panel);

       // panel.setSize(910, 410); 
        //panel.setBackground(Color.blue);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.getContentPane().add(spane);
        //this.setContentPane(cpanel);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	public ConstantsViewer(ConstantsController ctrl) {
		this();
		ctrl.addObserver(this);
		this.ctrl = ctrl;
	}
	class MyPanel extends JPanel{
		@Override 
		public void paintComponent(Graphics g){
			super.paintComponent(g);
		}
		public void setComps(Map<String, Object> vars) {
			//System.out.println("printing "+vars.size());
			panel.removeAll();
			for(String key:vars.keySet()) {
				Object ob = vars.get(key);
				JComponent comp = new JPanel();
				comp.setLayout(new BoxLayout(comp, BoxLayout.X_AXIS));
				comp.setAlignmentX(Component.LEFT_ALIGNMENT);
				comp.add(new JLabel(key));
				
				double value = ((Number)ob).doubleValue();
				SpinnerNumberModel model = new SpinnerNumberModel(value, -10000.0, 10000.0, 0.001); 
				JSpinner spinner = new JSpinner(model);
				JSpinner.NumberEditor editor = (JSpinner.NumberEditor)spinner.getEditor();
		        DecimalFormat format = editor.getFormat();
		        //format.setMinimumFractionDigits(2);
		        format.setMaximumFractionDigits(10);
		        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
				spinner.addChangeListener(e->ctrl.update(key, spinner.getValue()));
				comp.add(spinner);
				comp.add(new JLabel(String.valueOf(ob)));
				panel.add(comp);
			}
			this.revalidate();
			repaint();
		}
	}
	public void repaint(Map<String, Object> vars) {
		panel.setComps(vars);
	}
	public static void main(String args[]) {
		new ConstantsViewer();
	}
}
