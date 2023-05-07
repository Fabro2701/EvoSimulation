package experiment.models.infection;

import genome_editing.model.RandomSingleton;
import simulator.model.entity.Entity;
import simulator.model.evaluation.ActionEvaluator;

public class FunctionsinfectionFSMmod {
      public static void HealthyAction19(Entity e) {
            if(!e.getAttribute("status").equals("infected"))e.setAttribute("status","healthy");
      }
      public static void SickAction33(Entity e) {
            e.setAttribute("status","infected");
      }
      public static void InmuneAction53(Entity e) {
            e.setAttribute("status","inmune");
      }
      public static void DeathAction56(Entity e) {
            e.dispose();
      }
      public static boolean transition32() {
            return true;
      }
      public static boolean transition46(Entity e) {
            return e.getAttribute("status").equals("infected");
      }
      public static boolean transition54() {
            return RandomSingleton.nextFloat()<=((Number)ActionEvaluator.getGlobalVariable("recoveryProb")).floatValue();
      }
      public static boolean transition55(Entity e) {
            return RandomSingleton.nextFloat()<=((Number)ActionEvaluator.getGlobalVariable("lossinmuneProb")).floatValue()
                        && !((Boolean)e.getAttribute("vaccinated"));
      }
      public static boolean transition69() {
            return RandomSingleton.nextFloat()<=((Number)ActionEvaluator.getGlobalVariable("infectiondeathProb")).floatValue();
      }
      public static boolean transition70(Entity e) {
            return (Boolean)e.getAttribute("vaccinated");
      }
      public static boolean transition71(Entity e) {
            return (Boolean)e.getAttribute("vaccinated");
      }
}

