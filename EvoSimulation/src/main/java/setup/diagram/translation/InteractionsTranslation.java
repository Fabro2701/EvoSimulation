package setup.diagram.translation;

import org.json.JSONObject;

import setup.diagram.elements.Element;
import setup.diagram.elements.EntityElement;
import setup.diagram.elements.GroupElement;
import setup.diagram.elements.InteractionElement;

public class InteractionsTranslation {
	public static String translate(InteractionElement e, JSONObject ob) {
		JSONObject cjo = ob.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("blocks").getJSONObject(0);
		String body = cjo.getString("text");
		StringBuilder sb = new StringBuilder();
		String id = e.getId();

		sb.append(id);
		
		String freq = ob.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(1).getJSONArray("blocks").getJSONObject(0).getString("text");
		sb.append("(").append(freq).append(")");
		
		GroupElement fromE = null;
		String from = null;
		boolean fromb = false;
		if(e.getFrom() instanceof EntityElement) {
			from = ((EntityElement)e.getFrom()).getClazz().getName();
		}
		else {
			fromb = true;
			fromE = (GroupElement)e.getFrom();
			from = ((EntityElement)fromE.getFather()).getClazz().getName();
		}
		GroupElement toE = null;
		String to = null;
		boolean tob = false;
		if(e.getTo() instanceof EntityElement) {
			to = ((EntityElement)e.getTo()).getClazz().getName();
		}
		else {
			tob = true;
			toE = (GroupElement)e.getTo();
			to = ((EntityElement)toE.getFather()).getClazz().getName();
		}
		sb.append("(\""+from+"\"->\""+to+"\""+")");
		sb.append("{\n");
		if(fromb&&tob) {
			sb.append("if(this.getAttribute(\""+fromE.getAtt()+"\").equals(\""+fromE.getValue()+"\")");
			sb.append("&&e2.getAttribute(\""+toE.getAtt()+"\").equals(\""+toE.getValue()+"\")");
			sb.append("){").append('\n');
			sb.append(body).append('\n');
			sb.append("}\n");
		}
		else if(fromb) {
			sb.append("if(this.getAttribute(\""+fromE.getAtt()+"\").equals(\""+fromE.getValue()+"\")){").append('\n');
			sb.append(body).append('\n');
			sb.append("}\n");
		}
		else if(tob) {
			sb.append("if(this.getAttribute(\""+toE.getAtt()+"\").equals(\""+toE.getValue()+"\")){").append('\n');
			sb.append(body).append('\n');
			sb.append("}\n");
		}
		else{
			sb.append(body).append('\n');
		}
		sb.append("}\n");
		return sb.toString();
	}
}
