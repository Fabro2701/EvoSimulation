package genome_editing.model;



import java.util.ArrayList;
import java.util.List;

import genome_editing.model.elements.Vector3D;
import util.Util;

public class Triangulate {
	public static List<Vector3D>createStrand(float shift) {
		List<Vector3D>points = new ArrayList<Vector3D>();
		float t=0f,step=0.2f;
		float domain=5f;
		float a = 1f;
		
		t=-domain;
		
		float x,z;
		while(t<=domain) {
			x = a*Util.sinf(t+shift);
			z = a*Util.cosf(t+shift);
			points.add(new Vector3D(x,t,z));			
			t += step;
		}
		return points;
	}
}
