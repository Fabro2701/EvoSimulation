package experiment.models.metro;

import simulator.model.entity.Entity;

public class FunctionsADE {
      public static void AAction19(Entity e) {
            e.setAttribute("dest","A");
      }
      public static void DAction33(Entity e) {
            e.setAttribute("dest","D");
      }
      public static void EAction53(Entity e) {
            e.setAttribute("dest","E");
      }
      public static boolean transition32() {
            return true;
      }
      public static boolean transition46(Entity e) {
            return e.getAttribute("current").equals("A");
      }
      public static boolean transition66(Entity e) {
            return e.getAttribute("current").equals("D");
      }
      public static boolean transition67(Entity e) {
            return e.getAttribute("current").equals("E");
      }
}

