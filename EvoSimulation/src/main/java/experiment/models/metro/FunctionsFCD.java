package experiment.models.metro;

import simulator.model.entity.Entity;

public class FunctionsFCD {
      public static void FAction19(Entity e) {
            e.setAttribute("dest","F");
      }
      public static void CAction32(Entity e) {
            e.setAttribute("dest","C");
      }
      public static void DAction45(Entity e) {
            e.setAttribute("dest","D");
      }
      public static void CauxAction62(Entity e) {
            e.setAttribute("dest","C");
      }
      public static boolean transition58() {
            return true;
      }
      public static boolean transition59(Entity e) {
            return e.getAttribute("current").equals("F");
      }
      public static boolean transition60(Entity e) {
            return e.getAttribute("current").equals("C");
      }
      public static boolean transition75(Entity e) {
            return e.getAttribute("current").equals("D");
      }
      public static boolean transition76(Entity e) {
            return e.getAttribute("current").equals("C");
      }
}

