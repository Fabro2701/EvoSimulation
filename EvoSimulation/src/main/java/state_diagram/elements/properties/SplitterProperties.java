package state_diagram.elements.properties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import state_diagram.elements.Splitter;
import state_diagram.elements.Transition;

public class SplitterProperties extends ElementProperties {
	Splitter e;
	List<JTextField>tFields;
	JRadioButton stochastic, condition;
	JPanel proppanel;
	public SplitterProperties(JScrollPane father, Splitter e) {
		super(father);
		this.e = e;
		this.tFields = new ArrayList<>();

		this.setLayout(new BorderLayout());
		
		stochastic = new JRadioButton("stochastic");
		stochastic.addActionListener(ae->{
			e.setType(Splitter.SPLITTER_TYPE.STOCHASTIC);
			e.revalidate();
			});
		condition = new JRadioButton("condition");
		condition.addActionListener(ae->{
			e.setType(Splitter.SPLITTER_TYPE.COND);
			e.revalidate();
			});
		ButtonGroup bg =	new ButtonGroup();
		bg.add(stochastic);
		bg.add(condition);
		JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(1, 0));
        radioPanel.add(stochastic);
        radioPanel.add(condition);
        switch(e.getType()) {
		case STOCHASTIC:
			bg.setSelected(stochastic.getModel(), true);
			break;
		case COND:
			bg.setSelected(condition.getModel(), true);
			break;
		default:
			break;
        	
        }
		
		
		 switch(e.getType()) {
			case STOCHASTIC:
				stochPanel();
				break;
			case COND:
				condPanel();
				break;
			default:
				break;
	        	
	        }
		

		JButton saveb = new JButton("save");
		saveb.addActionListener(a->{
			save();
			e.getDiagram().repaint();
		});

		this.add(radioPanel, BorderLayout.PAGE_START);
		this.add(proppanel, BorderLayout.CENTER);
		this.add(saveb, BorderLayout.PAGE_END);

		
	}


	private void condPanel() {
		proppanel = new JPanel();
	}
	private void stochPanel() {
		proppanel = new JPanel();
		proppanel.setLayout(new GridLayout(0,2));
		
		int i=0;
		for(Transition t:e.getFromTs()) {
			JLabel idLabel = new JLabel("t "+t.ID+":");
			JTextField f = new JTextField(String.valueOf(e.getProbs().get(i)));
			tFields.add(f);
			proppanel.add(idLabel);proppanel.add(f);
			i++;
		}
	}
	@Override
	protected void save() {
		for(int i=0;i<e.getFromTs().size();i++) {
			e.getProbs().set(i, Double.valueOf(this.tFields.get(i).getText()));
		}
	}
	@Override
	protected void _load() {
		
	}

}
