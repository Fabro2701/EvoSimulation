package experiment.models.metro;

import java.awt.Color;
import java.util.List;

import simulator.model.entity.Entity;
import simulator.model.entity.PasiveEntity;
import simulator.model.map.Map;
import simulator.model.map.Node;
import simulator.view.viewer.ViewElement;
import simulator.view.viewer.ViewElementsController;

public class MetroViewElements extends  ViewElementsController{

	public MetroViewElements(java.util.Map<Object, ViewElement> viewElements) {
		super(viewElements);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void produce(List<Entity> entities, Map map) {
		float[][]grad = new float[][] {{1,1,0},{1,0,0}};
		for(Entity e:entities) {
			if(e instanceof PasiveEntity) {
				PasiveEntity pe = (PasiveEntity)e;
				double c = (double) pe.getAttribute("congestion");
				Node n = e.node;
				float nv = (float) (c/100f);
				int er = (int) (50f*nv)+30;
				viewElements.put(n, (g2)->{
					Color color = util.Util.getGradient(grad, nv,0.2f);
					g2.setColor(color);
					g2.fillOval(n.x-er/2, n.y-er/2, er,er);
				});
			}
		}
	}

}
