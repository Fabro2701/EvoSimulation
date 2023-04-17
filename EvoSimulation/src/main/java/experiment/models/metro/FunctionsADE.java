package experiment.models.metro;

import simulator.model.entity.individuals.MyIndividual;

public class FunctionsADE {
      public static void AAction19(MyIndividual e) {
            e.setAttribute("dest","A");
      }
      public static void DAction33(MyIndividual e) {
            e.setAttribute("dest","D");
      }
      public static void EAction53(MyIndividual e) {
            e.setAttribute("dest","E");
      }
      public static boolean transition32() {
            return true;
      }
      public static boolean transition46(MyIndividual e) {
            return e.getAttribute("current").equals("A");
      }
      public static boolean transition66(MyIndividual e) {
            return e.getAttribute("current").equals("D");
      }
      public static boolean transition67(MyIndividual e) {
            return e.getAttribute("current").equals("E");
      }
}

