package grammar;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import simulator.Constants.MOVE;

class EvaluatorTest {

	@Test
	void testGetNext() {
		String test1 = "if(x){\n"
				 + "	\"RIGHT\";\n"
				 + "	if(y){\n"
				 + "        if(w){\n"
				 + "            \"UP\";\n"
				 + "        }\n"
				 + "		\"LEFT\";\n"
				 + "		\"UP\";\n"
				 + "        if(z){\n"
				 + "             \"NEUTRAL\";"
				 + "             \"DOWN\";"
				 + "        }\n"
				 + "	}\n"
				 + "    \"DOWN\";\n"
				 + "}";
	Parser parser = new Parser();
	JSONObject program = parser.parse(test1);
	
	Evaluator evaluator = new Evaluator(program);
	
	evaluator._variables.put("x", "true");
	evaluator._variables.put("y", "true");
	evaluator._variables.put("z", "true");
	evaluator._variables.put("w", "false");
	assertEquals(MOVE.RIGHT,evaluator.getNext());
	assertEquals(MOVE.LEFT,evaluator.getNext());
	evaluator._variables.put("w", "true");
	evaluator._variables.put("z", "false");
	assertEquals(MOVE.UP,evaluator.getNext());
	assertEquals(MOVE.DOWN,evaluator.getNext());
	assertEquals(MOVE.RIGHT,evaluator.getNext());
	assertEquals(MOVE.UP,evaluator.getNext());
	assertEquals(MOVE.LEFT,evaluator.getNext());
	
	}
	@Test
	void testGetNext2() {
		String test1 = "if(x){\n"
				 + "	\"RIGHT\";\n"
				 + "	if(y){\n"
				 + "		\"UP\";\n"
				 + "	}\n"
				 + "    else{\n"
				 + "        \"NEUTRAL\";"
				 + "        \"LEFT\";"
				 + "    }\n"
				 + "    \"DOWN\";\n"
				 + "}";
	Parser parser = new Parser();
	JSONObject program = parser.parse(test1);
	
	Evaluator evaluator = new Evaluator(program);
	
	evaluator._variables.put("x", "true");
	evaluator._variables.put("y", "false");
	assertEquals(MOVE.RIGHT,evaluator.getNext());
	assertEquals(MOVE.NEUTRAL,evaluator.getNext());
	evaluator._variables.put("y", "true");
	assertEquals(MOVE.DOWN,evaluator.getNext());
	assertEquals(MOVE.RIGHT,evaluator.getNext());
	assertEquals(MOVE.UP,evaluator.getNext());
	assertEquals(MOVE.DOWN,evaluator.getNext());
	assertEquals(MOVE.RIGHT,evaluator.getNext());
	
	}
}
