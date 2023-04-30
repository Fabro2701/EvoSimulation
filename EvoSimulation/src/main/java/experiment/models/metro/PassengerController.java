package experiment.models.metro;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PassengerController {
	Map<String,Integer>passengers;
	private RouteManager routeManager;
	public PassengerController(RouteManager routeManager) {
		this.routeManager = routeManager;
		this.passengers = new ConcurrentHashMap<>();
	}

	public void addPassengers(String dest, int n) {
		if(this.passengers.containsKey(dest)) {
			this.passengers.put(dest, this.passengers.get(dest)+n);
		}
		else {
			this.passengers.put(dest, n);
		}
	}
	public void liberate(String station, String route) {
		//passengers arrived
		if(this.passengers.containsKey(station)) {
			this.passengers.put(station, 0);
		}
		
		//stopovers
		/*for(var entry:this.passengers.entrySet()) {
			String key = entry.getKey();
			
		}*/
	}
	public void transfer(PassengerController train, String route, int max) {
		if(max<0)return;
		int total = this.total();
		if(total==0)return;
		if(total<=max) {
			for(var entry:this.passengers.entrySet()) {
				String key = entry.getKey();
				if(route.contains(key)) {
					train.addPassengers(key, this.passengers.get(key));
					this.passengers.put(key,0);
				}
			}
		}
		else {
			for(var entry:this.passengers.entrySet()) {
				String key = entry.getKey();
				if(route.contains(key)) {
					float c = max*this.passengers.get(key)/total;
					train.addPassengers(key, (int) c);
					this.passengers.put(key,this.passengers.get(key)-(int)c);
				}
			}
		}
		
	}
	public int total() {
		return passengers.values().stream().mapToInt(i->i).sum();
	}
}
