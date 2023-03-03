package block_manipulation;

public class Vector2D implements Cloneable{
	public float x,y;
	public Vector2D() {
		
	}
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D copy) {
		x=copy.x;
		y=copy.y;
	}
	public static float dotProduct(Vector2D l, Vector2D r) {
		return l.x*r.x + l.y*r.y;
	}
	public static float length(Vector2D l) {
		//return ((float) Math.sqrt(Vector3D.dotProduct(l, l)));
		return Math.abs((float) Math.sqrt(Vector2D.dotProduct(l, l)));
	}
	public static Vector2D normal(Vector2D l) {
		float aux = Vector2D.length(l);
		return new Vector2D(l.x/aux,l.y/aux);
	}
	public static Vector2D abs(Vector2D l) {
		return new Vector2D(Math.abs(l.x),Math.abs(l.y));
	}
	public static Vector2D add(Vector2D l, Vector2D r) {
		return new Vector2D(l.x+r.x,l.y+r.y);
	}
	public static Vector2D sub(Vector2D l, Vector2D r) {
		return new Vector2D(l.x-r.x,l.y-r.y);
	}
	public static Vector2D mul(Vector2D l, Vector2D r) {
		return new Vector2D(l.x*r.x,l.y*r.y);
	}
	public static Vector2D div(Vector2D l, float r) {
		return new Vector2D(l.x/r,l.y/r);
	}
	public static Vector2D mul(Vector2D l, float r) {
		return new Vector2D(l.x*r,l.y*r);
	}
	public static Vector2D sub(Vector2D l, float r) {
		return new Vector2D(l.x-r,l.y-r);
	}
	public static Vector2D add(Vector2D l, float r) {
		return new Vector2D(l.x+r,l.y+r);
	}
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
	@Override
	public Object clone() {
		return new Vector2D(this);
	}
}









