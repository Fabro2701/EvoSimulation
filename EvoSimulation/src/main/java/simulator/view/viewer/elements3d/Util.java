package simulator.view.viewer.elements3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import simulator.view.viewer.elements3d.elements.Triangle;
import simulator.view.viewer.elements3d.Vector3D;


public class Util {
	public static float cosf(float x) {
		return (float) Math.cos(x);
	}
	public static float sinf(float x) {
		return (float) Math.sin(x);
	}
	public static float tanf(float x) {
		return (float) Math.tan(x);
	}
	public  static List<Triangle> loadFromObj(String filename) {
		List<Triangle> tris = new ArrayList<Triangle>();
		List<Vector3D> verts = new ArrayList<Vector3D>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("resources/"+filename));
			String line = reader.readLine();
			while(line!=null) {
				String info[] = line.split(" ");
				if(info[0].equals("v")) {
					verts.add(new Vector3D(Float.parseFloat(info[1]),Float.parseFloat(info[2]),Float.parseFloat(info[3])));
				}
				if(info[0].equals("f")) {
					tris.add(new Triangle(verts.get(Integer.parseInt(info[1])-1),verts.get(Integer.parseInt(info[2])-1),verts.get(Integer.parseInt(info[3])-1)));
				}
				line = reader.readLine();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(filename+ " loaded");
		return tris;
	}
	public static List<Triangle> createSquare() {
		List<Triangle> arr = new ArrayList<Triangle>();

		// SOUTH
		arr.add(new Triangle(new Vector3D(0.0f, 0.0f, 0.0f),    new Vector3D(0.0f, 1.0f, 0.0f),    new Vector3D(1.0f, 1.0f, 0.0f)));                    
		arr.add(new Triangle(new Vector3D(0.0f, 0.0f, 0.0f),    new Vector3D(1.0f, 1.0f, 0.0f),    new Vector3D(1.0f, 0.0f, 0.0f)));

		// EAST                                                      
		arr.add(new Triangle(new Vector3D(1.0f, 0.0f, 0.0f),    new Vector3D(1.0f, 1.0f, 0.0f),    new Vector3D(1.0f, 1.0f, 1.0f)));
		arr.add(new Triangle(new Vector3D(1.0f, 0.0f, 0.0f),    new Vector3D(1.0f, 1.0f, 1.0f),    new Vector3D(1.0f, 0.0f, 1.0f)));
		//NORTH                                                     
		arr.add(new Triangle(new Vector3D(1.0f, 0.0f, 1.0f),    new Vector3D(1.0f, 1.0f, 1.0f),    new Vector3D(0.0f, 1.0f, 1.0f)));
		arr.add(new Triangle(new Vector3D(1.0f, 0.0f, 1.0f),    new Vector3D(0.0f, 1.0f, 1.0f),    new Vector3D(0.0f, 0.0f, 1.0f)));
       
		//WEST                                                      
		arr.add(new Triangle(new Vector3D(0.0f, 0.0f, 1.0f),    new Vector3D(0.0f, 1.0f, 1.0f),    new Vector3D(0.0f, 1.0f, 0.0f)));
		arr.add(new Triangle(new Vector3D(0.0f, 0.0f, 1.0f),    new Vector3D(0.0f, 1.0f, 0.0f),    new Vector3D(0.0f, 0.0f, 0.0f)));
        
		//TOP                                                       
		arr.add(new Triangle(new Vector3D(0.0f, 1.0f, 0.0f),    new Vector3D(0.0f, 1.0f, 1.0f),    new Vector3D(1.0f, 1.0f, 1.0f)));
		arr.add(new Triangle(new Vector3D(0.0f, 1.0f, 0.0f),    new Vector3D(1.0f, 1.0f, 1.0f),    new Vector3D(1.0f, 1.0f, 0.0f)));
        
		//BOTTOM                                                    
		arr.add(new Triangle(new Vector3D(1.0f, 0.0f, 1.0f),    new Vector3D(0.0f, 0.0f, 1.0f),    new Vector3D(0.0f, 0.0f, 0.0f)));
		arr.add(new Triangle(new Vector3D(1.0f, 0.0f, 1.0f),    new Vector3D(0.0f, 0.0f, 0.0f),    new Vector3D(1.0f, 0.0f, 0.0f)));
		
		return arr;
	}
	public static void main2(String args[]) {
		Vector3D aux = new Vector3D(3.4f,5f,0.13f);
		
	}
}
