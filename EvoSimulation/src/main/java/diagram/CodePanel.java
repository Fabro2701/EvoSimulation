package diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;


public class CodePanel extends JTextPane{
	public Style redStyle,blueStyle,greenStyle;
	List<CodeStyle> styles;

	public CodePanel() {

		StyleContext sc = StyleContext.getDefaultStyleContext();

		redStyle = sc.addStyle("red", null);
		blueStyle = sc.addStyle("blue", null);
		greenStyle = sc.addStyle("blue", null);
		StyleConstants.setForeground(redStyle, new Color(148, 25, 36));
		StyleConstants.setForeground(blueStyle, new Color(3, 95, 185));
		StyleConstants.setForeground(greenStyle, new Color(2, 122, 19));
		
	
		 this.setPreferredSize(new Dimension(800,500));
		 this.setMinimumSize(new Dimension(800,500));
		 
		 this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		 
	
		 styles = new ArrayList<>();
		 styles.add(new CodeStyle("[\\s]if\\(","if",redStyle));
		 styles.add(new CodeStyle("this|let|new",redStyle));
	
		 styles.add(new CodeStyle("\"[^\"]*\"",blueStyle));
		 styles.add(new CodeStyle("(global|init|updates|interactions) :=",greenStyle));
		 styles.add(new CodeStyle("\\n[\\d\\w]*",greenStyle));
		 
	
	}
	public void insertString(String s) {
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), s, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public void insertString(String s, Style style) {
		Document doc = this.getDocument();
		try {
			doc.insertString(doc.getLength(), s, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public void clear() {
		this.setText("");
	}
	public void stylize() throws BadLocationException {
		
		String s = this.getText();
		this.setText("");
		String lines[] = s.split("\n");
		StringBuilder sb = new StringBuilder();
		
		int tab=0;
		for(String line:lines) {
			if(line.contains("}"))tab--;
			for(int i=0;i<tab;i++)sb.append("      ");
			sb.append(line).append('\n');
			if(line.contains("{"))tab++;
		}
		s = sb.toString();
		insertString(s);
		

		for(CodeStyle style:this.styles) {
			style.apply();
		}

		this.setCaretPosition(0);
	}
	private class CodeStyle{
		Pattern pattern;
		String realString; 
		Style style;
		public CodeStyle(String regex, String realString, Style style) {
			this.pattern = Pattern.compile(regex);
			this.realString = realString;
			this.style = style;
		}
		public CodeStyle(String regex, Style style) {
			this(regex,null,style);
		}
		public void apply() throws BadLocationException {
			Document doc = getDocument();
			Matcher m = pattern.matcher(doc.getText(0, doc.getLength()));
			
			while(m.find()) {
				String sm = m.group();
				if(realString==null) {
					CodePanel.this.getStyledDocument().setCharacterAttributes(m.start(), sm.length(), style, false);
					//((AbstractDocument)doc).replace(m.start(), sm.length(), sm, style);
					//doc.remove(m.start(), sm.length());
					//doc.insertString(m.start(), sm, style);
				}
				else {
					CodePanel.this.getStyledDocument().setCharacterAttributes(m.start()+sm.indexOf(realString), 
							realString.length(), style, false);
					//((AbstractDocument)doc).replace(m.start()+sm.indexOf(realString), 
					//								realString.length(), 
					//								realString, style);
					//doc.remove(m.start()+sm.indexOf(realString), realString.length());
					//doc.insertString(m.start()+sm.indexOf(realString), realString, style);
				}
			}
		}
	}
}
