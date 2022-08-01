package test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
  class Test extends RecursiveTask<Integer> {
    final int i,j;
    int[] arr = {1,2,3,4,5,6,7,8,9,10};
    Test(int i, int j) { this.i=i;this.j=j; }
    protected Integer compute() {
      if (j-i<1) {
    	  
        return arr[i];
      }
      int midpoint = i + (j - i) / 2;
      Test f1 = new Test(i,midpoint);
      f1.fork();
    
      Test f2 = new Test(midpoint+1,j);
      return f2.compute() + f1.join();
    }
    public static void main(String args[]) {
    	ForkJoinPool pool = ForkJoinPool.commonPool();
    	pool.invoke(new Test(0,9));
    }
  }
 