package experiment.models.metro;

import simulator.model.entity.Entity;

public class FunctionsABC {
      public static void AAction19(Entity e) {
            e.setAttribute("dest","A");
      }
      public static void BAction33(Entity e) {
            e.setAttribute("dest","B");
      }
      public static void CAction53(Entity e) {
            e.setAttribute("dest","C");
      }
      public static boolean transition32() {
            return true;
      }
      public static boolean transition46(Entity e) {
            return e.getAttribute("current").equals("A");
      }
      public static boolean transition66(Entity e) {
            return e.getAttribute("current").equals("B");
      }
      public static boolean transition67(Entity e) {
            return e.getAttribute("current").equals("C");
      }
}

