package setup.gui.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import setup.OOPParser;
import simulator.control.fsm.ComparisonTransition;
import simulator.control.fsm.FSM;
import simulator.control.fsm.SimpleState;
import simulator.control.fsm.StochasticComparisonTransition;
import simulator.control.fsm.Transition;
import simulator.control.fsm.TrueTransition;
import simulator.model.evaluation.ActionEvaluator;

public class FSMBlockTranslation {
	List<SimpleState>states;
	public FSM<String, String>evaluate(JSONObject program){
		FSM<String, String>fsm = new FSM<String, String>();
		
		JSONArray blocks = program.getJSONArray("blocks");
		JSONObject nameDef = blocks.getJSONObject(0);
		JSONObject statesDefs = blocks.getJSONObject(1).getJSONArray("children").getJSONObject(0);
		JSONObject transDefs = blocks.getJSONObject(3).getJSONArray("children").getJSONObject(0);
		String init = this.evaluateName(blocks.getJSONObject(5).getJSONArray("right").getJSONObject(0));
		
		String name = this.evaluateName(nameDef.getJSONArray("right").getJSONObject(0));
		
		List<JSONObject>statesDef = extractFromList(statesDefs, "STATE_DEF");
		states = this.evaluateStatesDefs(statesDef);
		
		List<JSONObject>transDef = extractFromList(transDefs, "TRANSITION_DEF");
		List<Pair<SimpleState, Transition<String>>>trans = this.evaluateTransDefs(transDef);
		
		
		
		System.out.println(name);
		for(SimpleState s:states) {
			System.out.println(s.getData());
			//s.doJobs(null);
		}
		System.out.println(init);
		
		
		for(Pair<SimpleState, Transition<String>>t:trans) {
			fsm.addTransition(t.first, t.second);
		}
		fsm.setCurrent(this.getState(init));
		return fsm;
	}
	private List<Pair<SimpleState, Transition<String>>> evaluateTransDefs(List<JSONObject> transDefs) {
		List<Pair<SimpleState, Transition<String>>> trans = new ArrayList<>();
		for(JSONObject transDef:transDefs) {
			trans.add(evaluateTransDef(transDef));
		}
		return trans;
	}
	private class Pair<E,T>{
		E first;
		T second;
		public Pair(E first, T second) {
			this.first = first;
			this.second = second;
		}
	}
	private Pair<SimpleState, Transition<String>> evaluateTransDef(JSONObject TRANSITION_DEF) {
		String state1 = evaluateName(TRANSITION_DEF.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0).getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));
		String state2 = evaluateName(TRANSITION_DEF.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(2).getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));

		JSONObject transType = TRANSITION_DEF.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("blocks").getJSONObject(0);
		String type = transType.getString("rule");
		switch(type) {
		case "TRUE_T":
			return new Pair<SimpleState, Transition<String>>(getState(state1), new TrueTransition<>(getState(state2)));
		case "COMP_T":
			return new Pair<SimpleState, Transition<String>>(getState(state1), new ComparisonTransition<>(getState(state2), evaluateName(transType.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0))));
		case "SCOMP_T":
			return new Pair<SimpleState, Transition<String>>(getState(state1), new StochasticComparisonTransition<>(List.of(getState(state2)),
																										  			List.of(Float.valueOf(evaluateName(transType.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(1)))), 
																										  			evaluateName(transType.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0))));
		}
		
		return null;
	}
	private SimpleState getState(String name) {
		for(SimpleState state:this.states) if(state.getData().equals(name))return state;
		return null;
	}
	private List<SimpleState> evaluateStatesDefs(List<JSONObject> statesDef) {
		List<SimpleState> states = new ArrayList<>();
		for(JSONObject stateDef:statesDef) {
			states.add(evaluateStateDef(stateDef));
		}
		return states;
	}
	private SimpleState evaluateStateDef(JSONObject STATE_DEF) {
		String name = this.evaluateName(STATE_DEF.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));
		SimpleState state = new SimpleState(name);
		
		OOPParser parser = new OOPParser() {
			@Override
			protected JSONObject Program() {
				return new JSONObject().put("list", this.Especification());
			}
		};
		
		//List<String>actions = new ArrayList<>();
		JSONObject actList = STATE_DEF.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0);
		List<JSONObject>acts = extractFromList(actList, "ACTION");
		for(JSONObject a:acts) {
			String code = evaluateName(a);
			JSONObject codeob = parser.parse(code);
			//actions.add(as);
			state.addJob(e->{
				ActionEvaluator eval = new ActionEvaluator(codeob.getJSONArray("list"));
				java.util.Map<String, Object>vars = new HashMap<String, Object>();
				vars.put("this", e);
				eval.evaluate(vars);
			});
		}
		
		return state;
	}

	private String evaluateName(JSONObject NAME) {
		return NAME.getJSONArray("blocks").getJSONObject(0).getString("text");
	}
	private List<JSONObject> extractFromList(JSONObject ob, String NT) {
		List<JSONObject>list = new ArrayList<>();;
		JSONArray blocks = ob.getJSONArray("blocks");
		if(blocks.length()==1) {
			list.add(blocks.getJSONObject(0).getJSONArray("children").getJSONObject(0));
			list.addAll(extractFromList(blocks.getJSONObject(0).getJSONArray("children").getJSONObject(1), NT));
		}
		else{
			list.add(blocks.getJSONObject(0).getJSONArray("children").getJSONObject(0));
		}
		return list;
	}
	public static void main(String args[]) {
		JSONObject o = new JSONObject("{\r\n"
				+ "                \"blocks\": [\r\n"
				+ "                    {\r\n"
				+ "                        \"name\": {\r\n"
				+ "                            \"text\": \"Def FSM\",\r\n"
				+ "                            \"type\": \"StrBlock\"\r\n"
				+ "                        },\r\n"
				+ "                        \"right\": [{\r\n"
				+ "                            \"blocks\": [{\r\n"
				+ "                                \"text\": \"fsm1\",\r\n"
				+ "                                \"type\": \"InputBlock\"\r\n"
				+ "                            }],\r\n"
				+ "                            \"rule\": \"NAME\",\r\n"
				+ "                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"type\": \"HeaderBlock\"\r\n"
				+ "                    },\r\n"
				+ "                    {\r\n"
				+ "                        \"children\": [{\r\n"
				+ "                            \"blocks\": [{\r\n"
				+ "                                \"children\": [\r\n"
				+ "                                    {\r\n"
				+ "                                        \"blocks\": [\r\n"
				+ "                                            {\r\n"
				+ "                                                \"name\": {\r\n"
				+ "                                                    \"text\": \"Def state\",\r\n"
				+ "                                                    \"type\": \"StrBlock\"\r\n"
				+ "                                                },\r\n"
				+ "                                                \"right\": [{\r\n"
				+ "                                                    \"blocks\": [{\r\n"
				+ "                                                        \"text\": \"bar\",\r\n"
				+ "                                                        \"type\": \"InputBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"rule\": \"NAME\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"type\": \"HeaderBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\r\n"
				+ "                                                \"children\": [{\r\n"
				+ "                                                    \"blocks\": [\r\n"
				+ "                                                        {\r\n"
				+ "                                                            \"children\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"text\": \"this.setAttribute(\\\"rest\\\",50);\",\r\n"
				+ "                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"ACTION\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                    ],\r\n"
				+ "                                                    \"rule\": \"ACTIONLIST\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\"type\": \"FloorBlock\"}\r\n"
				+ "                                        ],\r\n"
				+ "                                        \"rule\": \"STATE_DEF\",\r\n"
				+ "                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                    },\r\n"
				+ "                                    {\r\n"
				+ "                                        \"blocks\": [\r\n"
				+ "                                            {\r\n"
				+ "                                                \"children\": [{\r\n"
				+ "                                                    \"blocks\": [\r\n"
				+ "                                                        {\r\n"
				+ "                                                            \"name\": {\r\n"
				+ "                                                                \"text\": \"Def state\",\r\n"
				+ "                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            \"right\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"text\": \"house\",\r\n"
				+ "                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"NAME\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        {\r\n"
				+ "                                                            \"children\": [{\r\n"
				+ "                                                                \"blocks\": [\r\n"
				+ "                                                                    {\r\n"
				+ "                                                                        \"children\": [{\r\n"
				+ "                                                                            \"blocks\": [{\r\n"
				+ "                                                                                \"text\": \"this.setAttribute(\\\"rest\\\",100);\",\r\n"
				+ "                                                                                \"type\": \"InputBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"rule\": \"ACTION\",\r\n"
				+ "                                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                                    },\r\n"
				+ "                                                                    {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                                ],\r\n"
				+ "                                                                \"rule\": \"ACTIONLIST\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                    ],\r\n"
				+ "                                                    \"rule\": \"STATE_DEF\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\"type\": \"FloorBlock\"}\r\n"
				+ "                                        ],\r\n"
				+ "                                        \"rule\": \"STATE_DEFLIST\",\r\n"
				+ "                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                    }\r\n"
				+ "                                ],\r\n"
				+ "                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                            }],\r\n"
				+ "                            \"rule\": \"STATE_DEFLIST\",\r\n"
				+ "                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"type\": \"ChildrenBlock\"\r\n"
				+ "                    },\r\n"
				+ "                    {\"type\": \"FloorBlock\"},\r\n"
				+ "                    {\r\n"
				+ "                        \"children\": [{\r\n"
				+ "                            \"blocks\": [{\r\n"
				+ "                                \"children\": [\r\n"
				+ "                                    {\r\n"
				+ "                                        \"blocks\": [\r\n"
				+ "                                            {\r\n"
				+ "                                                \"name\": {\r\n"
				+ "                                                    \"text\": \"Def Transition\",\r\n"
				+ "                                                    \"type\": \"StrBlock\"\r\n"
				+ "                                                },\r\n"
				+ "                                                \"right\": [\r\n"
				+ "                                                    {\r\n"
				+ "                                                        \"blocks\": [{\r\n"
				+ "                                                            \"name\": {\r\n"
				+ "                                                                \"text\": \"name\",\r\n"
				+ "                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            \"right\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"text\": \"house\",\r\n"
				+ "                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"NAME\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"rule\": \"STATE\",\r\n"
				+ "                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                    },\r\n"
				+ "                                                    {\r\n"
				+ "                                                        \"text\": \" to \",\r\n"
				+ "                                                        \"type\": \"StrBlock\"\r\n"
				+ "                                                    },\r\n"
				+ "                                                    {\r\n"
				+ "                                                        \"blocks\": [{\r\n"
				+ "                                                            \"name\": {\r\n"
				+ "                                                                \"text\": \"name\",\r\n"
				+ "                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            \"right\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"text\": \"bar\",\r\n"
				+ "                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"NAME\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"rule\": \"STATE\",\r\n"
				+ "                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                    }\r\n"
				+ "                                                ],\r\n"
				+ "                                                \"type\": \"HeaderBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\r\n"
				+ "                                                \"children\": [{\r\n"
				+ "                                                    \"blocks\": [{\r\n"
				+ "                                                        \"blocks\": [{\r\n"
				+ "                                                            \"name\": {\r\n"
				+ "                                                                \"text\": \"equals:\",\r\n"
				+ "                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            \"right\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"text\": \"house\",\r\n"
				+ "                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"NAME\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"rule\": \"COMP_T\",\r\n"
				+ "                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"rule\": \"TRANSITION_TYPE\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\"type\": \"FloorBlock\"}\r\n"
				+ "                                        ],\r\n"
				+ "                                        \"rule\": \"TRANSITION_DEF\",\r\n"
				+ "                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                    },\r\n"
				+ "                                    {\r\n"
				+ "                                        \"blocks\": [\r\n"
				+ "                                            {\r\n"
				+ "                                                \"children\": [{\r\n"
				+ "                                                    \"blocks\": [\r\n"
				+ "                                                        {\r\n"
				+ "                                                            \"name\": {\r\n"
				+ "                                                                \"text\": \"Def Transition\",\r\n"
				+ "                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            \"right\": [\r\n"
				+ "                                                                {\r\n"
				+ "                                                                    \"blocks\": [{\r\n"
				+ "                                                                        \"name\": {\r\n"
				+ "                                                                            \"text\": \"name\",\r\n"
				+ "                                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                                        },\r\n"
				+ "                                                                        \"right\": [{\r\n"
				+ "                                                                            \"blocks\": [{\r\n"
				+ "                                                                                \"text\": \"bar\",\r\n"
				+ "                                                                                \"type\": \"InputBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"rule\": \"NAME\",\r\n"
				+ "                                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"rule\": \"STATE\",\r\n"
				+ "                                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                },\r\n"
				+ "                                                                {\r\n"
				+ "                                                                    \"text\": \" to \",\r\n"
				+ "                                                                    \"type\": \"StrBlock\"\r\n"
				+ "                                                                },\r\n"
				+ "                                                                {\r\n"
				+ "                                                                    \"blocks\": [{\r\n"
				+ "                                                                        \"name\": {\r\n"
				+ "                                                                            \"text\": \"name\",\r\n"
				+ "                                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                                        },\r\n"
				+ "                                                                        \"right\": [{\r\n"
				+ "                                                                            \"blocks\": [{\r\n"
				+ "                                                                                \"text\": \"house\",\r\n"
				+ "                                                                                \"type\": \"InputBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"rule\": \"NAME\",\r\n"
				+ "                                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"rule\": \"STATE\",\r\n"
				+ "                                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                }\r\n"
				+ "                                                            ],\r\n"
				+ "                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        {\r\n"
				+ "                                                            \"children\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"blocks\": [{\r\n"
				+ "                                                                        \"name\": {\r\n"
				+ "                                                                            \"text\": \"equals:\",\r\n"
				+ "                                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                                        },\r\n"
				+ "                                                                        \"right\": [{\r\n"
				+ "                                                                            \"blocks\": [{\r\n"
				+ "                                                                                \"text\": \"bar\",\r\n"
				+ "                                                                                \"type\": \"InputBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"rule\": \"NAME\",\r\n"
				+ "                                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"rule\": \"COMP_T\",\r\n"
				+ "                                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"TRANSITION_TYPE\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                    ],\r\n"
				+ "                                                    \"rule\": \"TRANSITION_DEF\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\"type\": \"FloorBlock\"}\r\n"
				+ "                                        ],\r\n"
				+ "                                        \"rule\": \"TRANSITION_DEFLIST\",\r\n"
				+ "                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                    }\r\n"
				+ "                                ],\r\n"
				+ "                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                            }],\r\n"
				+ "                            \"rule\": \"TRANSITION_DEFLIST\",\r\n"
				+ "                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"type\": \"ChildrenBlock\"\r\n"
				+ "                    },\r\n"
				+ "                    {\"type\": \"FloorBlock\"},\r\n"
				+ "                    {\r\n"
				+ "                        \"name\": {\r\n"
				+ "                            \"text\": \"init \",\r\n"
				+ "                            \"type\": \"StrBlock\"\r\n"
				+ "                        },\r\n"
				+ "                        \"right\": [{\r\n"
				+ "                            \"blocks\": [{\r\n"
				+ "                                \"text\": \"house\",\r\n"
				+ "                                \"type\": \"InputBlock\"\r\n"
				+ "                            }],\r\n"
				+ "                            \"rule\": \"NAME\",\r\n"
				+ "                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"type\": \"HeaderBlock\"\r\n"
				+ "                    }\r\n"
				+ "                ],\r\n"
				+ "                \"rule\": \"FSM_DEF\",\r\n"
				+ "                \"type\": \"RecursiveBlock\"\r\n"
				+ "            }");
		FSMBlockTranslation t = new FSMBlockTranslation();
		t.evaluate(o);
	}
}
