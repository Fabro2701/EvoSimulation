package test;

public class Iso3D {
	public class Point2D {
		public Point2D(int x, int y) {
			this.x = x;
			this.y = y;
		}

		int x = 0;
		int y = 0;
	}

	public class Point3D {
		public Point3D(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		int x = 0;
		int y = 0;
		int z = 0;
	}

	public Iso3D() {
	}

	int mode = 1;
	public final int LEFT_ISO = 0;
	public final int CENTER_ISO = 1;
	public final int RIGHT_ISO = 2;
	public float ySkew = 1.0f;

	public void setTransformMode(int mode) {
		this.mode = mode;
	}

	public Point2D transform3D(Point3D point3D) throws Exception {
		switch (mode) {
		case LEFT_ISO:
			return new Point2D(point3D.x + point3D.z, (int) (((-point3D.y) + point3D.z) * ySkew));
		case CENTER_ISO:
			return new Point2D(point3D.x + point3D.z, (int) (((-point3D.y) + point3D.z - point3D.x) * ySkew));
		case RIGHT_ISO:
			return new Point2D(point3D.x - point3D.z, (int) (((-point3D.y) + point3D.z) * ySkew));
		default:
			throw new Exception("Invalid Transformation Mode! [" + mode + "]?");
		}
	}

	public static void main(String args[]) {
		new Iso3D();
	}
}
