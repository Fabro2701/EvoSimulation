package test;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class IsoPanel extends JPanel {
	public class Surface3D {
		int xSquareSize = 5, ySquareSize = 5;
		int xGridSize, yGridSize;
		int[][] surfaceElevations = null;
		int[][] surfaceElevationsAverage = null;

		public int getElevation(int x, int y) {
			return surfaceElevations[y][x];
		}

		public void setElevation(int x, int y, int elevation) {
			surfaceElevations[y][x] = elevation;
		}

		public int randomElevation(int minHeight, int maxHeight) {
			return (int) (Math.random() * (maxHeight - minHeight) + minHeight);
		}

		public int getElevationAvg(int x, int y) {
			return surfaceElevationsAverage[y][x];
		}

		public void setElevationAvg(int x, int y, int elevation) {
			surfaceElevationsAverage[y][x] = elevation;
		}

		public int average(int x, int y) {
			int total = getElevation(x - 1, y - 1);
			total += getElevation(x, y - 1);
			total += getElevation(x + 1, y - 1);
			total += getElevation(x - 1, y);
			total += getElevation(x, y);
			total += getElevation(x + 1, y);
			total += getElevation(x - 1, y + 1);
			total += getElevation(x, y + 1);
			total += getElevation(x + 1, y + 1);
			return (int) total / 9;
		}

		Surface3D(int xGridSize, int yGridSize, int nRandomHeights, int minHeight, int maxHeight) {
			this.xGridSize = xGridSize;
			this.yGridSize = yGridSize;
			surfaceElevations = new int[xGridSize][yGridSize];
			int randomX, randomY = 0;
			for (int i = 0; i < nRandomHeights; i++) {
				randomX = (int) (Math.random() * (xGridSize - 2) + 1);// -2 leaves 0's in outsize edge points
				randomY = (int) (Math.random() * (yGridSize - 2) + 1);
				setElevation(randomX, randomY, randomElevation(minHeight, maxHeight));
			}
			surfaceElevationsAverage = new int[xGridSize][yGridSize];
			for (int i = 0; i < 2; i++) {
				for (int y = 1; y < yGridSize - 1; y++)
					for (int x = 1; x < xGridSize - 1; x++)
						setElevationAvg(x, y, average(x, y));
				surfaceElevations = surfaceElevationsAverage;
			}
		}
	}

	Surface3D surface = new Surface3D(50, 50, 150, -300, 300);

	public IsoPanel() {
	}

	public int originX = 0;
	public int originY = 0;
	public float ySkew = 1.0f;
	public Iso3D iso3D = new Iso3D();

	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 1000);
		g.setColor(Color.blue);
		Iso3D.Point2D point1 = null;
		Iso3D.Point2D point2 = null;
		int x = 2;
		int y = 1;
		int xs = 50;
		int ys = 50;
		xs = surface.xSquareSize;
		ys = surface.ySquareSize;
		g.setColor(Color.green);
		for (y = 0; y < surface.yGridSize; y++)
			for (x = 0; x < surface.xGridSize; x++) {
				try {
					if (x < surface.xGridSize - 1) {
						point1 = iso3D.transform3D(
								iso3D.new Point3D(x * xs, 
												 (int) (surface.getElevationAvg(x, y) * ySkew), 
												  y * ys));
						point2 = iso3D.transform3D(
								iso3D.new Point3D((x + 1) * xs,
												  (int) (surface.getElevationAvg(x + 1, y) * ySkew),
												   y * ys));
						
						g.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
					}
					if (y < surface.yGridSize - 1) {
						point1 = iso3D.transform3D(
								iso3D.new Point3D(x * xs, 
												 (int) (surface.getElevationAvg(x, y) * ySkew), 
												  y * ys));
						point2 = iso3D.transform3D(
								iso3D.new Point3D(x * xs,
												 (int) (surface.getElevationAvg(x, y + 1) * ySkew), 
												 (y + 1) * ys));
						
						g.drawLine(point1.x + originX, point1.y + originY, point2.x + originX, point2.y + originY);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
}