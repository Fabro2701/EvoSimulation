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
	
	public static List<String> encontrarCaminoMasCorto(Map<String, Set<String>> grafo, String origen, String destino) {
        Map<String, String> predecesores = new HashMap<>(); // map para guardar el predecesor de cada nodo
        Queue<String> cola = new LinkedList<>(); // cola para BFS
        Set<String> visitados = new HashSet<>(); // conjunto de nodos visitados

        predecesores.put(origen, null); // el nodo origen no tiene predecesor
        cola.add(origen); // añadir el nodo origen a la cola

        while (!cola.isEmpty()) {
            String actual = cola.poll(); // obtener el siguiente nodo de la cola

            if (actual.equals(destino)) { // si hemos llegado al destino, reconstruir el camino y devolverlo
                return reconstruirCamino(predecesores, origen, destino);
            }

            for (String vecino : grafo.getOrDefault(actual, new HashSet<>())) { // iterar por los vecinos del nodo actual
                if (!visitados.contains(vecino)) { // si el vecino no ha sido visitado, añadirlo a la cola y marcarlo como visitado
                    predecesores.put(vecino, actual);
                    cola.add(vecino);
                    visitados.add(vecino);
                }
            }
            
        }

        return null; // si no se encontró un camino, devolver null
    }

    private static List<String> reconstruirCamino(Map<String, String> predecesores, String origen, String destino) {
        List<String> camino = new ArrayList<>(); // lista para almacenar el camino desde el origen hasta el destino
        String actual = destino; // empezar por el nodo destino
        
        
        while (!actual.equals(origen)) { // mientras haya predecesores
            camino.add(actual); // añadir el nodo actual al camino
            actual = predecesores.get(actual); // avanzar al predecesor del nodo actual
        }
        
        //camino.add(origen);
        Collections.reverse(camino); // invertir el orden del camino para que vaya desde el origen hasta el destino
        
        return camino;
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
