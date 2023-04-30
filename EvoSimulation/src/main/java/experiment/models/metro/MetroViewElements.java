package experiment.models.metro;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.view.viewer.ViewElement;
import simulator.view.viewer.ViewElementsController;

public class MetroViewElements extends  ViewElementsController{

	float[][]grad = new float[][] {{1,1,0},{1,0,0}};
	public MetroViewElements(java.util.Map<Object, ViewElement> viewElements) {
		super(viewElements);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void produce(List<Entity> entities, Map map) {
		for(Entity e:entities) {
			if(e instanceof PasiveEntity) {
				PasiveEntity pe = (PasiveEntity)e;
				PassengerController passengers = (PassengerController) pe.getAttribute("passengers");
				Node n = e.node;
				float nv = (float) (Math.min(passengers.total(), 3000)/3000f);
				int er = (int) (50f*nv)+30;
	
				viewElements.put(n, (g2)->{
					Color color = util.Util.getGradient(grad, nv,0.2f);
					g2.setColor(color);
					g2.fillOval(n.x-er/2, n.y-er/2, er,er);
				});
				
				String name = (String) pe.getAttribute("station");
				viewElements.put(name, (g2)->{
					g2.setColor(Color.black);
					g2.setFont(new Font("Monospaced", Font.BOLD, 22));
					g2.drawString(name, n.x+15, n.y);
				});

				viewElements.put(name+"pass", (g2)->{
					g2.setColor(Color.black);
					g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
					int i=1;
					for(var entry:passengers.passengers.entrySet()) {
						g2.drawString(entry.getKey(), n.x+15, n.y+i*15);
						g2.drawString(String.valueOf(entry.getValue()), n.x+15+10, n.y+i*15);
						i++;
					}
					
				});
			}
			else {
				PassengerController passengers =  (PassengerController) e.getAttribute("passengers");
				String id = e.getId();
				Node n = e.node;
				viewElements.put(id, (g2)->{
					g2.setColor(Color.black);
					g2.setFont(new Font("Monospaced", Font.BOLD, 22));
					g2.drawString((String) e.getAttribute("route"), n.x+15, n.y);
				});
				viewElements.put(id+"pass", (g2)->{
					g2.setColor(Color.black);
					g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
					int i=1;
					for(var entry:passengers.passengers.entrySet()) {
						g2.drawString(entry.getKey(), n.x+15, n.y+i*15);
						g2.drawString(String.valueOf(entry.getValue()), n.x+15+10, n.y+i*15);
						i++;
					}
				});
				/*String route = (String) e.getAttribute("route");
				viewElements.put(id+"pass", (g2)->{
					g2.setColor(Color.black);
					Node aux = null;
					for(String s:route.split("")){
						for(Entity e2:entities) {
							if(e2 instanceof PasiveEntity) {
								String station = (String) e2.getAttribute("station");
								if(station.equals(s)) {
									if(aux==null)aux=e2.node;
									else {
										g2.drawLine(aux.x, aux.y, e2.node.x, e2.node.y);
										aux=e2.node;
									}
								}
							}
						}
					}
					
					
				});*/
			}
		}
	}

}
