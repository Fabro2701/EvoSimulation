package statistics.visualizers;

import javax.swing.JLabel;

import statistics.StatsData;

public class TextVisualizer extends StatsVisualizer{
	public TextVisualizer(StatsData data) {
		this.panel = data.getText(); 
	}
}
