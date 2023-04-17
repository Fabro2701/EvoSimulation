package experiment.models.metro;

import simulator.model.entity.individuals.MyIndividual;

public class FunctionsFCD {
      public static void FAction19(MyIndividual e) {
            e.setAttribute("dest","F");
      }
      public static void CAction32(MyIndividual e) {
            e.setAttribute("dest","C");
      }
      public static void DAction45(MyIndividual e) {
            e.setAttribute("dest","D");
      }
      public static void CauxAction62(MyIndividual e) {
            e.setAttribute("dest","C");
      }
      public static boolean transition58() {
            return true;
      }
      public static boolean transition59(MyIndividual e) {
            return e.getAttribute("current").equals("F");
      }
      public static boolean transition60(MyIndividual e) {
            return e.getAttribute("current").equals("C");
      }
      public static boolean transition75(MyIndividual e) {
            return e.getAttribute("current").equals("D");
      }
      public static boolean transition76(MyIndividual e) {
            return e.getAttribute("current").equals("C");
      }
}

