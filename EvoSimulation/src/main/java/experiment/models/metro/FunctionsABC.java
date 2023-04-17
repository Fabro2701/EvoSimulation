package experiment.models.metro;

import simulator.model.entity.individuals.MyIndividual;

public class FunctionsABC {
      public static void AAction19(MyIndividual e) {
            e.setAttribute("dest","A");
      }
      public static void BAction33(MyIndividual e) {
            e.setAttribute("dest","B");
      }
      public static void CAction53(MyIndividual e) {
            e.setAttribute("dest","C");
      }
      public static boolean transition32() {
            return true;
      }
      public static boolean transition46(MyIndividual e) {
            return e.getAttribute("current").equals("A");
      }
      public static boolean transition66(MyIndividual e) {
            return e.getAttribute("current").equals("B");
      }
      public static boolean transition67(MyIndividual e) {
            return e.getAttribute("current").equals("C");
      }
}

