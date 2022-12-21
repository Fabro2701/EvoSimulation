package simulator.control;

import org.json.JSONObject;

import simulator.model.evaluation.ActionEvaluator;
import simulator.model.evaluation.EvaluationException;
import simulator.view.ConstantsViewer;

public class ConstantsController {
	
	ConstantsViewer observer;
	
	public void paint() {
		if(observer!=null) {
			observer.repaint(ActionEvaluator.globalEnv.getRecord());
			//observer.repaint();
		}
	}
	public void update(String id, Object value) {
		System.out.println("updating: "+id+"  "+value);
//		"type": "VariableStatement",
//        "declaration": {
//            "init": {
//                "type": "NumberLiteral",
//                "value": "0.0004578",
//                "class": "java.lang.Float"
//            },
//            "id": {
//                "name": "mu",
//                "type": "Identifier"
//            },
//            "type": "VaribleDeclaration"
//        }
//		JSONObject query = new JSONObject().put("type", "VariableStatement")
//										   .put("declaration", new JSONObject().put("init", "NumberLiteral")
//												   							   .put("id", false)
//												   							   .put("type", "VaribleDeclaration")
//											   );
		
		//ActionEvaluator.evaluateGlobal(query);
		try {
			ActionEvaluator.globalEnv.assign(id, value);
		} catch (EvaluationException e) {
			System.err.println("Exception in global variables assingment");
			e.printStackTrace();
		}
		paint();
	}
	public void addObserver(ConstantsViewer constantsViewer) {
		observer = constantsViewer;
	}

}
