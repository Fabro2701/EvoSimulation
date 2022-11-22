package simulator;

import util.Pair;

public final class Constants {
	public static int jsonView = 0;
	public static final int INTERACTION_DISTANCE = 4;
	public static final int PHEROMONE_LENGTH = 8;
	
	public static final int CHROMOSOME_LENGTH = 70;
	public static final int PLOIDY = 2;
	
	public static final int DEFAULT_OBSERVATIONS_REFRESH_RATE = 1;
	
	public static final int STATS_PANEL_RATIO = 3;//aspect ratio
	
	public static final float ENTITY_OBSERVATION_DISTANCE = 120f;//observation radius
	

	public enum NODE_TYPE {
		LAND, VOID;
	}

	public enum ACTION {
		NOTHING, REPRODUCTION, ATTACK;
	}
	public enum STATE {
		EAT, MOVE, REST;
	}
	public enum MOVE { 
		
		UP(new Pair<>(0, -1),false) {
		},
		RIGHT(new Pair<>(1, 0),false) {
		},
		DOWN(new Pair<>(0, 1),false) {
		},
		LEFT(new Pair<>(-1, 0),false) {
		},
		NEUTRAL(new Pair<>(0, 0),false) {
		},
		//pseudo moves
		CHASE_UP(new Pair<>(0, -1),true) {
		},
		CHASE_RIGHT(new Pair<>(1, 0),true) {
		},
		CHASE_DOWN(new Pair<>(0, 1),true) {
		},
		CHASE_LEFT(new Pair<>(-1, 0),true) {
		},
		RUNAWAY_UP(new Pair<>(0, -1),true) {
		},
		RUNAWAY_RIGHT(new Pair<>(1, 0),true) {
		},
		RUNAWAY_DOWN(new Pair<>(0, 1),true) {
		},
		RUNAWAY_LEFT(new Pair<>(-1, 0),true) {
		};
		Pair<Integer, Integer>change;
		boolean pseudo;
		private MOVE(Pair<Integer, Integer>change, boolean pseudo) {
			this.change = change;
			this.pseudo = pseudo;
		}
		public Pair<Integer, Integer> getPosChange(){
			return change;
		}
		public boolean isPseudo() {
			return pseudo;
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
	public static final String AddRandomEntitiesGeneratorEventBuilder_TYPE = "areg";
	public static final String RefreshStatsBuilder_TYPE = "rs";
	public static final String SaveSimulatorBuilder_TYPE = "ss";
	public static final String SaveStatsImageBuilder_TYPE = "sti";
	

	public static final String PasiveEntityBuilder_TYPE = "pe";
	public static final String MyIndividualBuilder_TYPE = "mi";
	public static final String SimpleRandomEntityBuilder_TYPE = "sr";
	public static final String SimpleUPEntityBuilder = "su";
}
