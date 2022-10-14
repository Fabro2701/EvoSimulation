package simulator;

import java.util.Random;

public class RandomSingleton {
    private static RandomSingleton instance;
    private Random _rnd;

    private RandomSingleton() {
    	_rnd = new Random(98);
    }
    private RandomSingleton(long s) {
    	_rnd = new Random(s);
    }

    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }

    public static double nextDouble() {
         return getInstance()._rnd.nextDouble();
    }
    public static int nextInt(int u) {
        return getInstance()._rnd.nextInt(u);
   }
    public static double nextFloat() {
        return getInstance()._rnd.nextFloat();
   }
    public static void setSeed(long s) {
    	instance = new RandomSingleton(s);
    }
}