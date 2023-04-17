package state_diagram.product;

import simulator.model.entity.individuals.MyIndividual;

public class Functions {
      public static void houseAction19(MyIndividual e) {
             e.setAttribute("dest","house");
      }
      public static void restaurantAction45(MyIndividual e) {
             e.setAttribute("dest","restaurant");
      }
      public static void barAction58(MyIndividual e) { 
             e.setAttribute("dest","bar");
      }
      public static boolean transition71() {
            return true;
      }
      public static boolean transition72(MyIndividual e) {
            return e.getAttribute("current").equals("house");
      }
      public static boolean transition73() {
            return true;
      }
      public static boolean transition74() {
            return true;
      }
      public static boolean transition75(MyIndividual e) {
            return e.getAttribute("current").equals("restaurant");
      }
      public static boolean transition76(MyIndividual e) {
            return e.getAttribute("current").equals("bar");
      }
}

