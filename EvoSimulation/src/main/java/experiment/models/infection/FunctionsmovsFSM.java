package experiment.models.infection;

import simulator.model.entity.Entity;

public class FunctionsmovsFSM {
      public static void houseAction19(Entity e) {
             e.setAttribute("dest","house");
      }
      public static void restaurantAction45(Entity e) {
             e.setAttribute("dest","restaurant");
      }
      public static void barAction58(Entity e) {
             e.setAttribute("dest","bar");
      }
      public static boolean transition71() {
            return true;
      }
      public static boolean transition72(Entity e) {
            return e.getAttribute("current").equals("house");
      }
      public static boolean transition73() {
            return true;
      }
      public static boolean transition74() {
            return true;
      }
      public static boolean transition75(Entity e) {
            return e.getAttribute("current").equals("restaurant");
      }
      public static boolean transition76(Entity e) {
            return e.getAttribute("current").equals("bar");
      }
}

