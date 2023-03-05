package setup.gui.block;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.fsm.FSM;
import simulator.control.fsm.SimpleState;
import simulator.control.fsm.StochasticComparisonTransition;
import simulator.control.fsm.Transition;
import simulator.control.fsm.TrueTransition;

public class FSMBlockTranslation {
	List<SimpleState>states;
	FSM<String, String>evaluate(JSONObject program){
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
		List<Pair<SimpleState, Transition>>trans = this.evaluateTransDefs(transDef);
		
		
		
		System.out.println(name);
		for(SimpleState s:states) {
			System.out.println(s.getData());
			s.doJobs(null);
		}
		System.out.println(init);
		
		
		for(Pair<SimpleState, Transition>t:trans) {
			fsm.addTransition(t.first, t.second);
		}
		return fsm;
	}
	private List<Pair<SimpleState, Transition>> evaluateTransDefs(List<JSONObject> transDefs) {
		List<Pair<SimpleState, Transition>> trans = new ArrayList<>();
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
	private Pair<SimpleState, Transition> evaluateTransDef(JSONObject TRANSITION_DEF) {
		String state1 = evaluateName(TRANSITION_DEF.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0).getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));
		String state2 = evaluateName(TRANSITION_DEF.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(2).getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0));

		JSONObject transType = TRANSITION_DEF.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0);
		String type = transType.getString("type");
		switch(type) {
		case "TRUE_T":
			return new Pair<SimpleState, Transition>(getState(state1), new TrueTransition(getState(state2)));
		case "PROB_T":
			return new Pair<SimpleState, Transition>(getState(state1), new StochasticComparisonTransition(List.of(),List.of(),getState(state2)));
		case "COND_T":
			break;
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
		
		List<String>actions = new ArrayList<>();
		JSONObject actList = STATE_DEF.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0);
		List<JSONObject>acts = extractFromList(actList, "ACTION");
		for(JSONObject a:acts) {
			String as = evaluateName(a);
			actions.add(as);
			state.addJob(s->System.out.println(as));
		}
		
		return state;
	}
	private String evaluateName(JSONObject NAME) {
		return NAME.getJSONArray("blocks").getJSONObject(0).getString("text");
	}
	private List<JSONObject> extractFromList(JSONObject ob, String NT) {
		List<JSONObject>list = new ArrayList<>();;
		JSONArray blocks = ob.getJSONArray("blocks");
		for(int i=0;i<blocks.length();i++) {
			JSONObject block = blocks.getJSONObject(i);
			if(block.getString("type").equals("ChildrenBlock")) {
				list.add(block.getJSONArray("children").getJSONObject(0));
				list.addAll(extractFromList(block.getJSONArray("children").getJSONObject(1), NT));
			}
			else if(block.getString("type").equals("RecursiveBlock")){//NT
				list.add(block);
			}
		}
		return list;
	}
	public static void main(String args[]) {
		JSONObject o = new JSONObject("{\r\n"
				+ "            \"blocks\": [\r\n"
				+ "                {\r\n"
				+ "                    \"name\": {\r\n"
				+ "                        \"text\": \"Def FSM\",\r\n"
				+ "                        \"type\": \"StrBlock\"\r\n"
				+ "                    },\r\n"
				+ "                    \"right\": [{\r\n"
				+ "                        \"blocks\": [{\r\n"
				+ "                            \"text\": \"fsm1\",\r\n"
				+ "                            \"type\": \"InputBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"rule\": \"NAME\",\r\n"
				+ "                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                    }],\r\n"
				+ "                    \"type\": \"HeaderBlock\"\r\n"
				+ "                },\r\n"
				+ "                {\r\n"
				+ "                    \"children\": [{\r\n"
				+ "                        \"blocks\": [{\r\n"
				+ "                            \"children\": [\r\n"
				+ "                                {\r\n"
				+ "                                    \"blocks\": [\r\n"
				+ "                                        {\r\n"
				+ "                                            \"name\": {\r\n"
				+ "                                                \"text\": \"Def state\",\r\n"
				+ "                                                \"type\": \"StrBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            \"right\": [{\r\n"
				+ "                                                \"blocks\": [{\r\n"
				+ "                                                    \"text\": \"house\",\r\n"
				+ "                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"rule\": \"NAME\",\r\n"
				+ "                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                            }],\r\n"
				+ "                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                        },\r\n"
				+ "                                        {\r\n"
				+ "                                            \"children\": [{\r\n"
				+ "                                                \"blocks\": [{\r\n"
				+ "                                                    \"blocks\": [{\r\n"
				+ "                                                        \"text\": \"this.setAttribute(\\\"e\\\",2);\",\r\n"
				+ "                                                        \"type\": \"InputBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"rule\": \"ACTION\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"rule\": \"ACTIONLIST\",\r\n"
				+ "                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                            }],\r\n"
				+ "                                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                                        },\r\n"
				+ "                                        {\"type\": \"FloorBlock\"}\r\n"
				+ "                                    ],\r\n"
				+ "                                    \"rule\": \"STATE_DEF\",\r\n"
				+ "                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                },\r\n"
				+ "                                {\r\n"
				+ "                                    \"blocks\": [\r\n"
				+ "                                        {\r\n"
				+ "                                            \"blocks\": [\r\n"
				+ "                                                {\r\n"
				+ "                                                    \"name\": {\r\n"
				+ "                                                        \"text\": \"Def state\",\r\n"
				+ "                                                        \"type\": \"StrBlock\"\r\n"
				+ "                                                    },\r\n"
				+ "                                                    \"right\": [{\r\n"
				+ "                                                        \"blocks\": [{\r\n"
				+ "                                                            \"text\": \"super\",\r\n"
				+ "                                                            \"type\": \"InputBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"rule\": \"NAME\",\r\n"
				+ "                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"type\": \"HeaderBlock\"\r\n"
				+ "                                                },\r\n"
				+ "                                                {\r\n"
				+ "                                                    \"children\": [{\r\n"
				+ "                                                        \"blocks\": [{\r\n"
				+ "                                                            \"children\": [\r\n"
				+ "                                                                {\r\n"
				+ "                                                                    \"blocks\": [{\r\n"
				+ "                                                                        \"text\": \"this.setAttribute(\\\"rest\\\",4);\",\r\n"
				+ "                                                                        \"type\": \"InputBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"rule\": \"ACTION\",\r\n"
				+ "                                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                },\r\n"
				+ "                                                                {\r\n"
				+ "                                                                    \"blocks\": [{\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"text\": \"this.setAttribute(\\\"rest\\\",\\\"re\\\");\",\r\n"
				+ "                                                                            \"type\": \"InputBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"ACTION\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"rule\": \"ACTIONLIST\",\r\n"
				+ "                                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                }\r\n"
				+ "                                                            ],\r\n"
				+ "                                                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"rule\": \"ACTIONLIST\",\r\n"
				+ "                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                },\r\n"
				+ "                                                {\"type\": \"FloorBlock\"}\r\n"
				+ "                                            ],\r\n"
				+ "                                            \"rule\": \"STATE_DEF\",\r\n"
				+ "                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                        },\r\n"
				+ "                                        {\"type\": \"FloorBlock\"}\r\n"
				+ "                                    ],\r\n"
				+ "                                    \"rule\": \"STATE_DEFLIST\",\r\n"
				+ "                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                }\r\n"
				+ "                            ],\r\n"
				+ "                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"rule\": \"STATE_DEFLIST\",\r\n"
				+ "                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                    }],\r\n"
				+ "                    \"type\": \"ChildrenBlock\"\r\n"
				+ "                },\r\n"
				+ "                {\"type\": \"FloorBlock\"},\r\n"
				+ "                {\r\n"
				+ "                    \"children\": [{\r\n"
				+ "                        \"blocks\": [{\r\n"
				+ "                            \"children\": [\r\n"
				+ "                                {\r\n"
				+ "                                    \"blocks\": [\r\n"
				+ "                                        {\r\n"
				+ "                                            \"name\": {\r\n"
				+ "                                                \"text\": \"Def Transition\",\r\n"
				+ "                                                \"type\": \"StrBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            \"right\": [\r\n"
				+ "                                                {\r\n"
				+ "                                                    \"blocks\": [{\r\n"
				+ "                                                        \"name\": {\r\n"
				+ "                                                            \"text\": \"name\",\r\n"
				+ "                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        \"right\": [{\r\n"
				+ "                                                            \"blocks\": [{\r\n"
				+ "                                                                \"text\": \"house\",\r\n"
				+ "                                                                \"type\": \"InputBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"rule\": \"NAME\",\r\n"
				+ "                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"type\": \"HeaderBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"rule\": \"STATE\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                },\r\n"
				+ "                                                {\r\n"
				+ "                                                    \"text\": \" to \",\r\n"
				+ "                                                    \"type\": \"StrBlock\"\r\n"
				+ "                                                },\r\n"
				+ "                                                {\r\n"
				+ "                                                    \"blocks\": [{\r\n"
				+ "                                                        \"name\": {\r\n"
				+ "                                                            \"text\": \"name\",\r\n"
				+ "                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        \"right\": [{\r\n"
				+ "                                                            \"blocks\": [{\r\n"
				+ "                                                                \"text\": \"super\",\r\n"
				+ "                                                                \"type\": \"InputBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"rule\": \"NAME\",\r\n"
				+ "                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"type\": \"HeaderBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"rule\": \"STATE\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }\r\n"
				+ "                                            ],\r\n"
				+ "                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                        },\r\n"
				+ "                                        {\r\n"
				+ "                                            \"children\": [{\r\n"
				+ "                                                \"blocks\": [{\r\n"
				+ "                                                    \"blocks\": [{\r\n"
				+ "                                                        \"names\": [{\r\n"
				+ "                                                            \"text\": \"true\",\r\n"
				+ "                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"type\": \"InnerBlock\"\r\n"
				+ "                                                    }],\r\n"
				+ "                                                    \"rule\": \"TRUE_T\",\r\n"
				+ "                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                }],\r\n"
				+ "                                                \"rule\": \"TRANSITION_TYPE\",\r\n"
				+ "                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                            }],\r\n"
				+ "                                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                                        },\r\n"
				+ "                                        {\"type\": \"FloorBlock\"}\r\n"
				+ "                                    ],\r\n"
				+ "                                    \"rule\": \"TRANSITION_DEF\",\r\n"
				+ "                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                },\r\n"
				+ "                                {\r\n"
				+ "                                    \"blocks\": [{\r\n"
				+ "                                        \"children\": [\r\n"
				+ "                                            {\r\n"
				+ "                                                \"blocks\": [\r\n"
				+ "                                                    {\r\n"
				+ "                                                        \"name\": {\r\n"
				+ "                                                            \"text\": \"Def Transition\",\r\n"
				+ "                                                            \"type\": \"StrBlock\"\r\n"
				+ "                                                        },\r\n"
				+ "                                                        \"right\": [\r\n"
				+ "                                                            {\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"name\": {\r\n"
				+ "                                                                        \"text\": \"name\",\r\n"
				+ "                                                                        \"type\": \"StrBlock\"\r\n"
				+ "                                                                    },\r\n"
				+ "                                                                    \"right\": [{\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"text\": \"super\",\r\n"
				+ "                                                                            \"type\": \"InputBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"NAME\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"STATE\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            {\r\n"
				+ "                                                                \"text\": \" to \",\r\n"
				+ "                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            {\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"name\": {\r\n"
				+ "                                                                        \"text\": \"name\",\r\n"
				+ "                                                                        \"type\": \"StrBlock\"\r\n"
				+ "                                                                    },\r\n"
				+ "                                                                    \"right\": [{\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"text\": \"house\",\r\n"
				+ "                                                                            \"type\": \"InputBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"NAME\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"STATE\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }\r\n"
				+ "                                                        ],\r\n"
				+ "                                                        \"type\": \"HeaderBlock\"\r\n"
				+ "                                                    },\r\n"
				+ "                                                    {\r\n"
				+ "                                                        \"children\": [{\r\n"
				+ "                                                            \"blocks\": [{\r\n"
				+ "                                                                \"blocks\": [{\r\n"
				+ "                                                                    \"name\": {\r\n"
				+ "                                                                        \"text\": \"prob\",\r\n"
				+ "                                                                        \"type\": \"StrBlock\"\r\n"
				+ "                                                                    },\r\n"
				+ "                                                                    \"right\": [{\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"text\": \"num\",\r\n"
				+ "                                                                            \"type\": \"InputBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"NUM\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"rule\": \"PROB_T\",\r\n"
				+ "                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                            }],\r\n"
				+ "                                                            \"rule\": \"TRANSITION_TYPE\",\r\n"
				+ "                                                            \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                        }],\r\n"
				+ "                                                        \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                    },\r\n"
				+ "                                                    {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                ],\r\n"
				+ "                                                \"rule\": \"TRANSITION_DEF\",\r\n"
				+ "                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                            },\r\n"
				+ "                                            {\r\n"
				+ "                                                \"blocks\": [\r\n"
				+ "                                                    {\r\n"
				+ "                                                        \"blocks\": [\r\n"
				+ "                                                            {\r\n"
				+ "                                                                \"name\": {\r\n"
				+ "                                                                    \"text\": \"Def Transition\",\r\n"
				+ "                                                                    \"type\": \"StrBlock\"\r\n"
				+ "                                                                },\r\n"
				+ "                                                                \"right\": [\r\n"
				+ "                                                                    {\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"name\": {\r\n"
				+ "                                                                                \"text\": \"name\",\r\n"
				+ "                                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                                            },\r\n"
				+ "                                                                            \"right\": [{\r\n"
				+ "                                                                                \"blocks\": [{\r\n"
				+ "                                                                                    \"text\": \"super\",\r\n"
				+ "                                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                                }],\r\n"
				+ "                                                                                \"rule\": \"NAME\",\r\n"
				+ "                                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"STATE\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    },\r\n"
				+ "                                                                    {\r\n"
				+ "                                                                        \"text\": \" to \",\r\n"
				+ "                                                                        \"type\": \"StrBlock\"\r\n"
				+ "                                                                    },\r\n"
				+ "                                                                    {\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"name\": {\r\n"
				+ "                                                                                \"text\": \"name\",\r\n"
				+ "                                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                                            },\r\n"
				+ "                                                                            \"right\": [{\r\n"
				+ "                                                                                \"blocks\": [{\r\n"
				+ "                                                                                    \"text\": \"house\",\r\n"
				+ "                                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                                }],\r\n"
				+ "                                                                                \"rule\": \"NAME\",\r\n"
				+ "                                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"STATE\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    }\r\n"
				+ "                                                                ],\r\n"
				+ "                                                                \"type\": \"HeaderBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            {\r\n"
				+ "                                                                \"children\": [{\r\n"
				+ "                                                                    \"blocks\": [{\r\n"
				+ "                                                                        \"blocks\": [{\r\n"
				+ "                                                                            \"name\": {\r\n"
				+ "                                                                                \"text\": \"prob\",\r\n"
				+ "                                                                                \"type\": \"StrBlock\"\r\n"
				+ "                                                                            },\r\n"
				+ "                                                                            \"right\": [{\r\n"
				+ "                                                                                \"blocks\": [{\r\n"
				+ "                                                                                    \"text\": \"rndfloat()\",\r\n"
				+ "                                                                                    \"type\": \"InputBlock\"\r\n"
				+ "                                                                                }],\r\n"
				+ "                                                                                \"rule\": \"NUM\",\r\n"
				+ "                                                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                            }],\r\n"
				+ "                                                                            \"type\": \"HeaderBlock\"\r\n"
				+ "                                                                        }],\r\n"
				+ "                                                                        \"rule\": \"PROB_T\",\r\n"
				+ "                                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                    }],\r\n"
				+ "                                                                    \"rule\": \"TRANSITION_TYPE\",\r\n"
				+ "                                                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                                }],\r\n"
				+ "                                                                \"type\": \"ChildrenBlock\"\r\n"
				+ "                                                            },\r\n"
				+ "                                                            {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                        ],\r\n"
				+ "                                                        \"rule\": \"TRANSITION_DEF\",\r\n"
				+ "                                                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                                                    },\r\n"
				+ "                                                    {\"type\": \"FloorBlock\"}\r\n"
				+ "                                                ],\r\n"
				+ "                                                \"rule\": \"TRANSITION_DEFLIST\",\r\n"
				+ "                                                \"type\": \"RecursiveBlock\"\r\n"
				+ "                                            }\r\n"
				+ "                                        ],\r\n"
				+ "                                        \"type\": \"ChildrenBlock\"\r\n"
				+ "                                    }],\r\n"
				+ "                                    \"rule\": \"TRANSITION_DEFLIST\",\r\n"
				+ "                                    \"type\": \"RecursiveBlock\"\r\n"
				+ "                                }\r\n"
				+ "                            ],\r\n"
				+ "                            \"type\": \"ChildrenBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"rule\": \"TRANSITION_DEFLIST\",\r\n"
				+ "                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                    }],\r\n"
				+ "                    \"type\": \"ChildrenBlock\"\r\n"
				+ "                },\r\n"
				+ "                {\"type\": \"FloorBlock\"},\r\n"
				+ "                {\r\n"
				+ "                    \"name\": {\r\n"
				+ "                        \"text\": \"init \",\r\n"
				+ "                        \"type\": \"StrBlock\"\r\n"
				+ "                    },\r\n"
				+ "                    \"right\": [{\r\n"
				+ "                        \"blocks\": [{\r\n"
				+ "                            \"text\": \"init\",\r\n"
				+ "                            \"type\": \"InputBlock\"\r\n"
				+ "                        }],\r\n"
				+ "                        \"rule\": \"NAME\",\r\n"
				+ "                        \"type\": \"RecursiveBlock\"\r\n"
				+ "                    }],\r\n"
				+ "                    \"type\": \"HeaderBlock\"\r\n"
				+ "                }\r\n"
				+ "            ],\r\n"
				+ "            \"rule\": \"FSM_DEF\",\r\n"
				+ "            \"type\": \"RecursiveBlock\"\r\n"
				+ "        }");
		FSMBlockTranslation t = new FSMBlockTranslation();
		t.evaluate(o);
	}
}
