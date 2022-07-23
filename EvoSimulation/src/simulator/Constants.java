package simulator;

import util.Pair;

public final class Constants {
	public static int jsonView = 0;
	public static final int INTERACTION_DISTANCE = 4;
	public static float MOVEMENT_ENERGY_COST_CONSTANT = 0.05f;
	public static float LIVE_ENERGY_COST_CONSTANT = 0.3f;
	public static float HEAT_LIVE_ENERGY_COST_CONSTANT = 0.0015f;
	public static float FOOD_ENERGY_GIVEN_CONSTANT = 10.0f;
	public static final int PHEROMONE_LENGTH = 8;

	public enum MAP_TYPE {
		LAND, VOID;
	}

	public enum MOVE {
		UP {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(0, -1);
			}
		},
		RIGHT {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(1, 0);
			}
		},
		DOWN {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(0, 1);
			}
		},
		LEFT {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(-1, 0);
			}
		},
		NEUTRAL {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(0, 0);
			}
		};

		public abstract Pair<Integer, Integer> getPosChange();
	}
}
