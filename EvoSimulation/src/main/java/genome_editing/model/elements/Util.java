package genome_editing.model.elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import genome_editing.model.elements.Triangle;

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
}
