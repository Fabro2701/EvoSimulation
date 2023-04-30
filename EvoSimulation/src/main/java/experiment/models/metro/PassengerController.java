package experiment.models.metro;

import java.util.List;
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
	public void liberate(PassengerController receptor, String station, String route) {
		if(route.equals("ABC")) {
			int a=0;
		}
		//passengers arrived
		if(this.passengers.containsKey(station)) {
			this.passengers.put(station, 0);
		}
		
		//stopovers
		for(var entry:this.passengers.entrySet()) {
			String dest = entry.getKey();
			if(dest.equals(station))continue;
			List<String>path=this.routeManager.getPath(station, dest);
			boolean found=false;
			for(String s:route.split("")) {
				if(path.contains(s)) {
					found=true;
					break;//on the way
				}
			}
			if(!found) {//unreachable
				int min=Integer.MAX_VALUE;
				String r = null;
				for(String s:route.split("")) {
					int d = this.routeManager.getPath(s, dest).size();
 					if(d<min) {
						min=d;
						r=s;
					}
				}
				if(this.routeManager.getPath(station, dest).size()==min) {
					receptor.addPassengers(dest, this.passengers.get(dest));
					this.passengers.put(dest, 0);
				}
			}
		}
	}
	public void transfer(PassengerController train, String route, int max) {
		if(max<0)return;
		int total = this.total();
		if(total==0)return;
		
		for(var entry:this.passengers.entrySet()) {
			String key = entry.getKey();
			//if(route.contains(key)) {
				if(total<=max) {
					train.addPassengers(key, this.passengers.get(key));
					this.passengers.put(key,0);
				}
				else {
					float c = max*this.passengers.get(key)/total;
					train.addPassengers(key, (int) c);
					this.passengers.put(key,this.passengers.get(key)-(int)c);
				}
				
			//}
		}
	
	}
	public int total() {
		return passengers.values().stream().mapToInt(i->i).sum();
	}
}
