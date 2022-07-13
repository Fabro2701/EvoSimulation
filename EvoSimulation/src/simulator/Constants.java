package simulator;

import util.Pair;

public final class Constants {
	public static int jsonView = 0;
	public static final int INTERACTION_DISTANCE = 4; 
	public enum MOVE {
        UP {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(0,-1);
			}
		},
        RIGHT {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(1,0);
			}
		},
        DOWN {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(0,1);
			}
		},
        LEFT {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(-1,0);
			}
		},
        NEUTRAL {
			@Override
			public Pair<Integer, Integer> getPosChange() {
				return new Pair<>(0,0);
			}
		};
		public abstract Pair<Integer,Integer> getPosChange();
    }
}
