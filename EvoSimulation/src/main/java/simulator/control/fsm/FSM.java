package simulator.control.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import setup.gui.block.FSMBlockTranslation;
import simulator.model.entity.individuals.MyIndividual;



public class FSM<I, R> {
	HashMap<State<R>,ArrayList<Transition<I>>>states;
	State<R>current;
	
	public FSM() {
		states = new HashMap<State<R>,ArrayList<Transition<I>>>();
	}
	public State<R> run(State<R>state, I input) {
		if(state==null)return current;//init
		for(Transition<I> t:states.get(state)) {
			if(t.evaluate(input)) {
				return (State<R>) t.getTarget();
			}
		}
		return state;
	}
//	public R run(I input) {
//		for(Transition<I> t:states.get(current)) {
//			if(t.evaluate(input)) {
//				current = (State<R>) t.getTarget();
//			}
//		}
//		return current.execute();
//	}
	public void addTransition(State<R> state, Transition<I> transition) {
		if(!states.containsKey(state)) {
			ArrayList<Transition<I>>aux = new ArrayList<Transition<I>>();
			states.put(state, aux);
		}
		states.get(state).add(transition);
	}
	public void setCurrent(State<R>c) {
		current = c;
	}
	public State<R>getCurrent(){
		return this.current;
	}
	
	static FSM<String, String> MyIndividualFSM = createMyIndividualFSM();
	//static FSM<String, String> MyIndividualFSM2 = createMyIndividualFSM2();//current eval doesnt work with numeric method names
	public static FSM<String, String> getMyIndividualFSM(){return MyIndividualFSM;}
	public static FSM<String, String> createMyIndividualFSM() {
		FSM<String, String> fsm = new FSM<>();
		
		State<String> houseState = new SimpleState<>("house").addJob((MyIndividual e)->e.setAttribute("rest", 100));
		State<String> supermarketState = new SimpleState<>("supermarket").addJob((MyIndividual e)->e.setAttribute("rest", 50));
		State<String> barState = new SimpleState<>("bar").addJob((MyIndividual e)->e.setAttribute("rest", 200));
		State<String> restaurantState = new SimpleState<>("restaurant").addJob((MyIndividual e)->e.setAttribute("rest", 150));
		
		//Transition<String>houseToSupermarketTranstition = new ComparisonTransition<>(supermarketState, "house");
		//Transition<String>houseToBarTranstition = new ComparisonTransition<>(barState, "house");
		Transition<String>supermarketToHouseTranstition = new ComparisonTransition<>(houseState, "supermarket");
		Transition<String>barToHouseTranstition = new ComparisonTransition<>(houseState, "bar");
		Transition<String>restaurantToHouseTranstition = new ComparisonTransition<>(houseState, "restaurant");
				
		

		Transition<String>houseToSupermarketAndBarAndRestaurantTranstition = new StochasticComparisonTransition<>(List.of(supermarketState, barState, restaurantState), 
																												  List.of(0.65f, 0.2f, 0.15f), 
																												  "house");
		//Transition<STATE>toEatTranstitionT1 = new TrueTransition<STATE>(eatState2);
		
		
		//fsm.addTransition(houseState, houseToSupermarketTranstition);
		//fsm.addTransition(houseState, houseToBarTranstition);
		fsm.addTransition(houseState, houseToSupermarketAndBarAndRestaurantTranstition);

		fsm.addTransition(supermarketState, supermarketToHouseTranstition);
		fsm.addTransition(barState, barToHouseTranstition);
		fsm.addTransition(restaurantState, restaurantToHouseTranstition);
		
		fsm.setCurrent(houseState);
		return fsm;
	}
	public static FSM<String, String> createMyIndividualFSM2() {
		JSONObject json = new JSONObject("{\r\n"
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
		return t.evaluate(json);
	}
	public static void main(String args[]) {
		System.out.println(FSM.createMyIndividualFSM2());
//		FSM<Input, Image> fsm = new FSM<Input, Image>();
//		
//		Image img1 = new ImageIcon("resources/entities/myentity.png").getImage();
//		Image img2 = new ImageIcon("resources/entities/myentity2.png").getImage();
//		Image img3 = new ImageIcon("resources/entities/food.png").getImage();
//		
//		State<Image> s1 = new SimpleState<Image>(img1);
//		State<Image> s2 = new SimpleState<Image>(img2);
//		State<Image> s3 = new SimpleState<Image>(img3);
//		
//		Transition<Input>t12 = new TrueTransition<Input>(s2);
//		Transition<Input>t23 = new TrueTransition<Input>(s3);
//		Transition<Input>t31 = new TrueTransition<Input>(s1);
//		
//		fsm.addTransition(s1, t12);
//		fsm.addTransition(s2, t23);
//		fsm.addTransition(s3, t31);
//		
//		fsm.setCurrent(s1);
//		
//		
//		for(int i=0;i<10;i++) {
//			Image img = fsm.run(null);
//			System.out.println(img.toString());
//		}
		
		
	}
}
