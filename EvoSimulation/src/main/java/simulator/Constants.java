package simulator;

import util.Pair;

public final class Constants {
	public static int jsonView = 0;
	public static final int INTERACTION_DISTANCE = 4;
	public static float MOVEMENT_ENERGY_COST_CONSTANT = 0.05f;
	public static float LIVE_ENERGY_COST_CONSTANT = 0.15f;
	public static float HEAT_LIVE_ENERGY_COST_CONSTANT = 0.0015f;
	public static float FOOD_ENERGY_GIVEN_CONSTANT = 6.0f;
	public static final int PHEROMONE_LENGTH = 8;
	public static final int FOOD_LIVE_TIME = 400;
	public static final float REPRODUCTION_COST = 20.f;
	
	public static final int CHROMOSOME_LENGTH = 70;
	public static final float DEFAULT_INITIAL_ENERGY = 100f;
	public static final int DEFAULT_INITIAL_REST_TIME = 300;
	public static final int RECOVERY_REST_TIME = 300;
	public static final float DEFAULT_WEIGHT = 1f;

	public enum MAP_TYPE {
		LAND, VOID;//not used yet
	}

	public enum MOVE { 
		
		UP(new Pair<>(0, -1)) {
		},
		RIGHT(new Pair<>(1, 0)) {
		},
		DOWN(new Pair<>(0, 1)) {
		},
		LEFT(new Pair<>(-1, 0)) {
		},
		NEUTRAL(new Pair<>(0, 0)) {
		};
		Pair<Integer, Integer>change;
		public Pair<Integer, Integer> getPosChange(){
			return change;
		}
		private MOVE(Pair<Integer, Integer>change) {
			this.change = change;
		}
	}
	
	//builder types
	public static final String BestIndividualCodeBuilder_TYPE = "bic";
	public static final String ChildDepthStatsBuilder_TYPE = "cd";
	public static final String DeadOffSpringBuilder_TYPE = "dos";
	public static final String EnergyBuilder_TYPE = "es";
	public static final String GenotypeHeterogeneityBuilder_TYPE = "gh";
	public static final String MutationReproductionBuilder_TYPE = "mr";
	public static final String PopulationAgeBuilder_TYPE = "pa";
	public static final String PopulationCountBuilder_TYPE = "pc";
	

	public static final String AddEntitiesEventBuilder = "ae";
	public static final String AddFoodConditionGeneratorBuilder_TYPE = "afcg";
	public static final String AddFoodDistributionEventBuilder_TYPE = "fe";
	public static final String AddFoodGeneratorEventBuilder_TYPE = "fg";
	public static final String AddRandomEntitiesConditionGeneratorBuilder_TYPE = "arecg";
	public static final String AddRandomEntitiesGeneratorEventBuilder_TYPE = "reg";
	public static final String SaveSimulatorBuilder_TYPE = "ss";
	

	public static final String FoodEntityBuilder_TYPE = "f";
	public static final String MyIndividualBuilder_TYPE = "mi";
	public static final String SimpleRandomEntityBuilder_TYPE = "sr";
	public static final String SimpleUPEntityBuilder = "su";
}
