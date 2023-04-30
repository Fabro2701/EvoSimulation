package experiment.models.metro;

import simulator.model.entity.Entity;

public class FunctionsADEFB {
      public static void AAction19(Entity e) {
            e.setAttribute("dest","A");
      }
      public static void DAction32(Entity e) {
            e.setAttribute("dest","D");
      }
      public static void EAction45(Entity e) {
            e.setAttribute("dest","E");
      }
      public static void FAction58(Entity e) {
            e.setAttribute("dest","F");
      }
      public static void BAction71(Entity e) {
            e.setAttribute("dest","B");
      }
      public static boolean transition84() {
            return true;
      }
      public static boolean transition85(Entity e) {
            return e.getAttribute("current").equals("A");
      }
      public static boolean transition86(Entity e) {
            return e.getAttribute("current").equals("D");
      }
      public static boolean transition87(Entity e) {
            return e.getAttribute("current").equals("E");
      }
      public static boolean transition88(Entity e) {
            return e.getAttribute("current").equals("F");
      }
      public static boolean transition89(Entity e) {
            return e.getAttribute("current").equals("B");
      }
}

