package genome_editing.model.editor.block;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class DrawElement {
	public static abstract class Shape{
		protected Color color;
		public float priority;
		public abstract void draw(Graphics2D g2);
		public Shape(Color color) {
			this.color = color;
		}
		public abstract boolean contains(Point point);
	}
	public static class Rectangle extends Shape{
		float x, y, width, height;
		Color color;
		public Rectangle(float x, float y, float width, float height, Color color) {
			super(color);
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.color = color;
			priority = 0f;
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fillRect((int)x, (int)y, (int)width, (int)height);
			//g2.fillRoundRect((int)x, (int)y, (int)width, (int)height, 10,10);
			
			g2.setColor(Color.black);
			//g2.setStroke(new BasicStroke(4));
			g2.drawRect((int)x, (int)y, (int)width, (int)height);
		}
		/**
		 * W. Randolph Franklin algorithm 
		 * https://web.archive.org/web/20161108113341/https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
		 */
		@Override
		public boolean contains(Point point) {
			float pointsx[] = new float[] {this.x, this.x+width, this.x+width, this.x};
			float pointsy[] = new float[] {this.y, this.y, this.y+height, this.y+height};
			int i;
			int j;
			boolean result = false;
			for (i = 0, j = pointsx.length - 1; i < pointsx.length; j = i++) {
			  if ((pointsy[i] > point.y) != (pointsy[j] > point.y) &&
			      (point.x < (pointsx[j] - pointsx[i]) * (point.y - pointsy[i]) / (pointsy[j]-pointsy[i]) + pointsx[i])) {
				  result = !result;
			   }
			}
			return result;
		}
	}
	public static class Triangle extends Shape{
		float x1,x2,x3,y1,y2,y3;
		Color color;
		public Triangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
			super(color);
			this.x1 = x1;
			this.x2 = x2;
			this.x3 = x3;
			this.y1 = y1;
			this.y2 = y2;
			this.y3 = y3;
			this.color = color;
			priority = 1f + (color==Block.white?0f:0.1f);
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fillPolygon(new int[] {(int)x1,(int)x2,(int)x3}, new int[] {(int)y1,(int)y2,(int)y3}, 3);

			//g2.setColor(Color.black);
			//g2.drawPolygon(new int[] {(int)x1,(int)x2,(int)x3}, new int[] {(int)y1,(int)y2,(int)y3}, 3);
		}
		@Override
		public boolean contains(Point point) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	public static class StringShape extends Shape{
		String s;
		float x, y;
		Color color;
		public StringShape(String s, float x, float y, Color color) {
			super(color);
			this.x = x;
			this.y = y;
			this.s = s;
			this.color = color;
			priority = 2f;
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.drawString(s, x, y);
		}
		@Override
		public boolean contains(Point point) {
			return false;
		}
	}
}
