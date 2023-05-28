package grammar;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import setup.OOPParser;
import simulator.Constants.MOVE;
import simulator.model.evaluation.ActionEvaluator;
import simulator.model.evaluation.EvaluationException;

class EvaluatorTest {
	public static class TestClass{
		int x;
		public TestClass() {
			
		}
		public void setX(int x) {
			this.x=x;
		}
		public int getX() {
			return x;
		}
	}
	@Test
	void testGetNext() {
		OOPParser parser = new OOPParser() {
			@Override
			protected JSONObject Program() {
				return new JSONObject().put("list", this.Especification());
			}
		};
		String test1 = "if(b){\n"
				 + "	t.setX(5);"
				 + "}else{t.setX(7);}";
		ActionEvaluator eval = new ActionEvaluator(parser.parse(test1).getJSONArray("list"));
		java.util.Map<String, Object>vars = new HashMap<String, Object>();
		TestClass t = new TestClass();
		vars.put("t", t);
		vars.put("b", true);
		try {
			eval.evaluate(vars);
		} catch (EvaluationException e) {
			System.out.println("Error applying code");
			e.printStackTrace();
			assertFalse(true);
		}
		assertEquals(5,t.getX());

	
	}
	
}
