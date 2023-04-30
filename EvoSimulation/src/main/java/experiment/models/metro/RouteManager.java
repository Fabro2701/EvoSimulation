package experiment.models.metro;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteManager {
	Map<String, Set<String>>stations;
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
	}
	public boolean direct(String origin, String dest) {
		return stations.containsKey(origin)&&stations.get(origin).contains(dest);
	}
	public int connections(String station) {
		return stations.containsKey(station)?stations.get(station).size():0;
	}
	
	public static void main(String args[]) {
		RouteManager r = new RouteManager();
		r.signIn("ABC");
		r.signIn("BD");
		r.signIn("DEF");
		System.out.println(r.stations);
	}
}
