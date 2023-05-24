package experiment.models.metro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class RouteManager {
	Map<String, Set<String>>stations;
	private Map<String,Map<String,List<String>>>paths;
	
	public RouteManager() {
		this.stations = new LinkedHashMap<>();
	}
	public void signIn(String route) {
		for(String s:route.split("")) {
			for(String s2:route.split("")) {
				if(!s.equals(s2)){
					stations.computeIfAbsent(s, i->new HashSet<>()).add(s2);
				}
			}
		}
		System.out.println(stations);
		buildPaths();
	}
	public boolean direct(String origin, String dest) {
		return stations.containsKey(origin)&&stations.get(origin).contains(dest);
	}
	public int connections(String station) {
		return stations.containsKey(station)?stations.get(station).size():0;
	}
	public List<String>getPath(String origin, String dest){
		return this.paths.get(origin).get(dest);
	}
	
	private void buildPaths() {
		paths = new HashMap<>();
		for(var origin:this.stations.keySet()) {
			paths.put(origin, new HashMap<>());
			for(var dest:this.stations.keySet()) {
				paths.get(origin).put(dest, encontrarCaminoMasCorto(this.stations,origin,dest));
			}
		}
		System.out.println(paths);
	}
	
	public static List<String> encontrarCaminoMasCorto(Map<String, Set<String>> grafo, String origin, String dest) {
        Map<String, String> predecesores = new HashMap<>();
        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();

        predecesores.put(origin, null);
        cola.add(origin);

        while (!cola.isEmpty()) {
            String current = cola.poll();

            if (current.equals(dest)) {
                return reconstruirCamino(predecesores, origin, dest);
            }

            for (String neigh : grafo.getOrDefault(current, new HashSet<>())) {
                if (!visitados.contains(neigh)) {
                    predecesores.put(neigh, current);
                    cola.add(neigh);
                    visitados.add(neigh);
                }
            }
            
        }

        return null;
    }

    private static List<String> reconstruirCamino(Map<String, String> predecesores, String origin, String dest) {
        List<String> path = new ArrayList<>();
        String current = dest;
        
        
        while (!current.equals(origin)) {
        	path.add(current);
            current = predecesores.get(current);
        }
        
        //camino.add(origen);
        Collections.reverse(path);
        
        return path;
    }
	public static void main(String args[]) {
		RouteManager r = new RouteManager();
		r.signIn("ABC");
		r.signIn("BD");
		r.signIn("DEF");
		
		System.out.println(r.stations);
		var a = encontrarCaminoMasCorto(r.stations,"A","D");
		System.out.println(a);
		r.buildPaths();
		System.out.println(r.paths);
		
	}
}
